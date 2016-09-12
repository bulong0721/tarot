package com.myee.tarot.clientprize.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.myee.tarot.core.GenericEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Administrator on 2016/9/9.
 */
@Entity
@Table(name = "C_CLIENT_PRIZE_TICKET")
public class ClientPrizeTicket extends GenericEntity<Long, ClientPrizeTicket> {

    @Id
    @Column(name = "TICKET_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "CLIENT_PRIZE_TICKET_SEQ_NEXT_VAL",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    protected Long id;

    @Column(name = "ticketCode")
    private String ticketCode;

    @ManyToOne(targetEntity = ClientPrize.class, optional = false)
    @JoinColumn(name = "PRIZE_ID")
    @JSONField(serialize = false)
    private ClientPrize prize; //绑定奖券信息

    @Column(name = "startDate")
    private Date startDate;

    @Column(name = "endDate")
    private Date endDate;

    @Column(name = "status")
    private Integer status; //是否已发 1为未发 0为已发 2为过期

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTicketCode() {
        return ticketCode;
    }

    public void setTicketCode(String ticketCode) {
        this.ticketCode = ticketCode;
    }

    public ClientPrize getPrize() {
        return prize;
    }

    public void setPrize(ClientPrize prize) {
        this.prize = prize;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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
