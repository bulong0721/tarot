package com.myee.tarot.apiold.domain;

import com.myee.tarot.catering.domain.*;
import com.myee.tarot.catering.domain.Table;
import com.myee.tarot.core.GenericEntity;
import com.myee.tarot.merchant.domain.MerchantStore;

import javax.persistence.*;
import java.util.Date;

@Entity
@javax.persistence.Table(name = "CA_MATERIAL_BUSINESS")
public class MaterialBusiness extends GenericEntity<Long, MaterialBusiness> {

	@Id
	@Column(name = "MATERIAL_BUSINESS_ID", unique = true, nullable = false)
	@TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "MATERIAL_BUSINESS_SEQ_NEXT_VAL",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MATERIAL_ID")
	private Material material;

	@Column(name = "KIND", columnDefinition = "TINYINT")
	private Integer kind;//素材种类,0:未定义;1:apk升级素材,100:升级固件

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TIME_START")
	private Date timeStart; //本店素材用

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TIME_END")
	private Date timeEnd; //本店素材用

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STORE_ID")
	private MerchantStore store;//本店素材用

	@Column(name = "TYPE",columnDefinition = "TINYINT")
	private Integer type;//0：商户，1：商业（默认0）

	@Column(name = "DESCRIPTION",length = 200)
	private String description;

	@Column(name = "PACKAGE_NAME",length = 200)
	private String packageName;//包名

	@Column(name = "VERSION",length = 100)
	private String version;//版本号

	@Column(name = "ACTIVE", columnDefinition = "BIT")
	private Boolean active = Boolean.TRUE;
	@Column(name = "MB_CREATED_BY")
	private long mBCreatedBy;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MB_CREATED")
	private Date mBCreated;
	@Column(name = "MB_UPDATED_BY")
	private long mBUpdatedBy;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MB_UPDATED")
	private Date mBUpdated;

	public MaterialBusiness(){

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

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
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

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public long getmBCreatedBy() {
		return mBCreatedBy;
	}

	public void setmBCreatedBy(long mBCreatedBy) {
		this.mBCreatedBy = mBCreatedBy;
	}

	public Date getmBCreated() {
		return mBCreated;
	}

	public void setmBCreated(Date mBCreated) {
		this.mBCreated = mBCreated;
	}

	public long getmBUpdatedBy() {
		return mBUpdatedBy;
	}

	public void setmBUpdatedBy(long mBUpdatedBy) {
		this.mBUpdatedBy = mBUpdatedBy;
	}

	public Date getmBUpdated() {
		return mBUpdated;
	}

	public void setmBUpdated(Date mBUpdated) {
		this.mBUpdated = mBUpdated;
	}

	public MerchantStore getStore() {
		return store;
	}

	public void setStore(MerchantStore store) {
		this.store = store;
	}
}