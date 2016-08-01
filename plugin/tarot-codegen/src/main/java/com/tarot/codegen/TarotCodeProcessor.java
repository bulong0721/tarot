package com.tarot.codegen;

import org.hibernate.jpamodelgen.*;
import org.hibernate.jpamodelgen.util.Constants;
import org.hibernate.jpamodelgen.util.TypeUtils;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Set;

/**
 * Created by Martin on 2016/8/1.
 */
@SupportedAnnotationTypes({"javax.persistence.Entity"})
public class TarotCodeProcessor extends AbstractProcessor {

    private Context context;

    @Override
    public void init(ProcessingEnvironment env) {
        super.init(env);
        context = new Context(env);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getRootElements();
        for (Element element : elements) {
            if (isJPAEntity(element)) {
                context.logMessage(Diagnostic.Kind.OTHER, "Processing annotated class " + element.toString());
            }
        }
        return false;
    }

    private boolean isJPAEntity(Element element) {
        return TypeUtils.containsAnnotation(
                element,
                Constants.ENTITY,
                Constants.MAPPED_SUPERCLASS,
                Constants.EMBEDDABLE
        );
    }
}
