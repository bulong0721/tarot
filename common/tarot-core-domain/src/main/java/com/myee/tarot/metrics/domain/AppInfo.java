package com.myee.tarot.metrics.domain;

import com.myee.tarot.catalog.domain.DeviceUsed;
import com.myee.tarot.core.GenericEntity;
import com.myee.tarot.merchant.domain.MerchantStore;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Chay on 2016/10/13.
 */

@Entity
@Table(name = "M_APPINFO")
@DynamicUpdate //hibernate部分更新
public class AppInfo extends GenericEntity<Long, AppInfo> {

    @Id
    @Column(name = "APPINFO_ID", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(targetEntity = SystemMetrics.class, optional = false)
    @JoinColumn(name = "M_SYSTEM_METRICS_ID")
    private SystemMetrics systemMetrics;
    @ManyToOne(targetEntity = DeviceUsed.class, optional = false)
    @JoinColumn(name = "BOARD_NO")
    private DeviceUsed deviceUsed;
    @Column(name = "VERSION_CODE", length=100)
    private Long versionCode;
    @Column(name = "VERSION_NAME", length=100)
    private String versionName;//"应用名称com.taobao.ddd"
    @Column(name = "NODE",length=100)
    private String node; //节点类型，\monitor\summary\appinfo\,\monitor\metrics\appinfo\
    @Column(name = "INSTALL_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date installDate;
    @Column(name = "STATE",columnDefinition = "TINYINT")
    private int state;
    @Column(name = "TYPE",columnDefinition = "TINYINT")
    private int type;
    @Column(name = "LOGT_IME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date logTime;
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

    public Long getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(Long versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public Date getInstallDate() {
        return installDate;
    }

    public void setInstallDate(Date installDate) {
        this.installDate = installDate;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public Date getLogTime() {
        return logTime;
    }

    public void setLogTime(Date logTime) {
        this.logTime = logTime;
    }

    public SystemMetrics getSystemMetrics() {
        return systemMetrics;
    }

    public void setSystemMetrics(SystemMetrics systemMetrics) {
        this.systemMetrics = systemMetrics;
    }
}
