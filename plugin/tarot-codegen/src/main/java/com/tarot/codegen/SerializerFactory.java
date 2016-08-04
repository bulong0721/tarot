package com.tarot.codegen;

import com.myee.tarot.core.dao.GenericEntityDao;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.core.service.GenericEntityService;
import com.myee.tarot.core.service.GenericEntityServiceImpl;
import com.mysema.codegen.CodeWriter;
import com.mysema.codegen.model.ClassType;
import com.mysema.codegen.model.Parameter;
import com.mysema.codegen.model.SimpleType;
import com.mysema.codegen.model.Type;
import com.querydsl.codegen.EntityType;
import com.querydsl.codegen.Serializer;
import com.querydsl.codegen.SerializerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.processing.ProcessingEnvironment;
import java.io.IOException;

/**
 * Created by Martin on 2016/8/3.
 */
public class SerializerFactory {
    private Context               context;
    private ProcessingEnvironment processingEnv;
    private TarotCodeChain        chain;

    public SerializerFactory(ProcessingEnvironment processingEnv, EntityType type) {
//        this.context = context;
        this.processingEnv = processingEnv;
        this.chain = new TarotCodeChain(type);
    }

    private Serializer daoSerializer = new Serializer() {

        @Override
        public void serialize(EntityType type, SerializerConfig serializerConfig, CodeWriter writer) throws IOException {
            TarotCodeItem codeItem = chain.dao;
            writer.packageDecl(codeItem.packageName);
            Type[] typeArray = new Type[2];
            typeArray = type.getParameters().toArray(typeArray);
            ClassType superType = new ClassType(GenericEntityDao.class, typeArray);
            Type daoType = codeItem.toSimpleType();
            writer.importClasses(typeArray[1].getFullName());
            writer.imports(GenericEntityDao.class);
            writer.beginInterface(daoType, superType);
            writer.end();
        }
    };

    private Serializer daoImplSerializer = new Serializer() {

        @Override
        public void serialize(EntityType type, SerializerConfig serializerConfig, CodeWriter writer) throws IOException {
            TarotCodeItem codeItem = chain.daoImpl;
            writer.packageDecl(codeItem.packageName);
            Type[] typeArray = new Type[2];
            typeArray = type.getParameters().toArray(typeArray);
            ClassType superType = new ClassType(GenericEntityDaoImpl.class, typeArray);
            Type daoImplType = codeItem.toSimpleType();
            writer.importClasses(typeArray[1].getFullName(), chain.dao.javaType.getFullName());
            writer.imports(Repository.class, GenericEntityDaoImpl.class);
            writer.annotation(Repository.class);
            writer.beginClass(daoImplType, superType, chain.dao.javaType);
            writer.end();
        }
    };

    private Serializer svcSerializer = new Serializer() {

        @Override
        public void serialize(EntityType type, SerializerConfig serializerConfig, CodeWriter writer) throws IOException {
            TarotCodeItem codeItem = chain.svc;
            writer.packageDecl(codeItem.packageName);
            Type[] typeArray = new Type[2];
            type.getParameters().toArray(typeArray);
            ClassType superType = new ClassType(GenericEntityService.class, typeArray);
            Type svcType = codeItem.toSimpleType();
            writer.importClasses(typeArray[1].getFullName(), chain.dao.javaType.getFullName());
            writer.imports(GenericEntityService.class);
            writer.beginInterface(svcType, superType);
            writer.end();
        }
    };

    private Serializer svcImplSerializer = new Serializer() {

        @Override
        public void serialize(EntityType type, SerializerConfig serializerConfig, CodeWriter writer) throws IOException {
            TarotCodeItem codeItem = chain.svcImpl;
            writer.packageDecl(codeItem.packageName);
            Type[] typeArray = new Type[2];
            type.getParameters().toArray(typeArray);
            ClassType superType = new ClassType(GenericEntityServiceImpl.class, typeArray);
            Type svcImplType = codeItem.toSimpleType();
            writer.importClasses(typeArray[1].getFullName(), chain.dao.javaType.getFullName(), chain.svc.javaType.getFullName());
            writer.imports(Service.class, Autowired.class, GenericEntityServiceImpl.class);
            writer.annotation(Service.class);
            writer.beginClass(svcImplType, superType, chain.svc.javaType);
            writer.protectedField(chain.dao.javaType, "dao");
            Parameter daoParameter = new Parameter("dao", chain.dao.javaType);
            writer.annotation(Autowired.class);
            writer.beginConstructor(daoParameter);
            writer.line("super(dao);");
            writer.line("this.dao = dao;");
            writer.end();
            writer.end();
        }
    };

    public Serializer getDaoSerializer(TarotCodeItem codeItem) {
        chain.dao = codeItem;
        return daoSerializer;
    }

    public Serializer getDaoImplSerializer(TarotCodeItem codeItem) {
        chain.daoImpl = codeItem;
        return daoImplSerializer;
    }

    public Serializer getSvcSerializer(TarotCodeItem codeItem) {
        chain.svc = codeItem;
        return svcSerializer;
    }

    public Serializer getSvcImplSerializer(TarotCodeItem codeItem) {
        chain.svcImpl = codeItem;
        return svcImplSerializer;
    }
}
