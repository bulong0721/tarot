package com.myee.tarot.apiold.domain;


import com.myee.tarot.catering.domain.*;
import com.myee.tarot.catering.domain.Table;
import com.myee.tarot.core.GenericEntity;

import javax.persistence.*;
import java.util.Date;

@Entity
@javax.persistence.Table(name = "LOG_SMP_UPLOAD_ACCESS",indexes = @Index(name="TABLE_UPLOAD_ACCESS_ID",columnList = "TABLE_ID,UPLOAD_ACCESS_ID",unique = true))//log_smartPen_upload_access
public class UploadAccessLog extends GenericEntity<Long, UploadAccessLog> {

	@Id
	@Column(name = "ID", unique = true, nullable = true)
	@GeneratedValue(strategy = GenerationType.AUTO)//AUTO：主键由程序控制。IDENTITY：主键由数据库自动生成（主要是自动增长型） ； SEQUENCE：根据底层数据库的序列来生成主键，条件是数据库支持序列（类似oracle）；TABLE：使用一个特定的数据库表格来保存主键。
	private Long id;

	@Column(name = "UPLOAD_ACCESS_ID")
	private Long uploadAccessId;//上传记录ID

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TABLE_ID")
	private com.myee.tarot.catering.domain.Table table;

	@Column(name = "ACTION_ID")
	private Long actionId;//功能ID

	@Column(name = "CLICK_NUM")
	private Integer clickNum;//点击次数

	@Column(name = "TIME_POIT")
	private Date timePoit;//点击时间点

	@Column(name = "TIME_STAY")
	private Long timeStay;//浏览停留时长，秒为单位

	@Column(name = "DISCRIPTION",length = 255)
	private String discription;//描述

	@Column(name = "LEVEL_SECOND_ID")
	private Long levelSecondId;//二级功能ID

	@Column(name = "CREATED")
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;

//	private String orgName;//店铺名称

//	private String clientName;//品牌名称

//	private Long tableId;//餐桌ID

//	private String tableName;//桌子名称

//	private String actionName;//一级行为名称

//	private String levelSecondName;//二级行为名称

//	private Long clientId;

//	private Long orgId;



	public UploadAccessLog(){

	}

//	public UploadAccessLog(Long clientId, Long orgId, Long tableId, Long actionId, Integer clickNum, Date timePoit){
//		this.setClientId(clientId);
//		this.setOrgId(orgId);
//		this.setTableId(tableId);
//		this.actionId = actionId;
//		this.clickNum = clickNum;
//		this.timePoit = timePoit;
//		this.setCreatedBy(orgId);
//		this.setCreated(new Date());
//	}


	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public Long getUploadAccessId() {
		return uploadAccessId;
	}

	public void setUploadAccessId(Long uploadAccessId) {
		this.uploadAccessId = uploadAccessId;
	}

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	public Long getActionId() {
		return actionId;
	}

	public void setActionId(Long actionId) {
		this.actionId = actionId;
	}

	public Integer getClickNum() {
		return clickNum;
	}

	public void setClickNum(Integer clickNum) {
		this.clickNum = clickNum;
	}

	public Date getTimePoit() {
		return timePoit;
	}

	public void setTimePoit(Date timePoit) {
		this.timePoit = timePoit;
	}

	public Long getTimeStay() {
		return timeStay;
	}

	public void setTimeStay(Long timeStay) {
		this.timeStay = timeStay;
	}

	public String getDiscription() {
		return discription;
	}

	public void setDiscription(String discription) {
		this.discription = discription;
	}

	public Long getLevelSecondId() {
		return levelSecondId;
	}

	public void setLevelSecondId(Long levelSecondId) {
		this.levelSecondId = levelSecondId;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
}