package com.myee.tarot.apiold.domain;

import com.myee.tarot.core.GenericEntity;
import com.myee.tarot.merchant.domain.MerchantStore;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

//小超人轮播主图
@Entity
@Table(name = "CA_ADVERTISEMENT")
public class Advertisement extends GenericEntity<Long, Advertisement> {

	@Id
	@Column(name = "ADVERTISEMENT_ID", unique = true, nullable = false)
	@TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "CA_ADVERTISEMENT_NEXT_VAL",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PIC_ID")
	private Picture mainPic;//广告主图路径

	@Column(name = "TITLE",length = 100)
	private String title;

	@Column(name = "DESCRIPTION",length = 255)
	private String description;

	//创建该广告资源的门店
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STORE_ID")
	private MerchantStore store;

	@Column(name = "ACTIVE", columnDefinition = "BIT")
	private Boolean active = Boolean.TRUE;
	@Column(name = "AD_CREATED_BY",length = 20)
	private long aDCreatedBy;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "AD_CREATED")
	private Date aDCreated;
	@Column(name = "AD_UPDATED_BY",length = 20)
	private long aDUpdatedBy;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "AD_UPDATED")
	private Date aDUpdated;

	public Advertisement(){

	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public Picture getMainPic() {
		return mainPic;
	}

	public void setMainPic(Picture mainPic) {
		this.mainPic = mainPic;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public long getaDCreatedBy() {
		return aDCreatedBy;
	}

	public void setaDCreatedBy(long aDCreatedBy) {
		this.aDCreatedBy = aDCreatedBy;
	}

	public Date getaDCreated() {
		return aDCreated;
	}

	public void setaDCreated(Date aDCreated) {
		this.aDCreated = aDCreated;
	}

	public long getaDUpdatedBy() {
		return aDUpdatedBy;
	}

	public void setaDUpdatedBy(long aDUpdatedBy) {
		this.aDUpdatedBy = aDUpdatedBy;
	}

	public Date getaDUpdated() {
		return aDUpdated;
	}

	public void setaDUpdated(Date aDUpdated) {
		this.aDUpdated = aDUpdated;
	}
}