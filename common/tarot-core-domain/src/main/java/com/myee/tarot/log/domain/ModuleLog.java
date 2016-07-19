package com.myee.tarot.log.domain;

import com.myee.tarot.core.GenericEntity;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;

/**
 * Created by Enva on 2016/4/11.
 */

@Entity
@Table(name = "C_MODULE_LOG")
@DynamicUpdate //hibernate部分更新
public class ModuleLog extends GenericEntity<Long, ModuleLog> {

    @Id
    @Column(name = "MODULE_LOG_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "MODULE_LOG_SEQ_NEXT_VAL",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    private Long id;

    @NotEmpty
    @Column(name = "MODULE_ID")
    private Integer moduleId;

    @NotEmpty
    @Column(name = "MODULE_NAME", length=100)
    private String moduleName;

    @Column(name = "FUNCTION_ID")
    private Integer functionId;

    @Column(name = "FUNCTION_NAME", length=100)
    private String functionName;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Integer getModuleId() {
        return moduleId;
    }

    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Integer getFunctionId() {
        return functionId;
    }

    public void setFunctionId(Integer functionId) {
        this.functionId = functionId;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }
}
