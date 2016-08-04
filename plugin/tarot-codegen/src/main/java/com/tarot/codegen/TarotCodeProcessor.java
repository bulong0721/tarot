package com.tarot.codegen;

import com.querydsl.apt.Configuration;
import com.querydsl.apt.jpa.JPAConfiguration;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.persistence.*;

/**
 * Created by Martin on 2016/8/3.
 */
@SupportedAnnotationTypes({"javax.persistence.Entity"})
public class TarotCodeProcessor extends AbstractTarotProcessor {

    protected Configuration createConfiguration(RoundEnvironment roundEnv) {
        Class entity = Entity.class;
        Class superType = MappedSuperclass.class;
        Class embeddable = Embeddable.class;
        Class embedded = Embedded.class;
        Class skip = Transient.class;
        return new JPAConfiguration(roundEnv, this.processingEnv, this.processingEnv.getOptions(), entity, superType, embeddable, embedded, skip);
    }
}
