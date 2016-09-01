package com.myee.tarot.apiold.domain;

import com.myee.tarot.core.GenericEntity;
import com.myee.tarot.merchant.domain.MerchantStore;

import javax.persistence.*;
import java.util.Date;

@Entity
@javax.persistence.Table(name = "CA_VIDEO_PUBLISH")
public class VideoPublish extends GenericEntity<Long, VideoPublish> {

	@Id
	@Column(name = "VIDEO_PUBLISH_ID", unique = true, nullable = false)
	@TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "VIDEO_PUBLISH_SEQ_NEXT_VAL",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "VIDEO_BUSINESS_ID")
	private VideoBusiness videoBusiness;

	//推送该视频到哪个门店
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STORE_ID")
	private MerchantStore store;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TIME_START")
	private Date timeStart;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TIME_END")
	private Date timeEnd;

	@Column(name = "DESCRIPTION",length = 255)
	private String description;

	@Column(name = "ACTIVE", columnDefinition = "BIT")
	private Boolean active = Boolean.TRUE;
	@Column(name = "VP_CREATED_BY")
	private long vPCreatedBy;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "VP_CREATED")
	private Date vPCreated;

	public VideoPublish(){

	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public VideoBusiness getVideoBusiness() {
		return videoBusiness;
	}

	public void setVideoBusiness(VideoBusiness videoBusiness) {
		this.videoBusiness = videoBusiness;
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

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public long getvPCreatedBy() {
		return vPCreatedBy;
	}

	public void setvPCreatedBy(long vPCreatedBy) {
		this.vPCreatedBy = vPCreatedBy;
	}

	public Date getvPCreated() {
		return vPCreated;
	}

	public void setvPCreated(Date vPCreated) {
		this.vPCreated = vPCreated;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public MerchantStore getStore() {
		return store;
	}

	public void setStore(MerchantStore store) {
		this.store = store;
	}
}