package com.myee.tarot.metric.domain;

import com.myee.tarot.catalog.domain.DeviceUsed;
import com.myee.tarot.core.GenericEntity;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Chay on 2016/10/13.
 */

@Entity
@Table(name = "M_MATRICS_INFO")
@DynamicUpdate //hibernate部分更新
public class MetricInfo extends GenericEntity<Long, MetricInfo> {

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
    @ManyToOne(targetEntity = MetricDetail.class, optional = false)
    @JoinColumn(name = "KEY")
    private MetricDetail metricDetail; //"ramTotal" ,"romTotal"
    @Column(name = "NODE",length=100)
    private String node; //节点类型，用于表明当前类在节点关系中的层级，\monitor\summary\metricsinfo\,\monitor\metric\metricsinfo\
    @Column(name = "VALUE", length=100)
    private String value;
    @Column(name = "DESCRIPTION",length=100)
    private String description;
    @Column(name = "LOG_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date logTime;//冗余数据，用于单项查询使用
    @Column(name = "CREATED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;//冗余数据，用于单项查询使用
    @Transient
    private int state;//0正常，1警告，2报警

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

    public MetricDetail getMetricDetail() {
        return metricDetail;
    }

    public void setMetricDetail(MetricDetail metricDetail) {
        this.metricDetail = metricDetail;
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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
