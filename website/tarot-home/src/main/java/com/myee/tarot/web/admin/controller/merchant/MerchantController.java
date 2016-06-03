package com.myee.tarot.web.admin.controller.merchant;

import com.google.common.collect.Lists;
import com.myee.tarot.admin.domain.AdminUser;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.util.ajax.AjaxResponse;
import com.myee.tarot.merchant.domain.Merchant;
import com.myee.tarot.merchant.domain.MerchantStore;
import com.myee.tarot.merchant.service.MerchantService;
import com.myee.tarot.merchant.service.MerchantStoreService;
import com.myee.tarot.web.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * 商户接口
     */

    //切换商户接口
    @RequestMapping(value = "/admin/merchant/switch", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse switchMerchant(@ModelAttribute Merchant merchant,HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        try {
            if(merchant == null || merchant.getId() == null ){
                //抛出异常给异常处理机制
                resp.setErrorString("请切换商户！");
                return resp;
            }
            Long numFind = merchantService.getCountById(merchant);
            if (numFind != 1L) {
                //抛出异常给异常处理机制
                resp.setErrorString("要切换的商户信息错误");
                return resp;
            }
            Merchant merchantOld = merchantService.getEntity(Merchant.class, merchant.getId());
            //把商户信息写入session
            request.getSession().setAttribute(Constants.ADMIN_MERCHANT, merchantOld);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setErrorString("切换出错");
        }
        return resp;
    }

    @RequestMapping(value = "/admin/merchant/save", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse saveMerchant(@RequestBody Merchant merchant,HttpServletRequest request) throws Exception {
        AjaxResponse resp = null;
        try {
            //验证name,CuisineType,BusinessType不能为空
            if(StringUtil.isNullOrEmpty(merchant.getName())
                    || StringUtil.isNullOrEmpty(merchant.getCuisineType())
                    || StringUtil.isNullOrEmpty(merchant.getBusinessType())){
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
    public AjaxResponse getMerchant(@RequestParam Long id,HttpServletRequest request) throws Exception {
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
            if(request.getSession().getAttribute(Constants.ADMIN_MERCHANT) == null ){
                resp.setErrorString("请先切换商户");
                return resp;
            }
            Merchant merchant1 = (Merchant)request.getSession().getAttribute(Constants.ADMIN_MERCHANT);
            resp.addDataEntry(objectToEntry(merchant1));
        } catch (Exception e) {
            e.printStackTrace();
            resp.setErrorString("出错");
        }
        return resp;
    }

    @RequestMapping(value = "/admin/merchant/delete", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse deleteMerchant(@RequestParam Long id,HttpServletRequest request) throws Exception {
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

    @RequestMapping(value = "/admin/merchant/typeList", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResponse getMerchantType(){
        AjaxResponse resp = new AjaxResponse();
        Map entry = new HashMap();
        entry.put("AL","商场");
        entry.put("AK","餐饮");
        entry.put("AZ","零售");
        entry.put("AR","其他");
        entry.put("CA","商圈");
        resp.addDataEntry(entry);

        return resp;
    }

    //把类转换成entry返回给前端，解耦和
    private Map objectToEntry(Merchant merchant) {
        Map entry = new HashMap();
        entry.put("id", merchant.getId());
        entry.put("name", merchant.getName());
        entry.put("businessType", merchant.getBusinessType());
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
    public AjaxResponse saveMerchantStore(@RequestBody MerchantStore merchantStore,HttpServletRequest request) throws Exception {
        AjaxResponse resp = null;
        try {
            //验证name,code，关联merchant_id不能为空
            if(StringUtil.isNullOrEmpty(merchantStore.getName())
                    || StringUtil.isNullOrEmpty(merchantStore.getCode())){
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("名称和门店码不能为空");
                return resp;
            }
            //从session中取账号关联的商户信息
            if(request.getSession().getAttribute(Constants.ADMIN_MERCHANT) == null ){
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("请先切换商户");
                return resp;
            }

            Merchant merchant = (Merchant)request.getSession().getAttribute(Constants.ADMIN_MERCHANT);
            merchantStore.setMerchant(merchant);
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
    public AjaxResponse getMerchantStore(@RequestParam Long id,HttpServletRequest request) throws Exception {
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
    public AjaxResponse deleteMerchantStore(@RequestParam Long id,HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        try {
            if(id == null || StringUtil.isNullOrEmpty(id.toString())){
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
            if(request.getSession().getAttribute(Constants.ADMIN_MERCHANT) == null ){
                resp.setErrorString("请先切换商户");
                return resp;
            }

            Merchant merchant = (Merchant)request.getSession().getAttribute(Constants.ADMIN_MERCHANT);

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
            if(request.getSession().getAttribute(Constants.ADMIN_MERCHANT) == null ){
                resp.setErrorString("请先切换商户");
                Map entry = new HashMap();
                entry.put("error","请先切换商户");
                resp.addDataEntry(entry);
            }

            Merchant merchant = (Merchant)request.getSession().getAttribute(Constants.ADMIN_MERCHANT);
            List<MerchantStore> merchantStoreList = merchantStoreService.listByMerchant(merchant.getId());
            for (MerchantStore merchantStore : merchantStoreList) {
                Map entry = new HashMap();
                entry.put("name",merchantStore.getName());
                entry.put("value",merchantStore.getId());
                resp.addDataEntry(entry);
            }
            return resp.getRows();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
        entry.put("merchant",merchantStore.getMerchant());
        entry.put("address",merchantStore.getAddress());
        return entry;
    }


    /**
     * 公共函数
     */
    AdminUser getUserInfo(HttpServletRequest req){
        return (AdminUser) req.getSession().getAttribute(Constants.ADMIN_USER);//正式用
    }

}
