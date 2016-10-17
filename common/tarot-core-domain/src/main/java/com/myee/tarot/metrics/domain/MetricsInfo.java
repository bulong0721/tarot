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


}
