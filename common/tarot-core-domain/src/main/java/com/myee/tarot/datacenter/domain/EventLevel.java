package com.myee.tarot.datacenter.domain;

import com.myee.tarot.core.GenericEntity;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by Enva on 2016/4/11.
 */

@Entity
@Table(name = "LOG_EVENT_LEVEL")
@DynamicUpdate //hibernate部分更新
public class EventLevel extends GenericEntity<Long, EventLevel> {

    @Id
    @Column(name = "EVENT_LEVEL_LOG_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "EVENT_LEVEL_LOG_SEQ_NEXT_VAL",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    private Long id;

    @NotNull
    @Column(name = "EVENT")
    private Integer event;

    @NotEmpty
    @Column(name = "LEVEL", length=100)
    private String level;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Integer getEvent() {
        return event;
    }

    public void setEvent(Integer event) {
        this.event = event;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
