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
@Table(name = "M_SYSTEM_SUMMARY")
@DynamicUpdate //hibernate部分更新
public class SystemSummary extends GenericEntity<Long, SystemSummary> {

    @Id
    @Column(name = "M_SYSTEM_SUMMARY_ID", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "CPU_NAME", length=60)
    private String cpuName;
    @Column(name = "MAX_FREQUENCY", length=60)
    private String maxFrequency;
    @Column(name = "MIN_FREQUENCY", length=60)
    private String minFrequency;
    @ManyToOne(targetEntity = DeviceUsed.class, optional = false)
    @JoinColumn(name = "BOARD_NO")
    private DeviceUsed deviceUsed;
    @Column(name = "COMPUTER_TYPE", length=30)
    private String computerType;//大学士，小女生
    @Column(name = "MAC_ADDRESS", length=30)
    private String macAddress;
    @Column(name = "DEVICE_BRAND", length=60)
    private String brand;//设备制造商
    @Column(name = "DEVICE_MODE", length=60)
    private String deviceMode;//设备型号
    @Column(name = "SERIAL", length=60)
    private String serial;//设备序列号
    @Column(name = "RESOLUTION", length=60)
    private String resolution;//分辨率
    @Column(name = "LANGUAGE", length=30)
    private String language;//语言
    @Column(name = "OS_VERSION", length=60)
    private String osVersion;//系统版本
    @Column(name = "OS_KERNEL_VERSION", length=100)
    private String osKernelVersion;//系统内核版本
    @Column(name = "OS_UUID", length=60)
    private String osUUID;//系统UUID
    @OneToMany(targetEntity = AppInfo.class, fetch = FetchType.LAZY)
    private List<AppInfo> appLists;
    @Column(name = "RAM_TOTAL", length=30)
    private String ramTotal;
    @Column(name = "ROM_TOTAL", length=30)
    private String romTotal;

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

    public String getCpuName() {
        return cpuName;
    }

    public void setCpuName(String cpuName) {
        this.cpuName = cpuName;
    }

    public String getMaxFrequency() {
        return maxFrequency;
    }

    public void setMaxFrequency(String maxFrequency) {
        this.maxFrequency = maxFrequency;
    }

    public String getMinFrequency() {
        return minFrequency;
    }

    public void setMinFrequency(String minFrequency) {
        this.minFrequency = minFrequency;
    }

    public DeviceUsed getDeviceUsed() {
        return deviceUsed;
    }

    public void setDeviceUsed(DeviceUsed deviceUsed) {
        this.deviceUsed = deviceUsed;
    }

    public String getComputerType() {
        return computerType;
    }

    public void setComputerType(String computerType) {
        this.computerType = computerType;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDeviceMode() {
        return deviceMode;
    }

    public void setDeviceMode(String deviceMode) {
        this.deviceMode = deviceMode;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getOsKernelVersion() {
        return osKernelVersion;
    }

    public void setOsKernelVersion(String osKernelVersion) {
        this.osKernelVersion = osKernelVersion;
    }

    public String getOsUUID() {
        return osUUID;
    }

    public void setOsUUID(String osUUID) {
        this.osUUID = osUUID;
    }

    public List<AppInfo> getAppLists() {
        return appLists;
    }

    public void setAppLists(List<AppInfo> appLists) {
        this.appLists = appLists;
    }

    public String getRamTotal() {
        return ramTotal;
    }

    public void setRamTotal(String ramTotal) {
        this.ramTotal = ramTotal;
    }

    public String getRomTotal() {
        return romTotal;
    }

    public void setRomTotal(String romTotal) {
        this.romTotal = romTotal;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
