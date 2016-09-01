package com.myee.tarot.apiold.domain;

import com.myee.tarot.core.GenericEntity;
import com.myee.tarot.merchant.domain.MerchantStore;

import javax.persistence.*;
import java.util.Date;

//小超人轮播主图
@Entity
@Table(name = "CA_ADVERTISEMENT_PUBLISH")
public class AdvertisementPublish extends GenericEntity<Long, AdvertisementPublish> {

	@Id
	@Column(name = "ADVERTISEMENT_PUBLISH_ID", unique = true, nullable = false)
	@TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "CA_ADVERTISEMENT_PUBLISH_NEXT_VAL",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ADVERTISEMENT_ID")
	private Advertisement advertisement;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TIME_START")
	private Date timeStart;


	@Column(name = "ORDER_SEQ",columnDefinition = "TINYINT")
	private Integer orderSeq;//广告优先级

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TIME_END")
	private Date timeEnd;

	//广告资源要推送到的门店
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STORE_ID")
	private MerchantStore store;

	@Column(name = "ACTIVE", columnDefinition = "BIT")
	private Boolean active = Boolean.TRUE;
	@Column(name = "ADP_CREATED_BY",length = 20)
	private long aDPCreatedBy;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ADP_CREATED")
	private Date aDPCreated;

	public AdvertisementPublish(){

	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public Advertisement getAdvertisement() {
		return advertisement;
	}

	public void setAdvertisement(Advertisement advertisement) {
		this.advertisement = advertisement;
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

	public long getaDPCreatedBy() {
		return aDPCreatedBy;
	}

	public void setaDPCreatedBy(long aDPCreatedBy) {
		this.aDPCreatedBy = aDPCreatedBy;
	}

	public Date getaDPCreated() {
		return aDPCreated;
	}

	public void setaDPCreated(Date aDPCreated) {
		this.aDPCreated = aDPCreated;
	}

	public Integer getOrderSeq() {
		return orderSeq;
	}

	public void setOrderSeq(Integer orderSeq) {
		this.orderSeq = orderSeq;
	}
}