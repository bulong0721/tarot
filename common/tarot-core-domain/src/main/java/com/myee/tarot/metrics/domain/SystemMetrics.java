package com.myee.tarot.metrics.domain;

import com.myee.tarot.catalog.domain.DeviceUsed;
import com.myee.tarot.core.GenericEntity;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by Chay on 2016/10/13.
 */

@Entity
@Table(name = "M_SYSTEM_METRICS")
@DynamicUpdate //hibernate部分更新
public class SystemMetrics extends GenericEntity<Long, SystemMetrics> {

    @Id
    @Column(name = "M_SYSTEM_METRICS_ID", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(targetEntity = DeviceUsed.class, optional = false)
    @JoinColumn(name = "BOARD_NO")
    private DeviceUsed deviceUsed;
    @Column(name = "LOG_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date logTime;
    @OneToMany(targetEntity = AppInfo.class, fetch = FetchType.LAZY)
    private List<AppInfo> appList;
    @OneToMany(targetEntity = MetricsInfo.class, fetch = FetchType.LAZY)
    private List<MetricsInfo> metricsInfoList;
    @Column(name = "NODE",length=100)
    private String node; //节点类型，用于表明当前类在节点关系中的层级，\monitor\metrics\,\monitor\summary\
    @Column(name = "CREATED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public DeviceUsed getDeviceUsed() {
        return deviceUsed;
    }

    public void setDeviceUsed(DeviceUsed deviceUsed) {
        this.deviceUsed = deviceUsed;
    }

    public Date getLogTime() {
        return logTime;
    }

    public void setLogTime(Date logTime) {
        this.logTime = logTime;
    }

    public List<AppInfo> getAppList() {
        return appList;
    }

    public void setAppList(List<AppInfo> appList) {
        this.appList = appList;
    }

    public List<MetricsInfo> getMetricsInfoList() {
        return metricsInfoList;
    }

    public void setMetricsInfoList(List<MetricsInfo> metricsInfoList) {
        this.metricsInfoList = metricsInfoList;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
