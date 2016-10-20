package com.myee.tarot.metric.domain;

import com.myee.tarot.catalog.domain.DeviceUsed;
import com.myee.tarot.core.GenericEntity;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
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
//    @ManyToOne(targetEntity = DeviceUsed.class, optional = false)
//    @JoinColumn(name = "BOARD_NO")
//    private DeviceUsed deviceUsed;
    @Column(name = "BOARD_NO",length=100)
    private String boardNo;
    @Column(name = "LOG_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date logTime;
    @OneToMany(mappedBy = "systemMetricsId",targetEntity = AppInfo.class, fetch = FetchType.EAGER)
    private List<AppInfo> appList  = new ArrayList<AppInfo>();
    @OneToMany(mappedBy = "systemMetricsId",targetEntity = MetricInfo.class, fetch = FetchType.EAGER)
    private List<MetricInfo> metricInfoList = new ArrayList<MetricInfo>();
    @Column(name = "NODE",length=100)
    private String node; //节点类型，用于表明当前类在节点关系中的层级，\monitor\metric\,\monitor\summary\
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

    public String getBoardNo() {
        return boardNo;
    }

    public void setBoardNo(String boardNo) {
        this.boardNo = boardNo;
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

    public List<MetricInfo> getMetricInfoList() {
        return metricInfoList;
    }

    public void setMetricInfoList(List<MetricInfo> metricInfoList) {
        this.metricInfoList = metricInfoList;
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
