package com.myee.tarot.weixin.domain;

import com.myee.tarot.core.GenericEntity;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Martin on 2016/1/18.
 */
@Entity
@Table(name = "CA_INSTRUCTION_LOG")
@DynamicUpdate
public class RInstructionLog extends GenericEntity<Long, RInstructionLog> {
    @Id
    @Column(name = "CA_INSTRUCTION_LOG_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", pkColumnValue = "CA_INSTRUCTION_LOG_SEQ_NEXT_VAL", valueColumnName = "SEQ_COUNT", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    private Long    id;
    @Column(name = "SUBJECT_ID")
    private Long    subjectId;
    @Column(name = "INSTRUCTION_LEVEL1")
    private int     instructionLevel1;
    @Column(name = "INSTRUCTION_LEVEL2")
    private Integer instructionLevel2;
    @Column(name = "CONTEXT1")
    private String  context1;
    @Column(name = "CONTEXT2")
    private String  context2;
    @Column(name = "TIME_OCCURED")
    private Date    timeOccured;

    public String getContext1() {
        return context1;
    }

    public void setContext1(String context1) {
        this.context1 = context1;
    }

    public String getContext2() {
        return context2;
    }

    public void setContext2(String context2) {
        this.context2 = context2;
    }

    public int getInstructionLevel1() {
        return instructionLevel1;
    }

    public void setInstructionLevel1(int instructionLevel1) {
        this.instructionLevel1 = instructionLevel1;
    }

    public Integer getInstructionLevel2() {
        return instructionLevel2;
    }

    public void setInstructionLevel2(Integer instructionLevel2) {
        this.instructionLevel2 = instructionLevel2;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public Date getTimeOccured() {
        return timeOccured;
    }

    public void setTimeOccured(Date timeOccured) {
        this.timeOccured = timeOccured;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

}
