package com.myee.tarot.log.domain;

import com.myee.tarot.core.GenericEntity;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.JoinColumnOrFormula;
import org.hibernate.annotations.JoinColumnsOrFormulas;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.xml.soap.Text;

/**
 * Created by Enva on 2016/4/11.
 */

@Entity
@Table(name = "C_SELF_CHECK_LOG")
@DynamicUpdate //hibernate部分更新
public class SelfCheckLog extends GenericEntity<Long, SelfCheckLog> {

    @Id
    @Column(name = "SELF_CHECK_LOG_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "SELF_CHECK_LOG_SEQ_NEXT_VAL",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    private Long id;

    @Column(name = "TIME")
    private Long time;

    @NotEmpty
    @Column(name = "EVENT_LEVEL")
    private Integer eventLevel;

    @NotEmpty
    @Column(name = "MODULE_ID")
    private Integer moduleId;

    @Column(name = "FUNCTION_ID")
    private Integer functionId;

    @Column(name = "LENGTH")
    private Integer length;

    @Column(name = "DATA", length=20000)
    private String data;

    @ManyToOne(targetEntity = EventLevelLog.class)
//    @ManyToOne(targetEntity = EventLevelLog.class, optional = true, cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "EVENT_LEVEL",referencedColumnName = "EVENT", nullable = true, insertable =false, updatable = false)
    protected EventLevelLog eventLevelLog;

    @ManyToOne(targetEntity = ModuleLog.class)
//    @ManyToOne(targetEntity = ModuleLog.class, optional = true, cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumnsOrFormulas({
            @JoinColumnOrFormula(column=@JoinColumn(name ="MODULE_ID", referencedColumnName ="MODULE_ID", nullable = true, insertable =false, updatable = false)),
            @JoinColumnOrFormula(column=@JoinColumn(name ="FUNCTION_ID", referencedColumnName ="FUNCTION_ID", nullable = true, insertable =false, updatable = false))
            //            @JoinColumnOrFormula( formula=@JoinFormula(value="select 1 from dual", referencedColumnName="ID_MEGA")),
    })
    //    @JoinColumn(name = "moduleId",referencedColumnName = "moduleId")
    protected ModuleLog moduleLog;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Integer getEventLevel() {
        return eventLevel;
    }

    public void setEventLevel(Integer eventLevel) {
        this.eventLevel = eventLevel;
    }

    public Integer getModuleId() {
        return moduleId;
    }

    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }

    public Integer getFunctionId() {
        return functionId;
    }

    public void setFunctionId(Integer functionId) {
        this.functionId = functionId;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public EventLevelLog getEventLevelLog() {
        return eventLevelLog;
    }

    public void setEventLevelLog(EventLevelLog eventLevelLog) {
        this.eventLevelLog = eventLevelLog;
    }

    public ModuleLog getModuleLog() {
        return moduleLog;
    }

    public void setModuleLog(ModuleLog moduleLog) {
        this.moduleLog = moduleLog;
    }
}
