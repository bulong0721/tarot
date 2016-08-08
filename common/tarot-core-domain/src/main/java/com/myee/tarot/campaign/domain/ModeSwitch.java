package com.myee.tarot.campaign.domain;

import com.myee.tarot.core.GenericEntity;

import javax.persistence.*;

/**
 * Created by Administrator on 2016/8/2.
 */
@Entity
@Table(name = "P_MODE_SWITCH")
public class ModeSwitch extends GenericEntity<Long, ModeSwitch> {

    @Id
    @Column(name = "SWITCH_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "MODE_SWITCH_SEQ_NEXT_VAL",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    protected Long id;

    @Column(name= "STORE_ID",unique = true)
    private Long storeId;

    @Column(name = "STATUS")
    private int status;   //0为默认关闭，1为开启

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
}
