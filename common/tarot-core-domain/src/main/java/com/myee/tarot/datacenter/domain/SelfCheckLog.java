package com.myee.tarot.datacenter.domain;

import com.myee.tarot.core.GenericEntity;
import com.myee.tarot.merchant.domain.MerchantStore;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.JoinColumnOrFormula;
import org.hibernate.annotations.JoinColumnsOrFormulas;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by Enva on 2016/4/11.
 */

@Entity
@Table(name = "LOG_SELF_CHECK")
@DynamicUpdate //hibernate部分更新
public class SelfCheckLog extends GenericEntity<Long, SelfCheckLog> {

    @Id
    @Column(name = "SELF_CHECK_LOG_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "SELF_CHECK_LOG_SEQ_NEXT_VAL", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    private Long id;

    @Column(name = "TIME")
    private Long time;

    @NotNull
    @Column(name = "MODULE_ID")
    private Integer moduleId;

    @Column(name = "FUNCTION_ID")
    private Integer functionId;

    @Column(name = "LENGTH")
    private Integer length;

    @Column(name = "DATA", columnDefinition = "TEXT", nullable = true)
    private String data;

    @Column(name = "STORE_ID")
    private Long storeId;

    @Column(name = "BOARD_NO")
    protected String boardNo;

    @ManyToOne(targetEntity = MerchantStore.class)
    @JoinColumn(name = "STORE_ID", referencedColumnName = "STORE_ID", nullable = true, insertable = false, updatable = false)
    protected MerchantStore store;

    //    @ManyToOne(targetEntity = EventLevel.class, fetch = FetchType.LAZY)
//    @JoinColumn(name = "EVENT_LEVEL", referencedColumnName = "EVENT")
    @Column(name = "EVENT_LEVEL")
    protected String eventLevel;

    @ManyToOne(targetEntity = EventModule.class)
    @JoinColumnsOrFormulas({
            @JoinColumnOrFormula(column = @JoinColumn(name = "MODULE_ID", referencedColumnName = "MODULE_ID", nullable = true, insertable = false, updatable = false)),
            @JoinColumnOrFormula(column = @JoinColumn(name = "FUNCTION_ID", referencedColumnName = "FUNCTION_ID", nullable = true, insertable = false, updatable = false))
    })
    protected EventModule eventModule;

    public SelfCheckLog() {
    }

    public SelfCheckLog(SelfCheckLogVO vo) {
        this.setTime(vo.getTime());
        this.setFunctionId(vo.getFunctionId());
        this.setModuleId(vo.getModuleId());
        this.setData(vo.getData());
        this.setLength(vo.getLength());
    }

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

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getBoardNo() {
        return boardNo;
    }

    public void setBoardNo(String boardNo) {
        this.boardNo = boardNo;
    }

    public MerchantStore getStore() {
        return store;
    }

    public void setStore(MerchantStore store) {
        this.store = store;
    }

    public String getEventLevel() {
        return eventLevel;
    }

    public void setEventLevel(String eventLevel) {
        this.eventLevel = eventLevel;
    }

    public EventModule getEventModule() {
        return eventModule;
    }

    public void setEventModule(EventModule eventModule) {
        this.eventModule = eventModule;
    }
}
