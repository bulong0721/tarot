package com.myee.tarot.apiold.view;

import com.myee.tarot.apiold.bean.BaseInfo;
import com.myee.tarot.apiold.domain.MenuInfo;
import com.myee.tarot.core.util.StringUtil;

/**
 * Info: clever
 * User: Gary.zhang@clever-m.com
 * Date: 1/20/16
 * Time: 14:17
 * Version: 1.0
 * History: <p>如果有修改过程，请记录</P>
 */
public class MenuInfoView extends BaseInfo {
    private String menuId;     //ERP编码
    private String subMenuId;  //
    private long shopId;       //商户编号
    private String name;       //名称
    private String price;      //价格
    private String unit;       //单位
    private String menuCode;   //编码
    private String scanCode;   //扫描码
    private String photo;

    public MenuInfoView(){
    }

    public MenuInfoView(long shopId){
        this.shopId = shopId;
    }

    public MenuInfoView(MenuInfo menuInfo){
        if(menuInfo != null){
            this.menuId = menuInfo.getMenuId();
            this.subMenuId = menuInfo.getSubMenuId();
            this.shopId = menuInfo.getStore().getId();
            this.name = menuInfo.getName();
            this.price = menuInfo.getPrice();
            this.unit = menuInfo.getUnit();
            this.menuCode = menuInfo.getMenuCode();
            this.scanCode = menuInfo.getScanCode();
            this.photo = menuInfo.getPhoto();
        }
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public long getShopId() {
        return shopId;
    }

    public void setShopId(long shopId) {
        this.shopId = shopId;
    }

    public String getSubMenuId() {
        return subMenuId;
    }

    public void setSubMenuId(String subMenuId) {
        this.subMenuId = subMenuId;
    }

    public String getScanCode() {
        return scanCode;
    }

    public void setScanCode(String scanCode) {
        this.scanCode = scanCode;
    }

    public String getPhoto() {
        if(!StringUtil.isEmpty(photo)){
            return "http://7xs3f5.com2.z0.glb.qiniucdn.com/" + photo;//正式服
//            return "http://7xl2nm.com2.z0.glb.qiniucdn.com/" + photo;//测试服
        } else {
            return photo;
        }
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }


}
