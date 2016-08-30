package com.myee.tarot.apiold.domain;

import com.myee.tarot.core.GenericEntity;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;

@Entity
@javax.persistence.Table(name = "ca_smp_upload_action")//点点笔点击行为，支持多级级联
public class UploadAccessAction extends GenericEntity<Long, UploadAccessAction> {
	@Id
	private Long id;

	@NotEmpty
	@Column(name = "NAME",length = 200)
	private String name;

	@Column(name = "LEVEL",length = 10)
	private Integer level;//点击行为级别，1级，2级等

	@Column(name = "PARENT")
	private Long parent;//如果是2级以上点击行为，则应该有关联的父级行为ID

	@Column(name = "ACTIVE", columnDefinition = "BIT")
	private Boolean active = Boolean.TRUE;

	public UploadAccessAction(){

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Long getParent() {
		return parent;
	}

	public void setParent(Long parent) {
		this.parent = parent;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
}