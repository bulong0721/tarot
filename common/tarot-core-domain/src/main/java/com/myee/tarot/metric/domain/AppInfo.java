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
@Table(name = "M_APP_INFO",indexes = @Index(name="NODE_NAME",columnList = "NODE",unique = false) )
@DynamicUpdate //hibernate部分更新
public class AppInfo extends GenericEntity<Long, AppInfo> {

    @Id
    @Column(name = "APPINFO_ID", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
//    @ManyToOne(targetEntity = SystemMetrics.class, optional = false)
//    @JoinColumn(name = "M_SYSTEM_METRICS_ID")
//    private SystemMetrics systemMetrics;
    @Column(name = "M_SYSTEM_METRICS_ID")
    private Long systemMetricsId;
//    @ManyToOne(targetEntity = DeviceUsed.class, optional = false)
//    @JoinColumn(name = "BOARD_NO")
//    private DeviceUsed deviceUsed;
    @Column(name = "BOARD_NO",length=100)
    private String boardNo;
    @Column(name = "VERSION_CODE")
    private Long versionCode;
    @Column(name = "VERSION_NAME", length=100)
    private String versionName;//"应用版本名称"
    @Column(name = "APP_NAME", length=100)
    private String appName;  //"应用名称"
    @Column(name = "PACKAGE_NAME", length=100)
    private String packageName;  //"包名 com.taobao.ddd"
    @Column(name = "PROCESS_NAME", length=100)
    private String processName;  //"进程名称"
    @Column(name = "PID")
    private Long pid;//进程ID，用于控制
    @Column(name = "NODE",length=100)
    private String node; //节点类型，用于表明当前类在节点关系中的层级，\monitor\summary\appinfo\,\monitor\metric\appinfo\
    @Column(name = "INSTALL_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date installDate;
    @Column(name = "LAST_UPDATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateTime;
    @Column(name = "STATE",columnDefinition = "TINYINT")
    private int state;//状态，0已安装，1正在运行
    @Column(name = "TYPE",columnDefinition = "TINYINT")
    private int type; //1:服务  2：进程 3：应用
    @Column(name = "LOG_TIME")
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

    public Long getSystemMetricsId() {
        return systemMetricsId;
    }

    public void setSystemMetricsId(Long systemMetricsId) {
        this.systemMetricsId = systemMetricsId;
    }

    public String getBoardNo() {
        return boardNo;
    }

    public void setBoardNo(String boardNo) {
        this.boardNo = boardNo;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }
}
