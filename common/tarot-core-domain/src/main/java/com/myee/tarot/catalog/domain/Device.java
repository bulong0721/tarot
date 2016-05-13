package com.myee.tarot.catalog.domain;

import com.myee.tarot.core.GenericEntity;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.*;

/**
 * Created by Martin on 2016/4/11.
 */
@Entity
@Table(name = "C_PRODUCT")
public class Device extends GenericEntity<Long, Device> {

    @Id
    @Column(name = "PRODUCT_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "PRODUCT_SEQ_NEXT_VAL")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    protected Long id;

    @Column(name = "URL")
    protected String url;

    @Column(name = "URL_KEY")
    protected String urlKey;

    @Column(name = "DISPLAY_TEMPLATE")
    protected String displayTemplate;

    @Column(name = "MODEL")
    protected String model;

    @Column(name = "USE_WITHOUT_OPTIONS")
    protected boolean useWithoutOptions = false;

    @OneToMany(mappedBy = "device", targetEntity = DeviceAttribute.class, cascade = {CascadeType.ALL}, orphanRemoval = true)
    @MapKey(name = "name")
    protected Map<String, DeviceAttribute> productAttributes = new HashMap<String, DeviceAttribute>();

    @OneToMany(targetEntity = DeviceOptionXref.class, mappedBy = "device",
            cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    protected List<DeviceOptionXref> productOptions = new ArrayList<DeviceOptionXref>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
    @JoinTable(name = "C_PRODUCT_CATEGORY",
            joinColumns = {@JoinColumn(name = "PRODUCT_ID", nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "CATEGORY_ID", nullable = false, updatable = false)}
    )
    @Cascade({
            org.hibernate.annotations.CascadeType.DETACH,
            org.hibernate.annotations.CascadeType.LOCK,
            org.hibernate.annotations.CascadeType.REFRESH,
            org.hibernate.annotations.CascadeType.REPLICATE
    })
    private Set<Category> categories = new HashSet<Category>();

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getDisplayTemplate() {
        return displayTemplate;
    }

    public void setDisplayTemplate(String displayTemplate) {
        this.displayTemplate = displayTemplate;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Map<String, DeviceAttribute> getProductAttributes() {
        return productAttributes;
    }

    public void setProductAttributes(Map<String, DeviceAttribute> productAttributes) {
        this.productAttributes = productAttributes;
    }

    public List<DeviceOptionXref> getProductOptions() {
        return productOptions;
    }

    public void setProductOptions(List<DeviceOptionXref> productOptions) {
        this.productOptions = productOptions;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlKey() {
        return urlKey;
    }

    public void setUrlKey(String urlKey) {
        this.urlKey = urlKey;
    }

    public boolean isUseWithoutOptions() {
        return useWithoutOptions;
    }

    public void setUseWithoutOptions(boolean useWithoutOptions) {
        this.useWithoutOptions = useWithoutOptions;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }
}
