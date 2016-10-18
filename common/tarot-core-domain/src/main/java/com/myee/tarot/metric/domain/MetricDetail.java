package com.myee.tarot.metric.domain;

import com.myee.tarot.core.GenericEntity;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * Created by Chay on 2016/10/13.
 */

@Entity
@Table(name = "M_MATRICS_DETAIL")
@DynamicUpdate //hibernate部分更新
public class MetricDetail extends GenericEntity<Long, MetricDetail> {

    @Id
    @Column(name = "METRICSDETAIL_ID", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "KEY", unique = true,length=100)
    private String key; //指标键名称
    @Column(name = "NAME", length=100)
    private String name; //指标显示名称
    @Column(name = "DRAW_TYPE",columnDefinition = "TINYINT")
    private int drawType;//画图类型，0:数值，1:饼图，2:直方图,3:折线图
    @Column(name = "FREQ_TIME", length=100)
    private String freqTime;//采集间隔时间
    @Column(name = "MAX_VALUE", columnDefinition = "DECIMAL")
    private float maxValue; //该指标最大值,有的参数可能会从静态参数中读取，比如CPU最大值，内存最大值
    @Column(name = "MIN_VALUE", columnDefinition = "DECIMAL")
    private float minValue; //该指标最小值
    @Column(name = "WARNING", columnDefinition = "DECIMAL")
    private float warning;//该指标警告值
    @Column(name = "ALERT", columnDefinition = "DECIMAL")
    private float alert;//该指标报警值
    @Column(name = "UNIT",length=100)
    private String unit;
    @Column(name = "DESCRIPTION", length=100)
    private String description; //指标说明

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDrawType() {
        return drawType;
    }

    public void setDrawType(int drawType) {
        this.drawType = drawType;
    }

    public String getFreqTime() {
        return freqTime;
    }

    public void setFreqTime(String freqTime) {
        this.freqTime = freqTime;
    }

    public float getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(float maxValue) {
        this.maxValue = maxValue;
    }

    public float getMinValue() {
        return minValue;
    }

    public void setMinValue(float minValue) {
        this.minValue = minValue;
    }

    public float getWarning() {
        return warning;
    }

    public void setWarning(float warning) {
        this.warning = warning;
    }

    public float getAlert() {
        return alert;
    }

    public void setAlert(float alert) {
        this.alert = alert;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
