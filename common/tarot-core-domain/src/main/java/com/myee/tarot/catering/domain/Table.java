package com.myee.tarot.catering.domain;

import com.myee.tarot.core.GenericEntity;
import com.myee.tarot.merchant.domain.MerchantStore;

import javax.persistence.*;

/**
 * Created by Martin on 2016/4/11.
 */
@Entity
@javax.persistence.Table(name = "CA_TABLE")
public class Table extends GenericEntity<Long, Table> {

    @Id
    @Column(name = "TABLE_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "TABLE_SEQ_NEXT_VAL",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    private Long id;

    @Column(name = "NAME", nullable = false)
    protected String name;

    @Column(name = "DESCRIPTION")
    protected String description;

    @Column(name = "SCAN_CODE")
    protected String scanCode;//桌子码，无线点点笔使用

    @Column(name = "TEXT_ID")
    protected String textId;//小超人点菜使用

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = TableType.class)
    @JoinColumn(name = "TABLE_TYPE", referencedColumnName = "TABLE_TYPE_ID", nullable = false)
    private TableType tableType;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = TableZone.class)
    @JoinColumn(name = "TABLE_ZONE", referencedColumnName = "TABLE_ZONE_ID")
    private TableZone tableZone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STORE_ID")
    private MerchantStore store;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getScanCode() {
        return scanCode;
    }

    public void setScanCode(String scanCode) {
        this.scanCode = scanCode;
    }

    public String getTextId() {
        return textId;
    }

    public void setTextId(String textId) {
        this.textId = textId;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TableType getTableType() {
        return tableType;
    }

    public void setTableType(TableType tableType) {
        this.tableType = tableType;
    }

    public TableZone getTableZone() {
        return tableZone;
    }

    public void setTableZone(TableZone tableZone) {
        this.tableZone = tableZone;
    }

    public MerchantStore getStore() {
        return store;
    }

    public void setStore(MerchantStore store) {
        this.store = store;
    }
}
