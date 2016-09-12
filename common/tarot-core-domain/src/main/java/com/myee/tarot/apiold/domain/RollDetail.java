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

	@Column(name = "TITLE",length = 255)
	private String title;

	@Column(name = "DESCRIPTION",length = 255)
	private String description;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED")
	private Date created;

	@Transient //不持久化到数据库
	private Long createdL;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ROLL_MAIN_ID")
	private RollMain rollMain;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PIC_ID")
	private Picture pic;

	@Column(name = "ORDER_SEQ",columnDefinition = "TINYINT")
	private Integer orderSeq;//轮播优先级

	@Column(name = "ROLL_TIME",columnDefinition = "TINYINT")
	private Integer rollTime;//轮播时间

	public RollDetail(){

	}

	public Integer getRollTime() {
		return rollTime;
	}

	public void setRollTime(Integer rollTime) {
		this.rollTime = rollTime;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getOrderSeq() {
		return orderSeq;
	}

	public void setOrderSeq(Integer orderSeq) {
		this.orderSeq = orderSeq;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public RollMain getRollMain() {
		return rollMain;
	}

	public void setRollMain(RollMain rollMain) {
		this.rollMain = rollMain;
	}
}