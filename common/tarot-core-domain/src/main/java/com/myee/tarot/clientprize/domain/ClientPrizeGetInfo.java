package com.myee.tarot.clientprize.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.myee.tarot.campaign.domain.MerchantPrice;
import com.myee.tarot.core.GenericEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Administrator on 2016/8/29.
 */
@Entity
@Table(name = "C_CLIENT_PRIZE_GETINFO")
public class ClientPrizeGetInfo extends GenericEntity<Long, ClientPrizeGetInfo>{

    @Id
    @Column(name = "PRIZE_GET_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "CLIENT_PRIZE_GET_SEQ_NEXT_VAL",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    protected Long id;

    @Column(name = "PHONE_NUM")
    private Long phoneNum; //手机号码 作为辨识

    @Column(name = "DESK_ID")
    private String deskId;  //桌号

    @ManyToOne(targetEntity = ClientPrize.class, optional = false, cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "PRIZE_ID")
    private ClientPrize price;  //获取对应的奖项

    @Column(name = "GET_DATE")
    private Date getDate; //获取时间

    @Column(name = "CHECK_DATE")
    private Date checkDate; //检测时间

    @Column(name = "CHECK_CODE")
    private String checkCode; //兑奖验证码

    @Column(name = "STATUS")
    private Integer status; // 0为待领取  1为领取  2为已兑奖  3为已过期

    //快照存储
    @Column(name= "PRIZE_NAME")
    private String prizeName; //奖券名称

    @Column(name= "PRIZE_DESCRIPTION")
    private String prizeDescription; //奖券描述

    @Column(name= "PRIZE_TYPE")
    private Integer prizeType; //类型   0为手机 1为二维码

    @Column(name= "PRIZE_START_DATE")
    @JSONField(format = "yyyy-MM-dd")
    private Date prizeStartDate; //奖券有效开始时间

    @Column(name= "PRIZE_END_DATE")
    @JSONField(format = "yyyy-MM-dd")
    private Date prizeEndDate; //奖券有效结束日期

    public Date getGetDate() {
        return getDate;
    }

    public void setGetDate(Date getDate) {
        this.getDate = getDate;
    }

    public Date getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
    }

    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(Long phoneNum) {
        this.phoneNum = phoneNum;
    }

    public ClientPrize getPrice() {
        return price;
    }

    public void setPrice(ClientPrize price) {
        this.price = price;
    }

    public String getDeskId() {
        return deskId;
    }

    public void setDeskId(String deskId) {
        this.deskId = deskId;
    }

    public String getPrizeName() {
        return prizeName;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }

    public String getPrizeDescription() {
        return prizeDescription;
    }

    public void setPrizeDescription(String prizeDescription) {
        this.prizeDescription = prizeDescription;
    }

    public Integer getPrizeType() {
        return prizeType;
    }

    public void setPrizeType(Integer prizeType) {
        this.prizeType = prizeType;
    }

    public Date getPrizeStartDate() {
        return prizeStartDate;
    }

    public void setPrizeStartDate(Date prizeStartDate) {
        this.prizeStartDate = prizeStartDate;
    }

    public Date getPrizeEndDate() {
        return prizeEndDate;
    }

    public void setPrizeEndDate(Date prizeEndDate) {
        this.prizeEndDate = prizeEndDate;
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
}
