package com.myee.tarot.resource.domain;

import com.myee.tarot.catalog.domain.DeviceUsed;
import com.myee.tarot.catering.domain.TableType;
import com.myee.tarot.catering.domain.TableZone;
import com.myee.tarot.core.GenericEntity;
import com.myee.tarot.merchant.domain.MerchantStore;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by Chay on 2016/12/15.
 */
@Entity
@Table(name = "C_UPDATE_CONFIG")
public class UpdateConfig extends GenericEntity<Long, UpdateConfig> {

    @Id
    @Column(name = "CONFIG_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "UPDATE_CONFIG_SEQ_NEXT_VAL",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    private Long id;

    @Column(name = "NAME", nullable = false,length = 30)
    protected String name;//配置文件名

    @Column(name = "DESCRIPTION",length = 255)
    protected String description;

    @Column(name = "TYPE",nullable = false,length = 10)
    protected String type;//配置文件类型:apk,module等

    @Column(name = "SEE_TYPE",nullable = false,length = 10)
    private String seeType;//CHECKED：勾选设备可见，ALL：所有设备可见，NONE：所有设备不可见

    @Column(name = "CREATE_TIME")
    private Date createTime;

    @Column(name = "PATH",length = 255)
    protected String path;//配置文件下载路径

    @Column(name = "DEVICE_GROUP_NO_LIST",columnDefinition = "TEXT")
    private String deviceGroupNOList;//勾选设备列表,作为历史记录

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSeeType() {
        return seeType;
    }

    public void setSeeType(String seeType) {
        this.seeType = seeType;
    }

	public String getDeviceGroupNOList() {
		return deviceGroupNOList;
	}

	public void setDeviceGroupNOList(String deviceGroupNOList) {
		this.deviceGroupNOList = deviceGroupNOList;
	}
}
