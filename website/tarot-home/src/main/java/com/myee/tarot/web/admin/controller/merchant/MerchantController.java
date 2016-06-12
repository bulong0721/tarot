package com.myee.tarot.web.admin.controller.merchant;

import com.myee.tarot.address.service.GeoZoneService;
import com.myee.tarot.admin.domain.AdminUser;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.util.ajax.AjaxResponse;
import com.myee.tarot.merchant.domain.Merchant;
import com.myee.tarot.merchant.domain.MerchantStore;
import com.myee.tarot.merchant.service.MerchantService;
import com.myee.tarot.merchant.service.MerchantStoreService;
import com.myee.tarot.merchant.type.BusinessType;
import com.myee.tarot.merchant.view.MerchantStoreView;
import com.myee.tarot.reference.domain.Address;
import com.myee.tarot.reference.domain.GeoZone;
import com.myee.tarot.web.util.DateUtil;
import com.myee.tarot.web.util.StringUtil;
import org.hibernate.criterion.Expression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.*;

/**
 * Created by Martin on 2016/4/21.
 */
@Controller
public class MerchantController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MerchantController.class);

    @Autowired
    private MerchantService merchantService;
    @Autowired
    private MerchantStoreService merchantStoreService;
    @Autowired
    private GeoZoneService geoZoneService;

    /**
     * 商户接口
     */

    //切换商户接口
    @RequestMapping(value = "/admin/merchant/switch", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse switchMerchant(@RequestBody Long id, HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        try {
            if (id == null) {
                //抛出异常给异常处理机制
                resp.setErrorString("请切换商户！");
                return resp;
            }
            Long numFind = merchantService.getCountById(id);
            if (numFind != 1L) {
                //抛出异常给异常处理机制
                resp.setErrorString("要切换的商户信息错误");
                return resp;
            }
            Merchant merchantOld = merchantService.getEntity(Merchant.class, id);
            //把商户信息写入session
            request.getSession().setAttribute(Constants.ADMIN_MERCHANT, merchantOld);
            resp.addDataEntry(objectToEntry(merchantOld));
        } catch (Exception e) {
            e.printStackTrace();
            resp.setErrorString("切换出错");
        }
        return resp;
    }

    @RequestMapping(value = "/admin/merchant/save", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse saveMerchant(@RequestBody Merchant merchant, HttpServletRequest request) throws Exception {
        AjaxResponse resp = null;
        try {
            //验证name,CuisineType,BusinessType不能为空
            if (StringUtil.isNullOrEmpty(merchant.getName())
                    || StringUtil.isNullOrEmpty(merchant.getCuisineType())
                    || StringUtil.isNullOrEmpty(merchant.getBusinessType())) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("名称/菜系/商户类型不能为空");
                return resp;
            }
            merchantService.update(merchant);//新建或更新
        } catch (Exception e) {
            e.printStackTrace();
            resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
            resp.setErrorString("出错");
            return resp;
        }
        return AjaxResponse.success();
    }

//    @RequestMapping(value = "/admin/merchant/edit", method = RequestMethod.POST)
//    @ResponseBody
//    public AjaxResponse editMerchant(@RequestBody Merchant merchant,HttpServletRequest request) throws Exception {
//        AjaxResponse resp = new AjaxResponse();
//        try {
//            if(merchant.getId() == null ){
//                //抛出异常给异常处理机制
//                resp.setError("参数错误！");
//                return resp;
//            }
//            Long numFind = merchantService.getCountById(merchant);
//            if (numFind != 1L) {
//                //抛出异常给异常处理机制
//                resp.setError("要修改的商户信息有误");
//                return resp;
//            }
//            Merchant merchantOld = merchantService.getEntity(Merchant.class, merchant.getId());
//            merchantOld.setName(StringUtil.nullToString(merchant.getName())); //不能为空
//            merchantOld.setLogo(merchant.getLogo());
//            merchantOld.setCuisineType(StringUtil.nullToString(merchant.getCuisineType()));//不能为空
//            merchantOld.setDescription(merchant.getDescription());
//            merchantOld.setBusinessType(StringUtil.nullToString(merchant.getBusinessType()));//不能为空
//            merchantService.save(merchantOld);
//        } catch (Exception e) {
//            e.printStackTrace();
//            resp.setError("更新出错");
//        }
//        return resp;
//    }

    @RequestMapping(value = "/admin/merchant/get", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResponse getMerchant(@RequestParam Long id, HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        try {
            Merchant merchant1 = merchantService.getEntity(Merchant.class, id);//id由前端切换商户传进来
            resp.addDataEntry(objectToEntry(merchant1));
        } catch (Exception e) {
            e.printStackTrace();
            resp.setErrorString("出错");
        }
        return resp;
    }

    @RequestMapping(value = "/admin/merchant/getSwitch", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResponse getSwitchMerchant(HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        try {
            //从session中读取merchant信息，如果为空，则提示用户先切换商户
            if (request.getSession().getAttribute(Constants.ADMIN_MERCHANT) == null) {
                resp.setErrorString("请先切换商户");
                return resp;
            }
            Merchant merchant1 = (Merchant) request.getSession().getAttribute(Constants.ADMIN_MERCHANT);
            resp.addDataEntry(objectToEntry(merchant1));
        } catch (Exception e) {
            e.printStackTrace();
            resp.setErrorString("出错");
        }
        return resp;
    }

    @RequestMapping(value = "/admin/merchant/delete", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse deleteMerchant(@RequestParam Long id, HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        try {
            Merchant merchant = merchantService.getEntity(Merchant.class, id);
            if (merchant != null) merchantService.delete(merchant);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setErrorString("删除出错");
        }
        return resp;
    }

    @RequestMapping(value = "/admin/merchant/list", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResponse listMerchant(HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        try {
            List<Merchant> merchantList = merchantService.list();
            for (Merchant merchant : merchantList) {
                resp.addDataEntry(objectToEntry(merchant));
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.setErrorString("出错");
        }
        return resp;
    }

    @RequestMapping(value = "/admin/merchant/list4Select", method = RequestMethod.GET)
    @ResponseBody
    public List listMerchant4Select(HttpServletRequest request) throws Exception {
        List resp = new ArrayList();
        try {
            List<Merchant> merchantList = merchantService.list();
            for (Merchant merchant : merchantList) {
                resp.add(objectToEntry(merchant));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resp;
    }

    @RequestMapping(value = "/admin/merchant/typeList4Select", method = RequestMethod.GET)
    @ResponseBody
    public List getMerchantType4Select() {
        return new BusinessType().getMerchantBusinessType4Select();
    }

    //把类转换成entry返回给前端，解耦和
    private Map objectToEntry(Merchant merchant) {
        Map entry = new HashMap();
        entry.put("id", merchant.getId());
        entry.put("name", merchant.getName());
        entry.put("businessType", merchant.getBusinessType());
        entry.put("businessTypeKey", new BusinessType().getBusinessTypeName(merchant.getBusinessType()));
        entry.put("cuisineType", merchant.getCuisineType());
        entry.put("logo", merchant.getLogo());
        entry.put("description", merchant.getDescription());
        return entry;
    }



    /**
     * 门店接口
     */

    @RequestMapping(value = "/admin/merchantStore/save", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse saveMerchantStore(@RequestBody MerchantStore merchantStore, HttpServletRequest request) throws Exception {
        AjaxResponse resp = null;
        try {
            //验证name,code，关联merchant_id不能为空
            if (StringUtil.isNullOrEmpty(merchantStore.getName())
                    || StringUtil.isNullOrEmpty(merchantStore.getCode())) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("名称和门店码不能为空");
                return resp;
            }
            //从session中取账号关联的商户信息
            if (request.getSession().getAttribute(Constants.ADMIN_MERCHANT) == null) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("请先切换商户");
                return resp;
            }

            Merchant merchant = (Merchant) request.getSession().getAttribute(Constants.ADMIN_MERCHANT);
            merchantStore.setMerchant(merchant);
            Address address = merchantStore.getAddress();
            GeoZone geoZone = new GeoZone();
            if (address.getProvince()!= null && address.getProvince().getId() != null && !address.getProvince().getId().toString().equals("")) {
                geoZone = geoZoneService.getEntity(GeoZone.class, address.getProvince().getId());
                address.setProvince(geoZone);
            }
            if (address.getCity()!= null && address.getCity().getId() != null && !address.getCity().getId().toString().equals("")) {
                geoZone = geoZoneService.getEntity(GeoZone.class, address.getCity().getId());
                address.setCity(geoZone);
            }
            if (address.getCounty()!= null && address.getCounty().getId() != null && !address.getCounty().getId().toString().equals("")) {
                geoZone = geoZoneService.getEntity(GeoZone.class, address.getCounty().getId());
                address.setCounty(geoZone);
            }
            if (address.getCircle()!= null && address.getCircle().getId() != null && !address.getCircle().getId().toString().equals("")) {
                geoZone = geoZoneService.getEntity(GeoZone.class, address.getCircle().getId());
                address.setCircle(geoZone);
            }
            if (address.getMall()!= null && address.getMall().getId() != null && !address.getMall().getId().toString().equals("")) {
                geoZone = geoZoneService.getEntity(GeoZone.class, address.getMall().getId());
                address.setMall(geoZone);
            }
            merchantStore.setAddress(address);
            merchantStoreService.update(merchantStore);//新建或更新
        } catch (Exception e) {
            e.printStackTrace();
            resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
            resp.setErrorString("出错");
            return resp;
        }
        return AjaxResponse.success();
    }

//    @RequestMapping(value = "/admin/merchantStore/edit", method = RequestMethod.POST)
//    @ResponseBody
//    public AjaxResponse editMerchantStore(@RequestBody MerchantStore merchantStore,HttpServletRequest request) throws Exception {
//        AjaxResponse resp = new AjaxResponse();
//        try {
//            if(merchantStore.getId() == null ){
//                //抛出异常给异常处理机制
//                resp.setError("参数错误！");
//                return resp;
//            }
//            Long numFind = merchantStoreService.getCountById(merchantStore);
//            if (numFind != 1L) {
//                //抛出异常给异常处理机制
//                resp.setErrorString("要修改的门店不存在");
//                return resp;
//            }
//            MerchantStore merchantStoreOld = merchantStoreService.getEntity(MerchantStore.class, merchantStore.getId());
//            merchantStoreOld.setName(StringUtil.nullToString(merchantStore.getName())); //不能为空
//            merchantStoreOld.setCode(StringUtil.nullToString(merchantStore.getCode()));//不能为空
//            merchantStoreOld.setAddress(merchantStore.getAddress());
//            merchantStoreOld.setCpp(merchantStore.getCpp());
//            merchantStoreOld.setDescription(merchantStore.getDescription());
//            merchantStoreOld.setPhone(merchantStore.getPhone());
//            merchantStoreService.save(merchantStoreOld);
//        } catch (Exception e) {
//            e.printStackTrace();
//            resp.setErrorString("更新出错");
//        }
//        return resp;
//    }

    @RequestMapping(value = "/admin/merchantStore/get", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResponse getMerchantStore(@RequestParam Long id, HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        try {
            MerchantStore merchantStore1 = merchantStoreService.getEntity(MerchantStore.class, id);//id由前端切换商户传进来
            resp.addDataEntry(objectToEntry(merchantStore1));
        } catch (Exception e) {
            e.printStackTrace();
            resp.setErrorString("出错");
        }
        return resp;
    }

    @RequestMapping(value = "/admin/merchantStore/delete", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse deleteMerchantStore(@RequestParam Long id, HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        try {
            if (id == null || StringUtil.isNullOrEmpty(id.toString())) {
                resp.setErrorString("参数错误");
                return resp;
            }
            MerchantStore merchantStore = merchantStoreService.getEntity(MerchantStore.class, id);
            if (merchantStore != null) merchantStoreService.delete(merchantStore);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setErrorString("删除出错");
        }
        return resp;
    }

    @RequestMapping(value = "/admin/merchantStore/list", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResponse listMerchantStore(HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        try {
            List<MerchantStore> merchantStoreList = merchantStoreService.list();
            for (MerchantStore merchantStore : merchantStoreList) {
                resp.addDataEntry(objectToEntry(merchantStore));
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.setErrorString("出错");
        }
        return resp;
    }

    @RequestMapping(value = "/admin/merchantStore/listByMerchant", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResponse listMerchantStoreByMerchant(HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        try {
            //从session中取账号关联的商户信息
            if (request.getSession().getAttribute(Constants.ADMIN_MERCHANT) == null) {
                resp.setErrorString("请先切换商户");
                return resp;
            }

            Merchant merchant = (Merchant) request.getSession().getAttribute(Constants.ADMIN_MERCHANT);

            List<MerchantStore> merchantStoreList = merchantStoreService.listByMerchant(merchant.getId());
            for (MerchantStore merchantStore : merchantStoreList) {
                resp.addDataEntry(objectToEntry(merchantStore));
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.setErrorString("出错");
        }
        return resp;
    }

    @RequestMapping(value = "/admin/merchantStore/listByMerchantForSelect", method = RequestMethod.GET)
    @ResponseBody
    public List listMerchantStoreByMerchantForSelect(HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        try {
            //从session中取账号关联的商户信息
            if (request.getSession().getAttribute(Constants.ADMIN_MERCHANT) == null) {
                resp.setErrorString("请先切换商户");
                Map entry = new HashMap();
                entry.put("error", "请先切换商户");
                resp.addDataEntry(entry);
            }

            Merchant merchant = (Merchant) request.getSession().getAttribute(Constants.ADMIN_MERCHANT);
            List<MerchantStore> merchantStoreList = merchantStoreService.listByMerchant(merchant.getId());
            for (MerchantStore merchantStore : merchantStoreList) {
                Map entry = new HashMap();
                entry.put("name", merchantStore.getName());
                entry.put("value", merchantStore.getId());
                resp.addDataEntry(entry);
            }
            return resp.getRows();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/admin/merchantStore/storeOpts", method = RequestMethod.GET)
    @ResponseBody
    public List<MerchantStoreView> listByCommon(HttpServletRequest request) throws Exception {
        List<MerchantStoreView> merchantStoreViewList = new ArrayList<MerchantStoreView>();
        try {
            //从session中取账号关联的商户信息
//            if(request.getSession().getAttribute(Constants.ADMIN_MERCHANT) == null ){
//                return null;
//            }
//            Merchant merchant = (Merchant)request.getSession().getAttribute(Constants.ADMIN_MERCHANT);
            List<MerchantStore> merchantStoreList = merchantStoreService.listByMerchant(100L);
            for(MerchantStore store : merchantStoreList){
                merchantStoreViewList.add(new MerchantStoreView(store));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return merchantStoreViewList;
    }

    //把类转换成entry返回给前端，解耦和
    private Map objectToEntry(MerchantStore merchantStore) {
        Map entry = new HashMap();
        entry.put("id", merchantStore.getId());
        entry.put("name", merchantStore.getName());
        entry.put("code", merchantStore.getCode());
        entry.put("cpp", merchantStore.getCpp());
        entry.put("experience", merchantStore.isExperience());
        entry.put("description", merchantStore.getDescription());
        entry.put("phone", merchantStore.getPhone());
        entry.put("postalCode", merchantStore.getPostalCode());
        entry.put("ratings", merchantStore.getRatings());
        entry.put("merchant", merchantStore.getMerchant());
        entry.put("address", merchantStore.getAddress());
        return entry;
    }


    /**
     * 公共函数
     */
    AdminUser getUserInfo(HttpServletRequest req) {
        return (AdminUser) req.getSession().getAttribute(Constants.ADMIN_USER);//正式用
    }

}
