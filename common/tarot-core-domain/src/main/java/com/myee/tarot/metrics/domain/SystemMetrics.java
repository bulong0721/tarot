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
    @Column(name = "LOGT_IME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date logTime;
    @OneToMany(targetEntity = AppInfo.class, fetch = FetchType.LAZY)
    private List<AppInfo> appList;
    @OneToMany(targetEntity = MetricsInfo.class, fetch = FetchType.LAZY)
    private List<MetricsInfo> metricsInfoList;
    @Column(name = "NODE",length=100)
    private String node; //节点类型，\monitor\metrics\,\monitor\summary\
    @Column(name = "CREATED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
//    @Column(name = "PRODUCT_GLOBAL_IP", length=30)
//    private String productGlobalIP;
//    @Column(name = "PRODUCT_LOCAL_IP", length=30)
//    private String productLocalIP;
//    @Column(name = "CHARGING",columnDefinition = "TINYINT")
//    private int charging;
//    @Column(name = "RAM_USED", length=30)
//    private String ramUsed;
//    @Column(name = "ROM_USED", length=30)
//    private String romUsed;
//    @Column(name = "VOLUME", length=30)
//    private String volume;
//    @Column(name = "WIFI_STATUS", length=30)
//    private String wifiStatus;
//    @Column(name = "BLUETOOTH_STATE",columnDefinition = "TINYINT")
//    private int bluetoothState;
//    @Column(name = "SSID", length=100)
//    private String SSID;//网络SSID
//    @Column(name = "PRODUCT_POWER", length=30)
//    private String productPower;
//    @Column(name = "COMMENT", length=255)
//    private String comment;//备注
//    @Column(name = "CPU_USED", length=30)
//    private String cpuUsed;


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
