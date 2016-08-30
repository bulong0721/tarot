package com.myee.tarot.apiold.domain;


import com.myee.tarot.catering.domain.Table;
import com.myee.tarot.core.GenericEntity;

import javax.persistence.*;
import java.util.Date;

@Entity
@javax.persistence.Table(name = "CA_VIDEO")
public class Video extends GenericEntity<Long, Video> {

	@Id
	@Column(name = "VIDEO_ID", unique = true, nullable = false)
	@TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "VIDEO_SEQ_NEXT_VAL",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	@Column(name = "PREVIEW_PATH",length = 255)
	private String previewPath;

	@Column(name = "VIDEO_PATH",length = 255)
	private String videoPath;

	@Column(name = "QINIU_PATH",length = 255)
	private String qiniuPath;

	@Column(name = "PLAY_SECOND", columnDefinition = "INT")
	private Integer playSecond;

	@Column(name = "VIDEO_SIZE", columnDefinition = "Long")
	private Long videoSize;

	@Column(name = "TYPE", columnDefinition = "TINYINT")
	private Integer type;//0：商户，1：商业（默认0）

	@Column(name = "DESCRIPTION",length = 255)
	private String description;

	@Column(name = "ORIGINAL",length = 255)
	private String original;//原文件名

	@Column(name = "ACTIVE", columnDefinition = "BIT")
	private Boolean active = Boolean.TRUE;
	@Column(name = "V_CREATED_BY")
	private long vCreatedBy;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "V_CREATED")
	private Date vCreated;

	public Video(){

	}

//	public Material(Long materialId){
//		this.materialId = materialId;
//	}
//
//	public Material(User user, MaterialView view){
//		if(user != null){
//			this.setOrgId(user.getOrgId());
//			this.setClientId(user.getClientId());
//			this.setCreatedBy(user.getUserId());
//			this.setCreated(new Date());
//			this.setUpdatedBy(user.getUserId());
//			this.setUpdated(new Date());
//		}
//		if(view != null){
//			this.materialId = view.getMaterialId();
//			this.tableId = view.getTableId();
//			this.materialPath = view.getMaterialPath();
//			this.qiniuPath = view.getQiniuPath();
//			this.playSecond = view.getPlaySecond();
//			this.type = view.getType();
//			this.description = view.getDescription();
//			this.previewPath = view.getPreviewPath();
//			this.original = view.getOriginal();
//			this.setActive(view.getActive());
//		}
//	}


	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}


	public String getPreviewPath() {
		return previewPath;
	}

	public void setPreviewPath(String previewPath) {
		this.previewPath = previewPath;
	}

	public String getQiniuPath() {
		return qiniuPath;
	}

	public void setQiniuPath(String qiniuPath) {
		this.qiniuPath = qiniuPath;
	}

	public Integer getPlaySecond() {
		return playSecond;
	}

	public void setPlaySecond(Integer playSecond) {
		this.playSecond = playSecond;
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

	public String getOriginal() {
		return original;
	}

	public void setOriginal(String original) {
		this.original = original;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public long getvCreatedBy() {
		return vCreatedBy;
	}

	public void setvCreatedBy(long vCreatedBy) {
		this.vCreatedBy = vCreatedBy;
	}

	public Date getvCreated() {
		return vCreated;
	}

	public void setvCreated(Date vCreated) {
		this.vCreated = vCreated;
	}

	public String getVideoPath() {
		return videoPath;
	}

	public void setVideoPath(String videoPath) {
		this.videoPath = videoPath;
	}

	public Long getVideoSize() {
		return videoSize;
	}

	public void setVideoSize(Long videoSize) {
		this.videoSize = videoSize;
	}
}