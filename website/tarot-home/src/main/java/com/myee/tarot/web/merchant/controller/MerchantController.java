package com.myee.tarot.web.merchant.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.util.DateTimeUtils;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.ajax.AjaxPageableResponse;
import com.myee.tarot.core.util.ajax.AjaxResponse;
import com.myee.tarot.merchant.domain.Merchant;
import com.myee.tarot.merchant.domain.MerchantStore;
import com.myee.tarot.merchant.service.MerchantService;
import com.myee.tarot.merchant.service.MerchantStoreService;
import com.myee.tarot.merchant.type.BusinessType;
import com.myee.tarot.campaign.domain.SaleCorpMerchant;
import com.myee.tarot.campaign.service.SaleCorpMerchantService;
import com.myee.tarot.merchant.type.CuisineType;
import com.myee.tarot.profile.domain.Address;
import com.myee.tarot.profile.domain.GeoZone;
import com.myee.tarot.profile.service.GeoZoneService;
import com.myee.tarot.core.util.StringUtil;
import com.myee.tarot.web.apiold.util.CommonLoginParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

/**
 * Created by Martin on 2016/4/21.
 */
@Controller
public class MerchantController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MerchantController.class);

    @Autowired
    private MerchantService         merchantService;
    @Autowired
    private MerchantStoreService    merchantStoreService;
    @Autowired
    private GeoZoneService          geoZoneService;
    @Autowired
    private SaleCorpMerchantService saleCorpMerchantService;

    /**
     * 商户接口
     */
    @RequestMapping(value = "admin/merchant/save", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse saveMerchant(@RequestBody Merchant merchant, HttpServletRequest request) throws Exception {
        AjaxResponse resp = null;
        try {
            //验证name,CuisineType,BusinessType不能为空
            if (StringUtil.isBlank(merchant.getName())
                    || StringUtil.isBlank(merchant.getBusinessType())) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("名称/商户类型不能为空");
                return resp;
            }
            merchant.setCuisineType(StringUtil.isBlank(merchant.getCuisineType())?"":merchant.getCuisineType());
            Merchant merchant1 = merchantService.update(merchant);//新建或更新

            Long counts = merchantStoreService.getCountById(null, merchant1.getId());
            if (counts == 0) {
                //新建商户后，如果商户下没有门店自动新建一个默认店铺
                MerchantStore merchantStore = new MerchantStore();
                merchantStore.setMerchant(merchant1);
                merchantStore.setName(merchant1.getName() + "默认门店");
                merchantStore.setCode("defaultCode0000"+ DateTimeUtils.toShortDateTime(new Date()));
                merchantStoreService.update(merchantStore);
            }
            resp = AjaxResponse.success();
            resp.addEntry("updateResult", objectToEntry(merchant1));
        } catch (Exception e) {
            e.printStackTrace();
            resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
            resp.setErrorString("出错");
            return resp;
        }
        return resp;
    }

    @RequestMapping(value = "admin/merchant/get", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResponse getMerchant(@RequestParam Long id, HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        try {
            Merchant merchant1 = merchantService.findById(id);//id由前端切换商户传进来
            resp.addDataEntry(objectToEntry(merchant1));
        } catch (Exception e) {
            e.printStackTrace();
            resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
            resp.setErrorString("出错");
        }
        return resp;
    }

    @RequestMapping(value = {"admin/merchant/getSwitch","shop/merchant/getSwitch"}, method = RequestMethod.GET)
    @ResponseBody
    public AjaxResponse getSwitchMerchant(HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        try {
            //从session中读取merchant信息，如果为空，则提示用户先切换商户
            if (request.getSession().getAttribute(Constants.ADMIN_MERCHANT) == null) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("请先切换商户");
                return resp;
            }
            Merchant merchant1 = (Merchant) request.getSession().getAttribute(Constants.ADMIN_MERCHANT);
            resp.addDataEntry(objectToEntry(merchant1));
        } catch (Exception e) {
            e.printStackTrace();
            resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
            resp.setErrorString("出错");
        }
        return resp;
    }

    @RequestMapping(value = "admin/merchant/delete", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse deleteMerchant(@Valid @RequestBody Merchant merchantDelete, HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        try {
            Merchant thisSwitchMerchant = (Merchant)request.getSession().getAttribute(Constants.ADMIN_MERCHANT);
            if(thisSwitchMerchant == null ){
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("请先切换门店");
                return resp;
            }
            if(thisSwitchMerchant.getId() == merchantDelete.getId()){
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("要删除的商户是当前切换的门店商户，不能删除");
                return resp;
            }


            Merchant merchant = merchantService.findById(merchantDelete.getId());
            if (merchant != null) merchantService.delete(merchant);
        } catch (Exception e) {
            e.printStackTrace();
            resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
            resp.setErrorString("删除出错");
        }
        return resp;
    }

    @RequestMapping(value = "admin/merchant/list", method = RequestMethod.GET)
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
            resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
            resp.setErrorString("出错");
        }
        return resp;
    }

    @RequestMapping(value = "admin/merchant/paging", method = RequestMethod.GET)
    @ResponseBody
    public AjaxPageableResponse pageMerchant(HttpServletRequest request, PageRequest pageRequest) throws Exception {
        AjaxPageableResponse resp = new AjaxPageableResponse();
        try {
            PageResult<Merchant> pageList = merchantService.pageList(pageRequest);

            List<Merchant> merchantList = pageList.getList();
            for (Merchant merchant : merchantList) {
                resp.addDataEntry(objectToEntry(merchant));
            }
            resp.setRecordsTotal(pageList.getRecordsTotal());
        } catch (Exception e) {
            e.printStackTrace();
            resp.setErrorString("出错");
        }
        return resp;
    }

    @RequestMapping(value = "admin/merchant/list4Select", method = RequestMethod.GET)
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

    @RequestMapping(value = {"admin/merchant/typeList4Select","shop/merchant/typeList4Select"}, method = RequestMethod.GET)
    @ResponseBody
    public List getMerchantType4Select() {
        return new BusinessType().getMerchantBusinessType4Select();
    }

    @RequestMapping(value = {"admin/merchant/cuisineList4Select","shop/merchant/cuisineList4Select"}, method = RequestMethod.GET)
    @ResponseBody
    public List getMerchantCuisine4Select() {
        return new CuisineType().getMerchantCuisine4Select();
    }

    //把类转换成entry返回给前端，解耦和
    private Map objectToEntry(Merchant merchant) {
        Map entry = new HashMap();
        entry.put("id", merchant.getId());
        entry.put("name", merchant.getName());
        entry.put("businessType", merchant.getBusinessType());
        entry.put("businessTypeKey", new BusinessType().getBusinessTypeName(merchant.getBusinessType()));
        String suisineType = merchant.getCuisineType();
        entry.put("cuisineType",(suisineType == null || "".equals(suisineType))?"": new CuisineType().getCuisineTypeName(merchant.getCuisineType()));
        entry.put("logo", merchant.getLogo());
        entry.put("description", merchant.getDescription());
        return entry;
    }


    /**
     * 门店接口
     */

    @RequestMapping(value = "admin/merchantStore/save", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse saveMerchantStore(@RequestBody MerchantStore merchantStore, HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        try {
            //验证name,code，关联merchant_id不能为空
            if (StringUtil.isBlank(merchantStore.getName())
                    || StringUtil.isBlank(merchantStore.getCode())) {
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
            //code不能重复
            MerchantStore merchantStoreTemp = merchantStoreService.getByCode(merchantStore.getCode());
            if( merchantStoreTemp != null && merchantStoreTemp.getId() != merchantStore.getId()){
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("错误:重复的门店码，请修改后重新提交");
                return resp;
            }

            Merchant merchant = (Merchant) request.getSession().getAttribute(Constants.ADMIN_MERCHANT);
            merchantStore.setMerchant(merchant);
            Address address = merchantStore.getAddress();
            GeoZone geoZone = new GeoZone();
            if (address != null) {
                if (validGeoZone(address.getProvince())) {
                    geoZone = geoZoneService.findById(address.getProvince().getId());
                    address.setProvince(geoZone);
                }
                if (validGeoZone(address.getCity())) {
                    geoZone = geoZoneService.findById(address.getCity().getId());
                    address.setCity(geoZone);
                }
                if (validGeoZone(address.getCounty())) {
                    geoZone = geoZoneService.findById(address.getCounty().getId());
                    address.setCounty(geoZone);
                }
                if (validGeoZone(address.getCircle())) {
                    geoZone = geoZoneService.findById(address.getCircle().getId());
                    address.setCircle(geoZone);
                }
                if (validGeoZone(address.getMall())) {
                    geoZone = geoZoneService.findById(address.getMall().getId());
                    address.setMall(geoZone);
                }
            }
            merchantStore.setAddress(address);
            merchantStore = merchantStoreService.update(merchantStore);//新建或更新

            resp = AjaxResponse.success();
            resp.addEntry("updateResult", objectToEntry(merchantStore));
        } catch (Exception e) {
            e.printStackTrace();
            resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
            resp.setErrorString("出错");
            return resp;
        }
        return resp;
    }

    private boolean validGeoZone(GeoZone geoZone) {
        if (geoZone != null && geoZone.getId() != null && !geoZone.getId().toString().equals("")) {
            return true;
        } else {
            return false;
        }
    }

    @RequestMapping(value = "admin/merchantStore/get", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResponse getMerchantStore(@RequestParam Long id, HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        try {
            MerchantStore merchantStore1 = merchantStoreService.findById(id);//id由前端切换商户传进来
            resp.addDataEntry(objectToEntry(merchantStore1));
        } catch (Exception e) {
            e.printStackTrace();
            resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
            resp.setErrorString("出错");
        }
        return resp;
    }

    @RequestMapping(value = "admin/merchantStore/delete", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse deleteMerchantStore(@Valid @RequestBody MerchantStore merchantStoreDelete, HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        try {
            MerchantStore thisSwitchMerchantStore = (MerchantStore)request.getSession().getAttribute(Constants.ADMIN_STORE);
            if(thisSwitchMerchantStore == null ){
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("请先切换门店");
                return resp;
            }
            if(thisSwitchMerchantStore.getId() == merchantStoreDelete.getId()){
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("要删除的门店是当前切换的门店，不能删除");
                return resp;
            }

            if (merchantStoreDelete.getId() == null || StringUtil.isBlank(merchantStoreDelete.getId().toString())) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("参数错误");
                return resp;
            }
            MerchantStore merchantStore = merchantStoreService.findById(merchantStoreDelete.getId());
            if (merchantStore != null) merchantStoreService.delete(merchantStore);
        } catch (Exception e) {
            e.printStackTrace();
            resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
            resp.setErrorString("删除出错");
        }
        return resp;
    }

    @RequestMapping(value = {"admin/merchantStore/list","shop/merchantStore/list"}, method = RequestMethod.GET)
    @ResponseBody
    public AjaxPageableResponse listMerchantStore(PageRequest pageRequest , HttpServletRequest request) throws Exception {
        AjaxPageableResponse resp = new AjaxPageableResponse();
        try {
            String path = request.getServletPath();
            if (path.contains("/admin/")) {
                PageResult<MerchantStore> pageList = merchantStoreService.pageListByMerchant(null, pageRequest);
                List<MerchantStore> merchantStoreList = pageList.getList();
                for (MerchantStore merchantStore : merchantStoreList) {
                    resp.addDataEntry(objectToEntry(merchantStore));
                }
                resp.setRecordsTotal(pageList.getRecordsTotal());
            } else if (path.contains("/shop/")) {
                resp.setRecordsTotal(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.setErrorString("出错");
        }
        return resp;
    }

    @RequestMapping(value = "admin/merchantStore/listByMerchant", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResponse listMerchantStoreByMerchant(HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        try {
            //从session中取账号关联的商户信息
            if (request.getSession().getAttribute(Constants.ADMIN_MERCHANT) == null) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("请先切换商户");
                return resp;
            }

            Merchant merchant = (Merchant) request.getSession().getAttribute(Constants.ADMIN_MERCHANT);

            List<MerchantStore> merchantStoreList = merchantStoreService.pageListByMerchant(merchant.getId(), new PageRequest()).getList();
            for (MerchantStore merchantStore : merchantStoreList) {
                resp.addDataEntry(objectToEntry(merchantStore));
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
            resp.setErrorString("出错");
        }
        return resp;
    }

    @RequestMapping(value = "admin/merchantStore/pagingByMerchant", method = RequestMethod.GET)
    @ResponseBody
    public AjaxPageableResponse pagingMerchantStoreByMerchant(HttpServletRequest request, PageRequest pageRequest) throws Exception {
        AjaxPageableResponse resp = new AjaxPageableResponse();
        try {
            //从session中取账号关联的商户信息
            if (request.getSession().getAttribute(Constants.ADMIN_MERCHANT) == null) {
                resp.setErrorString("请先切换商户");
                return resp;
            }

            Merchant merchant = (Merchant) request.getSession().getAttribute(Constants.ADMIN_MERCHANT);

            PageResult<MerchantStore> pageList = merchantStoreService.pageListByMerchant(merchant.getId(), pageRequest);

            List<MerchantStore> merchantStoreList = pageList.getList();
            for (MerchantStore merchantStore : merchantStoreList) {
                SaleCorpMerchant saleCorpMerchant = saleCorpMerchantService.findByMerchantId(merchantStore.getId());
                List<MerchantStore> bindStores = Lists.newArrayList();
                if(saleCorpMerchant!=null&& !StringUtil.isBlank(saleCorpMerchant.getRelatedMerchants())){
                    List<Long> bindStore = JSON.parseArray(saleCorpMerchant.getRelatedMerchants(), Long.class);
                    for (Long storeId : bindStore) {
                        MerchantStore store = merchantStoreService.findById(storeId);
                        bindStores.add(store);
                    }
                }
                resp.addDataEntry(objectToEntryAdd(merchantStore, bindStores));
            }
            resp.setRecordsTotal(pageList.getRecordsTotal());
        } catch (Exception e) {
            e.printStackTrace();
            resp.setErrorString("出错");
        }
        return resp;
    }

    @RequestMapping(value = "admin/merchantStore/listByMerchantForSelect", method = RequestMethod.GET)
    @ResponseBody
    public List listMerchantStoreByMerchantForSelect(HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        try {
            //从session中取账号关联的商户信息
            if (request.getSession().getAttribute(Constants.ADMIN_MERCHANT) == null) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("请先切换商户");
                Map entry = new HashMap();
                entry.put("error", "请先切换商户");
                resp.addDataEntry(entry);
                return null;
            }

            Merchant merchant = (Merchant) request.getSession().getAttribute(Constants.ADMIN_MERCHANT);
            List<MerchantStore> merchantStoreList = merchantStoreService.pageListByMerchant(merchant.getId(), new PageRequest()).getList();
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

    //切换门店接口
    @RequestMapping(value = "admin/merchantStore/switch", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse switchMerchantStore(@RequestBody Long id, HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        try {
            if (id == null) {
                //抛出异常给异常处理机制
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("请切换门店！");
                return resp;
            }
            Long numFind = merchantStoreService.getCountById(id,null);
            if (numFind != 1L) {
                //抛出异常给异常处理机制
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("要切换的商户信息错误");
                return resp;
            }
            MerchantStore merchantStoreOld = merchantStoreService.findById(id);
            Merchant merchantOld = merchantService.findById(merchantStoreOld.getMerchant().getId());
            //把商户信息写入session
            request.getSession().setAttribute(Constants.ADMIN_STORE, merchantStoreOld);
            request.getSession().setAttribute(Constants.ADMIN_MERCHANT, merchantOld);
            resp.addDataEntry(objectToEntry(merchantStoreOld));
        } catch (Exception e) {
            e.printStackTrace();
            resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
            resp.setErrorString("切换出错");
        }
        return resp;
    }

    @RequestMapping(value = {"admin/merchantStore/getSwitch","shop/merchantStore/getSwitch"}, method = RequestMethod.GET)
    @ResponseBody
    public AjaxResponse getSwitchMerchantStore(HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        try {
            String path = request.getServletPath();
            String sessionName = CommonLoginParam.getRequestInfo(request).get(Constants.REQUEST_INFO_SESSION).toString();
            //从session中读取merchantStore信息，如果为空，则提示用户先切换门店
            if (request.getSession().getAttribute(sessionName) == null) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("请先切换门店");
                return resp;
            }
            MerchantStore merchantStore1 = (MerchantStore) request.getSession().getAttribute(sessionName);
            resp.addDataEntry(objectToEntry(merchantStore1));
        } catch (Exception e) {
            e.printStackTrace();
            resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
            resp.setErrorString("出错");
        }
        return resp;
    }

    @RequestMapping(value = "admin/merchantStore/getAllStoreExceptSelf", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResponse getAllStoreExceptSelf(){
        try {
            AjaxResponse resp =  new AjaxResponse();
            List<MerchantStore> result = merchantStoreService.list();
            for (MerchantStore merchantStore : result) {
                resp.addDataEntry(objectToEntry(merchantStore));
            }
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxResponse.failed(-1);

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

    //把类转换成entry返回给前端，解耦和  额外添加个绑定属性
    private Map objectToEntryAdd(MerchantStore merchantStore,List<MerchantStore> bindStores) {
        Map entry = objectToEntry(merchantStore);
        entry.put("bindStores",bindStores);
        return entry;
    }

}
