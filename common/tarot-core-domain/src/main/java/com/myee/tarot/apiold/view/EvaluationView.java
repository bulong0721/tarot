package com.myee.tarot.apiold.view;


import com.myee.tarot.apiold.bean.BaseBean;

/**
 * Info: 商圈信息
 * User: zhangxinglong@rui10.com
 * Date: 15/5/18
 * Time: 14:54
 * Version: 1.0
 * History: <p>如果有修改过程，请记录</P>
 */
public class EvaluationView extends BaseBean {

    private Long tableId;

    private Integer feelWhole;//整体评分，0,20,40,80,100，即0-5分扩大20倍

    private Integer feelFlavor;

    private Integer feelService;

    private Integer feelEnvironment;

    private String mealsRemark;

    private String deviceRemark;

    private Long timeSecond;

    private String orgName;//店铺名称

    private String clientName;//品牌名称

    private String tableName;//桌子名称

    public EvaluationView(){

    }

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public Integer getFeelWhole() {
        return feelWhole;
    }

    public void setFeelWhole(Integer feelWhole) {
        this.feelWhole = feelWhole;
    }

    public Integer getFeelFlavor() {
        return feelFlavor;
    }

    public void setFeelFlavor(Integer feelFlavor) {
        this.feelFlavor = feelFlavor;
    }

    public Integer getFeelService() {
        return feelService;
    }

    public void setFeelService(Integer feelService) {
        this.feelService = feelService;
    }

    public Integer getFeelEnvironment() {
        return feelEnvironment;
    }

    public void setFeelEnvironment(Integer feelEnvironment) {
        this.feelEnvironment = feelEnvironment;
    }

    public String getMealsRemark() {
        return mealsRemark;
    }

    public void setMealsRemark(String mealsRemark) {
        this.mealsRemark = mealsRemark;
    }

    public String getDeviceRemark() {
        return deviceRemark;
    }

    public void setDeviceRemark(String deviceRemark) {
        this.deviceRemark = deviceRemark;
    }

    public Long getTimeSecond() {
        return timeSecond;
    }

    public void setTimeSecond(Long timeSecond) {
        this.timeSecond = timeSecond;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
