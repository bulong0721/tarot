package com.tarot.codegen;

import com.google.common.collect.Sets;
import com.mysema.query.apt.Configuration;
import com.mysema.query.codegen.EntityType;
import com.mysema.query.codegen.Serializer;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.Collection;
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

        conf = createConfiguration(roundEnv);
        context = new Context();

        processAnnotations();

        validateMetaTypes();

        serializeMetaTypes();

        return false;
    }

    private void processAnnotations() {

    }

    private void validateMetaTypes() {

    }

    private void serializeMetaTypes() {

    }

    protected Set<TypeElement> collectElements() {
        Set<TypeElement> elements = Sets.newHashSet();
        return elements;
    }

    private void serialize(EntityType model, TarotCodeChain chain) {

    }

    protected abstract Configuration createConfiguration(RoundEnvironment roundEnv);
}
