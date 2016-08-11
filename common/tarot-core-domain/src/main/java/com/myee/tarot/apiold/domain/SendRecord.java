package com.myee.tarot.apiold.domain;

import com.myee.tarot.catering.domain.Table;
import com.myee.tarot.core.GenericEntity;
import com.myee.tarot.merchant.domain.MerchantStore;

import javax.persistence.*;
import java.util.Date;

@Entity
@javax.persistence.Table(name = "CA_SEND_RECORD")
public class SendRecord extends GenericEntity<Long, SendRecord> {

	@Id
	@Column(name = "RECORD_ID", unique = true, nullable = false)
	@TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "RECORD_SEQ_NEXT_VAL",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TABLE_ID")
	private Table table;

	@Column(name = "FLAG")
	private Integer flag;

	@Column(name = "TEMPLATENUM")
	private String templateNum;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "PARTNER")
	private Integer partner;

	@Column(name = "CREATED")
	private Date created;

	private Long createdL;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STORE_ID")
	private MerchantStore store;

	public SendRecord(){

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public String getTemplateNum() {
		return templateNum;
	}

	public void setTemplateNum(String templateNum) {
		this.templateNum = templateNum;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getPartner() {
		return partner;
	}

	public void setPartner(Integer partner) {
		this.partner = partner;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Long getCreatedL() {
		return createdL;
	}

	public void setCreatedL(Long createdL) {
		this.createdL = createdL;
	}

	public MerchantStore getStore() {
		return store;
	}

	public void setStore(MerchantStore store) {
		this.store = store;
	}
}