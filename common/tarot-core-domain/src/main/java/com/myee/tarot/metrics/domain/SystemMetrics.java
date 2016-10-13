package com.myee.tarot.metrics.domain;

import com.myee.tarot.catalog.domain.DeviceUsed;
import com.myee.tarot.core.GenericEntity;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.NotEmpty;

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

    @Column(name = "PRODUCT_GLOBAL_IP", length=30)
    private String productGlobalIP;
    @Column(name = "PRODUCT_LOCAL_IP", length=30)
    private String productLocalIP;
    @Column(name = "CHARGING",columnDefinition = "TINYINT")
    private int charging;
    @Column(name = "RAM_USED", length=30)
    private String ramUsed;
    @Column(name = "ROM_USED", length=30)
    private String romUsed;
    @Column(name = "VOLUME", length=30)
    private String volume;
    @Column(name = "WIFI_STATUS", length=30)
    private String wifiStatus;
    @Column(name = "BLUETOOTH_STATE",columnDefinition = "TINYINT")
    private int bluetoothState;
    @Column(name = "SSID", length=100)
    private String SSID;//网络SSID
    @Column(name = "LOGT_IME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date logTime;
    @Column(name = "PRODUCT_POWER", length=30)
    private String productPower;
    @Column(name = "COMMENT", length=255)
    private String comment;//备注
    @Column(name = "CPU_USED", length=30)
    private String cpuUsed;
    @ManyToOne(targetEntity = DeviceUsed.class, optional = false)
    @JoinColumn(name = "BOARD_NO")
    private DeviceUsed deviceUsed;
    @OneToMany(targetEntity = AppInfo.class, fetch = FetchType.LAZY)
    private List<AppInfo> appLists;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getProductGlobalIP() {
        return productGlobalIP;
    }

    public void setProductGlobalIP(String productGlobalIP) {
        this.productGlobalIP = productGlobalIP;
    }

    public String getProductLocalIP() {
        return productLocalIP;
    }

    public void setProductLocalIP(String productLocalIP) {
        this.productLocalIP = productLocalIP;
    }

    public int getCharging() {
        return charging;
    }

    public void setCharging(int charging) {
        this.charging = charging;
    }

    public String getRamUsed() {
        return ramUsed;
    }

    public void setRamUsed(String ramUsed) {
        this.ramUsed = ramUsed;
    }

    public String getRomUsed() {
        return romUsed;
    }

    public void setRomUsed(String romUsed) {
        this.romUsed = romUsed;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getWifiStatus() {
        return wifiStatus;
    }

    public void setWifiStatus(String wifiStatus) {
        this.wifiStatus = wifiStatus;
    }

    public int getBluetoothState() {
        return bluetoothState;
    }

    public void setBluetoothState(int bluetoothState) {
        this.bluetoothState = bluetoothState;
    }

    public String getSSID() {
        return SSID;
    }

    public void setSSID(String SSID) {
        this.SSID = SSID;
    }

    public Date getLogTime() {
        return logTime;
    }

    public void setLogTime(Date logTime) {
        this.logTime = logTime;
    }

    public String getProductPower() {
        return productPower;
    }

    public void setProductPower(String productPower) {
        this.productPower = productPower;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCpuUsed() {
        return cpuUsed;
    }

    public void setCpuUsed(String cpuUsed) {
        this.cpuUsed = cpuUsed;
    }

    public DeviceUsed getDeviceUsed() {
        return deviceUsed;
    }

    public void setDeviceUsed(DeviceUsed deviceUsed) {
        this.deviceUsed = deviceUsed;
    }

    public List<AppInfo> getAppLists() {
        return appLists;
    }

    public void setAppLists(List<AppInfo> appLists) {
        this.appLists = appLists;
    }
}
