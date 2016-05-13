package com.myee.tarot.catalog.domain;

import com.myee.tarot.core.GenericEntity;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Martin on 2016/4/11.
 */
@Entity
@Table(name = "C_PRODUCT_OPTION")
public class DeviceOption extends GenericEntity<Long, DeviceOption> {

    @Id
    @Column(name = "PRODUCT_OPTION_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "PRODUCT_OPTION_SEQ_NEXT_VAL")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    protected Long id;

    @Column(name = "OPTION_TYPE")
    protected String type;

    @Column(name = "ATTRIBUTE_NAME")
    protected String attributeName;

    @Column(name = "LABEL")
    protected String label;

    @Column(name = "REQUIRED")
    protected boolean required;

    @Column(name = "DISPLAY_ORDER")
    protected Integer displayOrder;

    @Column(name = "VALIDATION_STRATEGY_TYPE")
    private String productOptionValidationStrategyType;

    @Column(name = "VALIDATION_TYPE")
    private String productOptionValidationType;

    @Column(name = "VALIDATION_STRING")
    protected String validationString;

    @Column(name = "ERROR_CODE")
    protected String errorCode;

    @Column(name = "ERROR_MESSAGE")
    protected String errorMessage;

    @OneToMany(targetEntity = DeviceOptionXref.class, mappedBy = "deviceOption")
    protected List<DeviceOptionXref> products = new ArrayList<DeviceOptionXref>();

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValidationString() {
        return validationString;
    }

    public void setValidationString(String validationString) {
        this.validationString = validationString;
    }

    public List<DeviceOptionXref> getProducts() {
        return products;
    }

    public void setProducts(List<DeviceOptionXref> products) {
        this.products = products;
    }

    public String getProductOptionValidationStrategyType() {
        return productOptionValidationStrategyType;
    }

    public void setProductOptionValidationStrategyType(String productOptionValidationStrategyType) {
        this.productOptionValidationStrategyType = productOptionValidationStrategyType;
    }

    public String getProductOptionValidationType() {
        return productOptionValidationType;
    }

    public void setProductOptionValidationType(String productOptionValidationType) {
        this.productOptionValidationType = productOptionValidationType;
    }
}
