package com.myee.tarot.metric.domain;

import com.myee.tarot.core.GenericEntity;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by Chay on 2016/10/13.
 */

@Entity
@Table(name = "M_MATRIC_DETAIL")
@DynamicUpdate //hibernate部分更新
public class MetricDetail extends GenericEntity<Long, MetricDetail> {

    @Id
    @Column(name = "METRIC_DETAIL_ID", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "KEY_NAME", unique = true,length=100)
    private String keyName; //指标键名称
    /**
     * 详细指标关联的总览指标名称，比如cpuUsed关联cpuTotal
     */
    @Column(name = "RELATE_KEY_NAME",length=100)
    private String relateKeyName;
    @Column(name = "NAME", length=100)
    private String name; //指标显示名称
    /**
     * 画图类型，0:数值，1:饼图，2:直方图,3:折线图.
     * 可画多种图的，以逗号分隔，第一个作为默认
     * 比如： 3,2,0,1
     */
    @Column(name = "DRAW_TYPE",columnDefinition = "TINYINT")
    private int drawType;
    @Column(name = "FREQ_TIME", length=100)
    private String freqTime;//采集间隔时间
    @Column(name = "MAX_VALUE", precision = 20,scale = 2)
    private BigDecimal maxValue; //该指标最大值,有的参数可能会从静态参数中读取，比如CPU最大值，内存最大值
    @Column(name = "MIN_VALUE", precision = 20,scale = 2)
    private BigDecimal minValue; //该指标最小值
    @Column(name = "WARNING", precision = 20,scale = 2)
    private BigDecimal warning;//该指标警告值
    @Column(name = "ALERT", precision = 20,scale = 2)
    private BigDecimal alert;//该指标报警值
    @Column(name = "UNIT",length=100)
    private String unit;
    @Column(name = "DESCRIPTION", length=100)
    private String description; //指标说明
    @Column(name = "VALUE_TYPE",columnDefinition = "TINYINT")
    private int valueType;//指标值类型，0:数值（数值型统一用double去处理），1:字符串，2:...
    /**
     * 指标值报警公式，
     * 规定：x是指标值,a是警告/报警值，b是关联的指标名称，
     * 比如大于0.5倍的a报警，公式就是x>0.5*a；比如小于1倍的a报警，就是x<a;...
     * 比如大于0.4倍的b报警，公式就是x>0.4*b;
     * 比如大于a倍的b报警，公式就是x>a*b;
     */
    @Column(name = "ALERT_REGULAR",length=100)
    private String alertRegular;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
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

    public int getValueType() {
        return valueType;
    }

    public void setValueType(int valueType) {
        this.valueType = valueType;
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

    public String getRelateKeyName() {
        return relateKeyName;
    }

    public void setRelateKeyName(String relateKeyName) {
        this.relateKeyName = relateKeyName;
    }

    public String getAlertRegular() {
        return alertRegular;
    }

    public void setAlertRegular(String alertRegular) {
        this.alertRegular = alertRegular;
    }

    public BigDecimal getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(BigDecimal maxValue) {
        this.maxValue = maxValue;
    }

    public BigDecimal getMinValue() {
        return minValue;
    }

    public void setMinValue(BigDecimal minValue) {
        this.minValue = minValue;
    }

    public BigDecimal getWarning() {
        return warning;
    }

    public void setWarning(BigDecimal warning) {
        this.warning = warning;
    }

    public BigDecimal getAlert() {
        return alert;
    }

    public void setAlert(BigDecimal alert) {
        this.alert = alert;
    }
}
