package com.myee.tarot.apiold.domain;


import com.myee.tarot.core.GenericEntity;
import com.myee.tarot.merchant.domain.MerchantStore;

import javax.persistence.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;

/**
 * Info: clever
 * User: Gary.zhang@clever-m.com
 * Date: 1/20/16
 * Time: 14:17
 * Version: 1.0
 * History: <p>如果有修改过程，请记录</P>
 */
@Entity
@javax.persistence.Table(name = "CA_MENU")
public class MenuInfo extends GenericEntity<Long, MenuInfo> {

    @Id
    @Column(name = "ID", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)//AUTO：主键由程序控制。IDENTITY：主键由数据库自动生成（主要是自动增长型） ； SEQUENCE：根据底层数据库的序列来生成主键，条件是数据库支持序列（类似oracle）；TABLE：使用一个特定的数据库表格来保存主键。
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STORE_ID")
    private MerchantStore store;//商户编号

    @Column(name = "MENU_ID",length = 15)
    private String menuId;     //ERP编码
    @Column(name = "SUB_MENU_ID",length = 10)
    private String subMenuId;  //
    @Column(name = "NAME",length = 60)
    private String name;       //名称
    @Column(name = "PRICE",length = 11)
    private String price;      //价格
    @Column(name = "AMOUNT")
    private int amount;        //价格(分)
    @Column(name = "UNIT",length = 6)
    private String unit;       //单位
    @Column(name = "MENU_CODE",length = 60)
    private String menuCode;   //编码
    @Column(name = "SCAN_CODE",length = 10)
    private String scanCode;   //扫描码
    @Column(name = "CREATED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @Column(name = "UPDATED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;
    @Column(name = "ACTIVE",columnDefinition = "BIT")
    private Boolean active = Boolean.TRUE;
    @Column(name = "PHOTO",length = 100)
    private String photo;

    public MenuInfo(){
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getSubMenuId() {
        return subMenuId;
    }

    public void setSubMenuId(String subMenuId) {
        this.subMenuId = subMenuId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        if(amount>0){
            DecimalFormat format = new DecimalFormat("0.00");
            price = format.format(new BigDecimal(amount/100));
        }
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }

    public String getScanCode() {
        return scanCode;
    }

    public void setScanCode(String scanCode) {
        this.scanCode = scanCode;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public MerchantStore getStore() {
        return store;
    }

    public void setStore(MerchantStore store) {
        this.store = store;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }
}
