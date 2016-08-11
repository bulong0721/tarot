package com.myee.tarot.apiold.domain;

import com.myee.tarot.core.GenericEntity;
import com.myee.tarot.merchant.domain.MerchantStore;

import javax.persistence.*;

@Entity
@Table(name = "CA_AK_SK")
public class AkSk extends GenericEntity<Long, AkSk> {

	@Id
	@Column(name = "AK_SK_ID", unique = true, nullable = false)
	@TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "AK_SK_SEQ_NEXT_VAL",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	@Column(name = "ACCESS_KEY")
	private String accessKey;

	@Column(name = "SECRET_KEY")
	private String secretKey;

	@Column(name = "COMPANY_NAME")
	private String companyName;

	@Column(name = "COMPANY_IP")
	private String companyIp;

	@Column(name = "ACTIVE")
	private int active;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STORE_ID")
	private MerchantStore store;

	public AkSk(){

	}

	public AkSk(String accessKey){
		this.accessKey = accessKey;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyIp() {
		return companyIp;
	}

	public void setCompanyIp(String companyIp) {
		this.companyIp = companyIp;
	}

	public MerchantStore getStore() {
		return store;
	}

	public void setStore(MerchantStore store) {
		this.store = store;
	}
}