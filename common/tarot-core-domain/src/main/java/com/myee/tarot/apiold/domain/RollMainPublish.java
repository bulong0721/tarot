package com.myee.tarot.apiold.domain;

import com.myee.tarot.core.GenericEntity;
import com.myee.tarot.merchant.domain.MerchantStore;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

//小超人轮播主图
@Entity
@Table(name = "CA_ROLL_MAIN_PUBLISH")
public class RollMainPublish extends GenericEntity<Long, RollMainPublish> {

	@Id
	@Column(name = "ROLL_MAIN_PUBLISH_ID", unique = true, nullable = false)
	@TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "CA_ROLL_MAIN_PUBLISH_NEXT_VAL",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ROLL_MAIN_ID")
	private RollMain rollMain;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TIME_START")
	private Date timeStart;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TIME_END")
	private Date timeEnd;

	//活动资源要推送到的门店
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STORE_ID")
	private MerchantStore store;

	@Column(name = "ORDER_SEQ",columnDefinition = "TINYINT")
	private Integer orderSeq;//轮播优先级

	@Column(name = "ACTIVE", columnDefinition = "BIT")
	private Boolean active = Boolean.TRUE;
	@Column(name = "RMP_CREATED_BY",length = 20)
	private long rMPCreatedBy;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "RMP_CREATED")
	private Date rMPCreated;

	public RollMainPublish(){

	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public RollMain getRollMain() {
		return rollMain;
	}

	public void setRollMain(RollMain rollMain) {
		this.rollMain = rollMain;
	}

	public Date getTimeStart() {
		return timeStart;
	}

	public void setTimeStart(Date timeStart) {
		this.timeStart = timeStart;
	}

	public Date getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd(Date timeEnd) {
		this.timeEnd = timeEnd;
	}

	public MerchantStore getStore() {
		return store;
	}

	public void setStore(MerchantStore store) {
		this.store = store;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public long getrMPCreatedBy() {
		return rMPCreatedBy;
	}

	public void setrMPCreatedBy(long rMPCreatedBy) {
		this.rMPCreatedBy = rMPCreatedBy;
	}

	public Date getrMPCreated() {
		return rMPCreated;
	}

	public void setrMPCreated(Date rMPCreated) {
		this.rMPCreated = rMPCreated;
	}

	public Integer getOrderSeq() {
		return orderSeq;
	}

	public void setOrderSeq(Integer orderSeq) {
		this.orderSeq = orderSeq;
	}
}