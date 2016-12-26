package com.myee.tarot.common.domain;

import com.myee.tarot.core.GenericEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * Project Name : tarot
 * User: Jelynn
 * Date: 2016/12/16
 * Time: 16:41
 * Describe: 登录日志实体类
 * Version:1.0
 */

@Entity
@Table(name = "C_LOGIN_LOG")
public class LoginLog extends GenericEntity<Long, LoginLog> {

	@Id
	@Column(name = "LOGIN_LOG_ID", unique = true, nullable = false)
	@TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "LOGIN_LOG_SEQ_NEXT_VAL", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	@Column(name = "USER_ID", nullable = false)
	protected long userId;

	@Column(name = "TYPE", nullable = false)
	protected int type;   //	1:admin   2:customer

	@Column(name = "LOGIN_IP")
	protected String loginIP;

	@Column(name = "LOGIN_ADDRESS")
	protected String loginAddress;

	@Column(name = "LOGIN_TIME")
	protected Date loginTime;

	@Column(name = "REMARK")
	protected String remark;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getLoginIP() {
		return loginIP;
	}

	public void setLoginIP(String loginIP) {
		this.loginIP = loginIP;
	}

	public String getLoginAddress() {
		return loginAddress;
	}

	public void setLoginAddress(String loginAddress) {
		this.loginAddress = loginAddress;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
