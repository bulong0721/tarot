package com.myee.tarot.apiold.domain;


import com.myee.tarot.catalog.domain.DeviceUsed;
import com.myee.tarot.catering.domain.Table;
import com.myee.tarot.core.GenericEntity;
import com.myee.tarot.merchant.domain.MerchantStore;

import javax.persistence.*;

//对应老系统的r_info_phone表
@Entity
@javax.persistence.Table(name = "CA_TABLE_PHONE")
public class TablePhone extends GenericEntity<Long, TablePhone> {

	@Id
	@Column(name = "TABLE_PHONE_ID", unique = true, nullable = false)
	@TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "TABLE_PHONE_SEQ_NEXT_VAL",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TABLE_ID")
	private Table table;

	@Column(name = "PHONE")
	private String phone;

	@Column(name = "PHONE1")
	private String phone1;

	@Column(name = "PHONE2")
	private String phone2;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STORE_ID")
	private MerchantStore store;

	public TablePhone(){

	}

	public TablePhone(Long id){
		this.setId(id);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPhone1() {
		return phone1;
	}

	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}

	public String getPhone2() {
		return phone2;
	}

	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	public MerchantStore getStore() {
		return store;
	}

	public void setStore(MerchantStore store) {
		this.store = store;
	}
}