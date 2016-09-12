package com.myee.tarot.apiold.domain;

import com.myee.tarot.catering.domain.Table;
import com.myee.tarot.core.GenericEntity;
import com.myee.tarot.merchant.domain.MerchantStore;

import javax.persistence.*;
import java.util.Date;

@Entity
@javax.persistence.Table(name = "CA_PICTURE")
public class Picture extends GenericEntity<Long, Picture> {

	@Id
	@Column(name = "PIC_ID", unique = true, nullable = false)
	@TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "CA_PICTURE_SEQ_NEXT_VAL",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	//预览路径
	@Column(name = "PREVIEW_PATH",length = 255)
	private String previewPath;

	//本地路径
	@Column(name = "PIC_PATH",length = 255)
	private String picPath;

	//七牛路径
	@Column(name = "PIC_QINIU_PATH",length = 255)
	private String picQiniuPath;

	//图片创建的年月天(作为文件夹分类用)
	@Column(name = "YEAR",columnDefinition = "TINYINT")
	private Integer year;
	//图片创建的年月天(作为文件夹分类用)
	@Column(name = "MONTH")
	private Integer month;
	//图片创建的年月天(作为文件夹分类用)
	@Column(name = "DAY")
	private Integer day;
	//图片菜品种类（未定义）(作为文件夹分类用),0：未分类，1：优惠专区，2：广告图片，3：菜品图片,4:点点笔截图...
	@Column(name = "KIND",columnDefinition = "TINYINT")
	private Integer kind;
	//0：商户，1：商业（默认0）
	@Column(name = "TYPE",columnDefinition = "TINYINT")
	private Integer type;

	//原文件名
	@Column(name = "ORIGINAL",length = 255)
	private String original;

	@Column(name = "ACTIVE", columnDefinition = "BIT")
	private Boolean active = Boolean.TRUE;
	@Column(name = "V_CREATED_BY")
	private long vCreatedBy;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "V_CREATED")
	private Date vCreated;

	//本店图片用，管理员查询的时候不受此字段限制
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STORE_ID")
	private MerchantStore store;

	public Picture(){

	}

	public MerchantStore getStore() {
		return store;
	}

	public void setStore(MerchantStore store) {
		this.store = store;
	}

	public String getPreviewPath() {
		return previewPath;
	}

	public void setPreviewPath(String previewPath) {
		this.previewPath = previewPath;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	public String getPicQiniuPath() {
		return picQiniuPath;
	}

	public void setPicQiniuPath(String picQiniuPath) {
		this.picQiniuPath = picQiniuPath;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public Integer getKind() {
		return kind;
	}

	public void setKind(Integer kind) {
		this.kind = kind;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
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
}