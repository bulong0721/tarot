package com.myee.tarot.resource.domain;

import com.myee.tarot.catalog.domain.ProductUsed;
import com.myee.tarot.core.GenericEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Chay on 2016/12/15.
 */
@Entity
@Table(name = "C_UPDATE_CONFIG_PRODUCT_USED_XREF")
public class UpdateConfigProductUsedXREF extends GenericEntity<Long, UpdateConfigProductUsedXREF> {

    @Id
    @Column(name = "XREF_ID", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(targetEntity = UpdateConfig.class, optional = false)
    @JoinColumn(name = "CONFIG_ID")
    protected UpdateConfig updateConfig;

    @ManyToOne(targetEntity = ProductUsed.class, optional = false)
    @JoinColumn(name = "CODE")
    protected ProductUsed productUsed;

    @Column(name = "TYPE",nullable = false,length = 10)
    protected String type;//配置文件类型:apk,module等

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public UpdateConfig getUpdateConfig() {
        return updateConfig;
    }

    public void setUpdateConfig(UpdateConfig updateConfig) {
        this.updateConfig = updateConfig;
    }

    public ProductUsed getProductUsed() {
        return productUsed;
    }

    public void setProductUsed(ProductUsed productUsed) {
        this.productUsed = productUsed;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
