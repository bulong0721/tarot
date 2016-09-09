//package com.tarot.codegen;
//
//import org.apache.openjpa.jdbc.meta.ClassMapping;
//import org.apache.openjpa.jdbc.meta.FieldMapping;
//import org.apache.openjpa.jdbc.meta.ReverseCustomizer;
//import org.apache.openjpa.jdbc.meta.ReverseMappingTool;
//import org.apache.openjpa.jdbc.schema.Column;
//import org.apache.openjpa.jdbc.schema.ForeignKey;
//import org.apache.openjpa.jdbc.schema.Table;
//
//import java.util.Properties;
//
///**
// * Created by Martin on 2016/8/2.
// */
//public class TarotJPACustomizer implements ReverseCustomizer {
//    private ReverseMappingTool mappingTool;
//
//    @Override
//    public void setConfiguration(Properties properties) {
//
//    }
//
//    @Override
//    public void setTool(ReverseMappingTool reverseMappingTool) {
//        this.mappingTool = reverseMappingTool;
//    }
//
//    @Override
//    public int getTableType(Table table, int defaultType) {
//        return defaultType;
//    }
//
//    @Override
//    public String getClassName(Table table, String s) {
//        return null;
//    }
//
//    @Override
//    public void customize(ClassMapping classMapping) {
//
//    }
//
//    @Override
//    public String getClassCode(ClassMapping classMapping) {
//        return null;
//    }
//
//    @Override
//    public String getFieldName(ClassMapping classMapping, Column[] columns, ForeignKey foreignKey, String s) {
//        return null;
//    }
//
//    @Override
//    public void customize(FieldMapping fieldMapping) {
//
//    }
//
//    @Override
//    public String getInitialValue(FieldMapping fieldMapping) {
//        return null;
//    }
//
//    @Override
//    public String getDeclaration(FieldMapping fieldMapping) {
//        return null;
//    }
//
//    @Override
//    public String getFieldCode(FieldMapping fieldMapping) {
//        return null;
//    }
//
//    @Override
//    public boolean unmappedTable(Table table) {
//        return false;
//    }
//
//    @Override
//    public void close() {
//
//    }
//}
