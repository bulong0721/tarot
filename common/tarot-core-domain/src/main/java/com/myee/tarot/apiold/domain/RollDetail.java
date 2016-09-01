package com.myee.tarot.apiold.domain;

import com.myee.tarot.catering.domain.Table;
import com.myee.tarot.core.GenericEntity;
import com.myee.tarot.merchant.domain.MerchantStore;

import javax.persistence.*;
import java.util.Date;

@Entity
@javax.persistence.Table(name = "CA_ROLL_DETAIL")
public class RollDetail extends GenericEntity<Long, RollDetail> {

	@Id
	@Column(name = "ROLL_DETAIL_ID", unique = true, nullable = false)
	@TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "CA_ROLL_DETAIL_SEQ_NEXT_VAL",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TABLE_ID")
	private Table table;

	@Column(name = "FLAG",columnDefinition = "TINYINT")
	private Integer flag;

	@Column(name = "TEMPLATENUM",length = 20)
	private String templateNum;

	@Column(name = "DESCRIPTION",length = 255)
	private String description;

	@Column(name = "PARTNER")
	private Integer partner;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED")
	private Date created;

	@Transient //不持久化到数据库
	private Long createdL;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STORE_ID")
	private MerchantStore store;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ROLL_MAIN_ID")
	private RollMain rollMain;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PIC_ID")
	private Picture pic;

	public RollDetail(){

	}

	public Picture getPic() {
		return pic;
	}

	public void setPic(Picture pic) {
		this.pic = pic;
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

	public RollMain getRollMain() {
		return rollMain;
	}

	public void setRollMain(RollMain rollMain) {
		this.rollMain = rollMain;
	}
}