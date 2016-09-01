package com.myee.tarot.apiold.domain;

import com.myee.tarot.core.GenericEntity;
import com.myee.tarot.merchant.domain.MerchantStore;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

//小超人轮播主图
@Entity
@javax.persistence.Table(name = "CA_ROLL_MAIN")
public class RollMain extends GenericEntity<Long, RollMain> {

	@Id
	@Column(name = "ROLL_MAIN_ID", unique = true, nullable = false)
	@TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "CA_ROLL_MAIN_NEXT_VAL",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PIC_ID")
	private Picture mainPic;//轮播主图路径

	@Column(name = "TYPE",columnDefinition = "TINYINT")
	private Integer type;

	@Column(name = "ROLL_TIME",columnDefinition = "TINYINT")
	private Integer rollTime;//轮播时间


	@Column(name = "TITLE",length = 100)
	private String title;

	@Column(name = "DESCRIPTION",length = 255)
	private String description;

	//创建该活动资源的门店
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STORE_ID")
	private MerchantStore store;

	@OneToMany(mappedBy = "rollMain", targetEntity = RollDetail.class, fetch = FetchType.LAZY)
	private List<RollDetail> rollDetailList;

	@Column(name = "IS_APP",columnDefinition = "TINYINT")
	private Integer isApp;//是否为后端访问

	@Column(name = "ACTIVE", columnDefinition = "BIT")
	private Boolean active = Boolean.TRUE;
	@Column(name = "RM_CREATED_BY",length = 20)
	private long rMCreatedBy;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "RM_CREATED")
	private Date rMCreated;
	@Column(name = "RM_UPDATED_BY",length = 20)
	private long rMUpdatedBy;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "RM_UPDATED")
	private Date rMUpdated;


	/**
	 * 本店活动用的参数，本店活动不用推送，所以在上传的时候就设置有效时间和优先级，而木爷活动只有在推送时才能设置这些属性
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TIME_START")
	private Date timeStart;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TIME_END")
	private Date timeEnd;
	@Column(name = "ORDER_SEQ",columnDefinition = "TINYINT")
	private Integer orderSeq;//轮播优先级

	public RollMain(){

	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getRollTime() {
		return rollTime;
	}

	public void setRollTime(Integer rollTime) {
		this.rollTime = rollTime;
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

	public long getrMCreatedBy() {
		return rMCreatedBy;
	}

	public void setrMCreatedBy(long rMCreatedBy) {
		this.rMCreatedBy = rMCreatedBy;
	}

	public Date getrMCreated() {
		return rMCreated;
	}

	public void setrMCreated(Date rMCreated) {
		this.rMCreated = rMCreated;
	}

	public long getrMUpdatedBy() {
		return rMUpdatedBy;
	}

	public void setrMUpdatedBy(long rMUpdatedBy) {
		this.rMUpdatedBy = rMUpdatedBy;
	}

	public Date getrMUpdated() {
		return rMUpdated;
	}

	public void setrMUpdated(Date rMUpdated) {
		this.rMUpdated = rMUpdated;
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

	public List<RollDetail> getRollDetailList() {
		return rollDetailList;
	}

	public void setRollDetailList(List<RollDetail> rollDetailList) {
		this.rollDetailList = rollDetailList;
	}

	public Integer getIsApp() {
		return isApp;
	}

	public void setIsApp(Integer isApp) {
		this.isApp = isApp;
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

	public Integer getOrderSeq() {
		return orderSeq;
	}

	public void setOrderSeq(Integer orderSeq) {
		this.orderSeq = orderSeq;
	}
}