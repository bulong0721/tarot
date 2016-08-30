package com.myee.tarot.apiold.domain;

import com.myee.tarot.catering.domain.Table;
import com.myee.tarot.core.GenericEntity;
import com.myee.tarot.merchant.domain.MerchantStore;

import javax.persistence.*;
import java.util.Date;

@Entity
@javax.persistence.Table(name = "CA_MATERIAL_PUBLISH")
public class MaterialPublish extends GenericEntity<Long, MaterialPublish> {

	@Id
	@Column(name = "MATERIAL_PUBLISH_ID", unique = true, nullable = false)
	@TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "MATERIAL_PUBLISH_SEQ_NEXT_VAL",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MATERIAL_BUSINESS_ID")
	private MaterialBusiness materialBusiness;

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
	private int active;
	@Column(name = "CREATED_BY")
	private long createdBy;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED")
	private Date created;

	public MaterialPublish(){

	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public MaterialBusiness getMaterialBusiness() {
		return materialBusiness;
	}

	public void setMaterialBusiness(MaterialBusiness materialBusiness) {
		this.materialBusiness = materialBusiness;
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

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(long createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
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