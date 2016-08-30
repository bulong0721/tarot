package com.myee.tarot.apiold.domain;

import com.myee.tarot.apiold.view.EvaluationView;
import com.myee.tarot.catering.domain.Table;
import com.myee.tarot.core.GenericEntity;
import com.myee.tarot.merchant.domain.MerchantStore;

import javax.persistence.*;
import java.util.Date;

@Entity
@javax.persistence.Table(name = "CA_EVALUATION")
public class Evaluation extends GenericEntity<Long, Evaluation> {

	@Id
	@Column(name = "EVALUATION_ID", unique = true, nullable = false)
	@TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "EVALUATION_SEQ_NEXT_VAL",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TABLE_ID")
	private Table table;

	@Column(name = "FEELWHOLE", columnDefinition = "TINYINT")
	private int feelWhole;//整体评价星级，0,20,40,80,100，即0-5分扩大20倍

	@Column(name = "FEELFLAVOR", columnDefinition = "TINYINT")
	private int feelFlavor;//口味星级

	@Column(name = "FEELSERVICE", columnDefinition = "TINYINT")
	private int feelService;//服务星级

	@Column(name = "FEELENVIRONMENT", columnDefinition = "TINYINT")
	private int feelEnvironment;//环境星级

	@Column(name = "MEALSREMARK",length = 400)
	private String mealsRemark;//用餐评价

	@Column(name = "DEVICEREMARK",length = 400)
	private String deviceRemark;//设备评价

	@Column(name = "TIMESECOND")
	private Long timeSecond;//提交时间，格式yyyyMMddHHmmss

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EVALU_CREATED")
	private Date evaluCreated;

	@Transient //不持久化到数据库
	private Long evaluCreatedL;

	@Column(name = "ACTIVE", columnDefinition = "BIT")
	private Boolean active = Boolean.TRUE;

	public Evaluation(){

	}

	public Evaluation(EvaluationView evaluationView){
		this.setDeviceRemark(evaluationView.getDeviceRemark());
		this.setFeelEnvironment(evaluationView.getFeelEnvironment() == null ? 0 : evaluationView.getFeelEnvironment());
		this.setFeelFlavor(evaluationView.getFeelFlavor() == null ? 0 : evaluationView.getFeelFlavor());
		this.setFeelService(evaluationView.getFeelService() == null ? 0 : evaluationView.getFeelService());
		this.setFeelWhole(evaluationView.getFeelWhole() == null ? 0 : evaluationView.getFeelWhole());
		this.setMealsRemark(evaluationView.getMealsRemark());
		this.setTimeSecond(evaluationView.getTimeSecond());
	}

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

	public int getFeelWhole() {
		return feelWhole;
	}

	public void setFeelWhole(int feelWhole) {
		this.feelWhole = feelWhole;
	}

	public int getFeelFlavor() {
		return feelFlavor;
	}

	public void setFeelFlavor(int feelFlavor) {
		this.feelFlavor = feelFlavor;
	}

	public int getFeelService() {
		return feelService;
	}

	public void setFeelService(int feelService) {
		this.feelService = feelService;
	}

	public int getFeelEnvironment() {
		return feelEnvironment;
	}

	public void setFeelEnvironment(int feelEnvironment) {
		this.feelEnvironment = feelEnvironment;
	}

	public String getMealsRemark() {
		return mealsRemark;
	}

	public void setMealsRemark(String mealsRemark) {
		this.mealsRemark = mealsRemark;
	}

	public String getDeviceRemark() {
		return deviceRemark;
	}

	public void setDeviceRemark(String deviceRemark) {
		this.deviceRemark = deviceRemark;
	}

	public Long getTimeSecond() {
		return timeSecond;
	}

	public void setTimeSecond(Long timeSecond) {
		this.timeSecond = timeSecond;
	}

	public Date getEvaluCreated() {
		return evaluCreated;
	}

	public void setEvaluCreated(Date evaluCreated) {
		this.evaluCreated = evaluCreated;
	}

	public Long getEvaluCreatedL() {
		return evaluCreatedL;
	}

	public void setEvaluCreatedL(Long evaluCreatedL) {
		this.evaluCreatedL = evaluCreatedL;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
}