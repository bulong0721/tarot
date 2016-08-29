package com.myee.tarot.apiold.domain;

import com.myee.tarot.core.GenericEntity;
import com.myee.tarot.merchant.domain.MerchantStore;

import javax.persistence.*;
import java.util.Date;

@Entity
@javax.persistence.Table(name = "CA_VIDEO_BUSINESS")
public class VideoBusiness extends GenericEntity<Long, VideoBusiness> {

	@Id
	@Column(name = "VIDEO_BUSINESS_ID", unique = true, nullable = false)
	@TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "VIDEO_BUSINESS_SEQ_NEXT_VAL",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "VIDEO_ID")
	private Video video;

	@Column(name = "KIND", columnDefinition = "INT",length = 2)
	private Integer kind;//视频种类,1:待机广告视频

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TIME_START")
	private Date timeStart; //本店视频用

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TIME_END")
	private Date timeEnd; //本店视频用

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STORE_ID")
	private MerchantStore store;//本店视频用

	@Column(name = "TYPE",length = 2)
	private Integer type;//0：商户，1：商业（默认0）

	@Column(name = "DESCRIPTION",length = 255)
	private String description;

	@Column(name = "ACTIVE", columnDefinition = "INT",length = 1)
	private int active;
	@Column(name = "CREATED_BY",length = 20)
	private long createdBy;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED")
	private Date created;
	@Column(name = "UPDATED_BY",length = 20)
	private long updatedBy;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED")
	private Date updated;

	public VideoBusiness(){

	}

//	public MaterialBusiness(Long materialId){
//		this.materialId = materialId;
//	}
//
//	public MaterialBusiness(User user, MaterialView view){
//		if(user != null){
//			this.setOrgId(user.getOrgId());
//			this.setClientId(user.getClientId());
//			this.setCreatedBy(user.getUserId());
//			this.setCreated(new Date());
//			this.setUpdatedBy(user.getUserId());
//			this.setUpdated(new Date());
//		}
//		if(view != null){
//			this.materialBusinessId = view.getMaterialBusinessId();
//			this.materialId = view.getMaterialId();
//			this.tableId = view.getTableId();
//			this.kind = view.getKind();
//			this.type = view.getType();
//			this.timeEnd = DateTime.toMillis(view.getTimeEnd());
//			this.timeStart = DateTime.toMillis(view.getTimeStart());
//			this.description = view.getDescription();
//			this.packageName = view.getPackageName();
//			this.version = view.getVersion();
//			this.setActive(view.getActive());
//		}
//	}
//
//	public MaterialBusiness(Long materialBusinessId, Long orgId, Integer type){
//		this.setOrgId(orgId);
//		this.type = type;
//		this.materialBusinessId = materialBusinessId;
//	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public Video getVideo() {
		return video;
	}

	public void setVideo(Video video) {
		this.video = video;
	}

	public Integer getKind() {
		return kind;
	}

	public void setKind(Integer kind) {
		this.kind = kind;
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(long updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public MerchantStore getStore() {
		return store;
	}

	public void setStore(MerchantStore store) {
		this.store = store;
	}
}