package com.myee.tarot.apiold.domain;


import com.myee.tarot.catering.domain.*;
import com.myee.tarot.catering.domain.Table;
import com.myee.tarot.core.GenericEntity;

import javax.persistence.*;
import java.util.Date;

@Entity
@javax.persistence.Table(name = "CA_MATERIAL")
public class Material extends GenericEntity<Long, Material> {

	@Id
	@Column(name = "MATERIAL_ID", unique = true, nullable = false)
	@TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "MATERIAL_SEQ_NEXT_VAL",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TABLE_ID")
	private Table table;

	@Column(name = "PREVIEW_PATH")
	private String previewPath;

	@Column(name = "MATERIAL_PATH")
	private String materialPath;

	@Column(name = "QINIU_PATH")
	private String qiniuPath;

	@Column(name = "PLAY_SECOND", columnDefinition = "INT")
	private Integer playSecond;

	@Column(name = "MATERIAL_SIZE", columnDefinition = "Long")
	private Long materialSize;

	@Column(name = "TYPE", columnDefinition = "INT")
	private Integer type;//0：商户，1：商业（默认0）

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MATERIAL_FILE_KIND_ID")
	private MaterialFileKind materialFileKind; //文件类型,0:未定义;1:apk;2:txt/plaintext;3:audio;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "ORIGINAL")
	private String original;//原文件名

	@Column(name = "ACTIVE", columnDefinition = "INT",length = 1)
	private int active;
	@Column(name = "CREATED_BY")
	private long createdBy;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED")
	private Date created;
	@Column(name = "UPDATED_BY")
	private long updatedBy;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED")
	private Date updated;

	public Material(){

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

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	public String getPreviewPath() {
		return previewPath;
	}

	public void setPreviewPath(String previewPath) {
		this.previewPath = previewPath;
	}

	public String getMaterialPath() {
		return materialPath;
	}

	public void setMaterialPath(String materialPath) {
		this.materialPath = materialPath;
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

	public Long getMaterialSize() {
		return materialSize;
	}

	public void setMaterialSize(Long materialSize) {
		this.materialSize = materialSize;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public MaterialFileKind getMaterialFileKind() {
		return materialFileKind;
	}

	public void setMaterialFileKind(MaterialFileKind materialFileKind) {
		this.materialFileKind = materialFileKind;
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
}