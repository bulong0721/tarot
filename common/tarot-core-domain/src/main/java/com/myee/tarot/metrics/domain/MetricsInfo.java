package com.myee.tarot.metrics.domain;

import com.myee.tarot.catalog.domain.DeviceUsed;
import com.myee.tarot.core.GenericEntity;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Chay on 2016/10/13.
 */

@Entity
@Table(name = "M_MATRICSFO")
@DynamicUpdate //hibernate部分更新
public class MetricsInfo extends GenericEntity<Long, MetricsInfo> {

    @Id
    @Column(name = "METRICSINFO_ID", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(targetEntity = SystemMetrics.class, optional = false)
    @JoinColumn(name = "M_SYSTEM_METRICS_ID")
    private SystemMetrics systemMetrics;
    @ManyToOne(targetEntity = DeviceUsed.class, optional = false)
    @JoinColumn(name = "BOARD_NO")
    private DeviceUsed deviceUsed;
    @Column(name = "NAME", length=100)
    private String name; //"ramTotal" ,"romTotal"
    @Column(name = "NODE",length=100)
    private String node; //节点类型，\monitor\summary\metricsinfo\,\monitor\metrics\metricsinfo\
    @Column(name = "VALUE", length=100)
    private String value;
    @Column(name = "DESCRIPTION",length=100)
    private String description;
    @Column(name = "LOGT_IME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date logTime;//冗余数据，用于单项查询使用
    @Column(name = "CREATED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;//冗余数据，用于单项查询使用

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public SystemMetrics getSystemMetrics() {
        return systemMetrics;
    }

    public void setSystemMetrics(SystemMetrics systemMetrics) {
        this.systemMetrics = systemMetrics;
    }

    public DeviceUsed getDeviceUsed() {
        return deviceUsed;
    }

    public void setDeviceUsed(DeviceUsed deviceUsed) {
        this.deviceUsed = deviceUsed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getLogTime() {
        return logTime;
    }

    public void setLogTime(Date logTime) {
        this.logTime = logTime;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
