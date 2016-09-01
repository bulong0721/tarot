package com.myee.tarot.apiold.domain;

import com.myee.tarot.core.GenericEntity;
import com.myee.tarot.merchant.domain.MerchantStore;

import javax.persistence.*;
import java.util.Date;

/**
 * Info: clever
 * User: Gary.zhang@clever-m.com
 * Date: 2016-03-30
 * Time: 14:46
 * Version: 1.0
 * History: <p>如果有修改过程，请记录</P>
 */
@Entity
@javax.persistence.Table(name = "CA_VERSION")
public class VersionInfo extends GenericEntity<Long, VersionInfo> {

    @Id
    @Column(name = "AUTO_ID", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)//AUTO：主键由程序控制。IDENTITY：主键由数据库自动生成（主要是自动增长型） ； SEQUENCE：根据底层数据库的序列来生成主键，条件是数据库支持序列（类似oracle）；TABLE：使用一个特定的数据库表格来保存主键。
    private Long id;

    @Column(name = "NAME")
    private String name;         //版本名称

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STORE_ID")
    private MerchantStore store;

    @Column(name = "VERSION",length = 15)
    private String version;      //版本号
    @Column(name = "INFO",length = 100)
    private String info;         //版本升级显示信息
    @Column(name = "URL",length = 255)
    private String url;          //版本升级url
    @Column(name = "DESCRIPTION",length = 255)
    private String description;  //版本升级备注
    @Column(name = "UPDATED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;
    @Column(name = "ACTIVE",columnDefinition = "BIT")
    private Boolean active = Boolean.TRUE;//状态1生效0不生效

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MerchantStore getStore() {
        return store;
    }

    public void setStore(MerchantStore store) {
        this.store = store;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
