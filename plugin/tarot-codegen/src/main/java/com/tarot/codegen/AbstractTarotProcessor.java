package com.tarot.codegen;

import com.google.common.collect.Sets;
import com.mysema.codegen.JavaWriter;
import com.mysema.codegen.model.SimpleType;
import com.mysema.codegen.model.Type;
import com.querydsl.apt.Configuration;
import com.querydsl.codegen.*;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Martin on 2016/8/1.
 */
public abstract class AbstractTarotProcessor extends AbstractProcessor {

    public static Types TYPES;

    public static Elements ELEMENTS;

    private RoundEnvironment roundEnv;

    private Configuration conf;

    private Context context;


    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        TYPES = processingEnv.getTypeUtils();
        ELEMENTS = processingEnv.getElementUtils();

        this.conf = createConfiguration(roundEnv);
        this.context = new Context();
        this.roundEnv = roundEnv;

        processAnnotations();

        validateMetaTypes();

        serializeMetaTypes();

        return false;
    }

    private void processAnnotations() {
        Set<TypeElement> elements = collectElements();

        for (TypeElement element : elements) {
            EntityType entityType = handleEntityType(element);
            registerTypeElement(entityType.getFullName(), element);
            if (element.getAnnotation(conf.getEntityAnnotation()) != null) {
                context.entityTypes.put(entityType.getFullName(), entityType);
            }
            context.allTypes.put(entityType.getFullName(), entityType);
        }

        context.clean();
    }

    private EntityType handleEntityType(TypeElement element) {
        List<? extends TypeMirror> typeArguments = ((DeclaredType) element.getSuperclass()).getTypeArguments();
        Type[] typeArray = new Type[0];
        if (null != typeArguments && typeArguments.size() >= 2) {
            typeArray = new Type[2];
            typeArray[0] = new SimpleType(declareToType(typeArguments.get(0)));
            typeArray[1] = new SimpleType(declareToType(typeArguments.get(1)));
        }
        String fullName = element.getQualifiedName().toString();
        String simpleName = element.getSimpleName().toString();
        String packageName = fullName.substring(0, fullName.indexOf(simpleName) - 1);
        SimpleType type = new SimpleType(fullName, packageName, simpleName, typeArray);
        return new EntityType(type);
    }

    Type declareToType(TypeMirror typeMirror) {
        String fullName = typeMirror.toString();
        int lastDot = fullName.lastIndexOf('.');
        String packageName = fullName.substring(0, lastDot - 1);
        String className = fullName.substring(lastDot + 1);
        return new SimpleType(fullName, packageName, className);
    }

    private void registerTypeElement(String entityName, TypeElement element) {
        Set<TypeElement> elements = context.typeElements.get(entityName);
        if (elements == null) {
            elements = new HashSet<TypeElement>();
            context.typeElements.put(entityName, elements);
        }
        elements.add(element);
    }

    private void validateMetaTypes() {

    }

    private void serializeMetaTypes() {
        if (!context.entityTypes.isEmpty()) {
            for (EntityType model : context.entityTypes.values()) {
                SerializerFactory factory = new SerializerFactory(processingEnv, model);
                TarotCodeItem codeItem = createCodeItem(model, ".dao");

                Serializer serializer = factory.getDaoSerializer(codeItem);
                if (!codeItem.fileExists()) {
                    serialize(model, codeItem.javaFile, serializer);
                }

                codeItem = createCodeItem(model, ".dao.impl");
                serializer = factory.getDaoImplSerializer(codeItem);
                if (!codeItem.fileExists()) {
                    serialize(model, codeItem.javaFile, serializer);
                }

                codeItem = createCodeItem(model, ".service");
                serializer = factory.getSvcSerializer(codeItem);
                if (!codeItem.fileExists()) {
                    serialize(model, codeItem.javaFile, serializer);
                }

                codeItem = createCodeItem(model, ".service.impl");
                serializer = factory.getSvcImplSerializer(codeItem);
                if (!codeItem.fileExists()) {
                    serialize(model, codeItem.javaFile, serializer);
                }
            }
        }
    }

    private TarotCodeItem createCodeItem(EntityType model, String pkgPostfix) {
        TarotCodeItem result = new TarotCodeItem(model);
        String packageName = model.getPackageName().replace(".domain", pkgPostfix);
        String simpleName = model.getSimpleName();

        Set<TypeElement> elements = context.typeElements.get(model.getFullName());
        if (elements == null) {
            elements = new HashSet<TypeElement>();
        }

        if (".dao".equals(pkgPostfix)) {
            simpleName += "Dao";
        } else if (".dao.impl".equals(pkgPostfix)) {
            simpleName += "DaoImpl";
        } else if (".service".equals(pkgPostfix)) {
            simpleName += "Service";
        } else if (".service.impl".equals(pkgPostfix)) {
            simpleName += "ServiceImpl";
        }

        result.packageName = packageName;
        result.simpleName = simpleName;
        String className = result.getFullName();

        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Generating " + className + " for " + elements);
        try {
            result.javaFile = processingEnv.getFiler().createSourceFile(className, elements.toArray(new Element[elements.size()]));
        } catch (IOException e) {
            if (null != processingEnv.getMessager()) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage());
            }
        }
        return result;
    }

    private void serialize(EntityType model, JavaFileObject javaFile, Serializer serializer) {
        Writer writer = null;
        try {
            writer = javaFile.openWriter();
            SerializerConfig serializerConfig = conf.getSerializerConfig(model);
            serializer.serialize(model, serializerConfig, new JavaWriter(writer));
        } catch (Exception e) {
            if (null != processingEnv.getMessager()) {
//                processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, e.getMessage());
            }
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    if (null != processingEnv.getMessager()) {
                        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage());
                    }
                }
            }
        }
    }

    protected Set<TypeElement> collectElements() {
        Set<TypeElement> elements = Sets.newHashSet();

        for (Class<? extends Annotation> annotation : conf.getEntityAnnotations()) {
            for (Element element : getElements(annotation)) {
                if (element instanceof TypeElement) {
                    elements.add((TypeElement) element);
                }
            }
        }
        return elements;
    }

    private Set<? extends Element> getElements(Class<? extends Annotation> a) {
        return roundEnv.getElementsAnnotatedWith(a);
    }


    protected abstract Configuration createConfiguration(RoundEnvironment roundEnv);
}
