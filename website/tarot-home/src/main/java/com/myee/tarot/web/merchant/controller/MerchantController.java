package com.myee.tarot.web.merchant.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.myee.tarot.admin.domain.AdminUser;
import com.myee.tarot.admin.service.AdminUserService;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.util.*;
import com.myee.tarot.core.util.ajax.AjaxPageableResponse;
import com.myee.tarot.core.util.ajax.AjaxResponse;
import com.myee.tarot.customer.domain.Customer;
import com.myee.tarot.customer.service.CustomerService;
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
import com.myee.tarot.web.apiold.util.CommonLoginParam;
import com.myee.tarot.web.apiold.util.FileValidCreateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
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
    @Autowired
    private AdminUserService userService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private SaleCorpMerchantService saleCorpMerchantService;
    @Value("${cleverm.push.dirs}")
    private String DOWNLOAD_HOME;

    /**
     * 商户接口
     */
    @RequestMapping(value = "admin/merchant/save", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse saveMerchant(/*@RequestParam(value = "file") String imgBase64,*/@RequestBody Merchant merchant, HttpServletRequest request) throws Exception {
        AjaxResponse resp = null;
        try {
            //验证name,CuisineType,BusinessType不能为空
            if (StringUtil.isBlank(merchant.getName())
                    || StringUtil.isBlank(merchant.getBusinessType())) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("名称/商户类型不能为空");
                return resp;
            }

            //校验商户名称不能重复
            Merchant merchantTemp = merchantService.getByMerchantName(merchant.getName());
            if (merchantTemp != null && !merchantTemp.getId().equals(merchant.getId())) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("错误:重复的商户名称，请修改后重新提交");
                return resp;
            }

            //处理logo截图
            Boolean logoSaveSucc = false;
            String path = "";
            String imgBase64 = merchant.getLogoBase64();
            if (imgBase64 != null && !StringUtil.isBlank(imgBase64)) {
                path = "images/logo/" + System.currentTimeMillis() + ".png";
                logoSaveSucc = FileValidCreateUtil.createBase64Img(imgBase64, DOWNLOAD_HOME + "/" + path);
            }
            if (logoSaveSucc) {
                merchant.setLogo(path);
            }

            merchant.setCuisineType(StringUtil.isBlank(merchant.getCuisineType()) ? "" : merchant.getCuisineType());
            Merchant merchant1 = merchantService.update(merchant);//新建或更新

            Long counts = merchantStoreService.getCountById(null, merchant1.getId());
            if (counts == 0) {
                //新建商户后，如果商户下没有门店自动新建一个默认店铺
                MerchantStore merchantStore = new MerchantStore();
                merchantStore.setMerchant(merchant1);
                merchantStore.setName(merchant1.getName() + "默认门店");
                merchantStore.setCode("defaultCode0000" + DateTimeUtils.toShortDateTime(new Date()));
                merchantStoreService.update(merchantStore);
            }
            resp = AjaxResponse.success();
            resp.addEntry(Constants.RESPONSE_UPDATE_RESULT, objectToEntry(merchant1));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
            resp.setErrorString("出错");
            return resp;
        }
        return resp;
    }

    @RequestMapping(value = "admin/merchant/get", method = RequestMethod.GET)
    @ResponseBody
//    @PreAuthorize("hasAnyAuthority(['MERCHANT_MANAGE','MERCHANT_STORE_R'])")
    public AjaxResponse getMerchant(@RequestParam Long id, HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        try {
            Merchant merchant1 = merchantService.findById(id);//id由前端切换商户传进来
            resp.addDataEntry(objectToEntry(merchant1));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
            resp.setErrorString("出错");
        }
        return resp;
    }

    @RequestMapping(value = {"admin/merchant/getSwitch", "shop/merchant/getSwitch"}, method = RequestMethod.GET)
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
            LOGGER.error(e.getMessage(),e);
            resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
            resp.setErrorString("出错");
        }
        return resp;
    }

    @RequestMapping(value = "admin/merchant/delete", method = RequestMethod.POST)
    @ResponseBody
//    @PreAuthorize("hasAnyAuthority('MERCHANT_MANAGE','MERCHANT_STORE_D')")
    public AjaxResponse deleteMerchant(@Valid @RequestBody Merchant merchantDelete, HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        try {
            Merchant thisSwitchMerchant = (Merchant) request.getSession().getAttribute(Constants.ADMIN_MERCHANT);
            if (thisSwitchMerchant == null) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("请先切换门店");
                return resp;
            }
            if (thisSwitchMerchant.getId() == merchantDelete.getId()) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("要删除的商户是当前切换的门店商户，不能删除");
                return resp;
            }
            List<MerchantStore> merchantStores = merchantStoreService.listByMerchantId(merchantDelete.getId());
            if (merchantStores != null && merchantStores.size() > 0) {
                //删除当前商户下所有门店，如果报错，则提示“该商户下有门店在被其他模块使用，无法删除”；如果没报错，则说明商户可以删除
                for (MerchantStore merchantStore : merchantStores) {
                    try {
                        merchantStoreService.delete(merchantStore);
                    } catch (Exception e) {
                        resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                        resp.setErrorString("该商户下'" + merchantStore.getName() + "'门店在被其他模块使用，无法删除");
                        return resp;
                    }
                }
            }
            Merchant merchant = merchantService.findById(merchantDelete.getId());
            if (merchant != null) merchantService.delete(merchant);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
            resp.setErrorString("删除出错");
        }
        return resp;
    }

    @RequestMapping(value = "admin/merchant/list", method = RequestMethod.GET)
    @ResponseBody
//    @PreAuthorize("hasAnyAuthority('MERCHANT_MANAGE','MERCHANT_STORE_R')")
    public AjaxResponse listMerchant(HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        try {
            List<Merchant> merchantList = merchantService.list();
            for (Merchant merchant : merchantList) {
                resp.addDataEntry(objectToEntry(merchant));
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
            resp.setErrorString("出错");
        }
        return resp;
    }

    @RequestMapping(value = "admin/merchant/paging", method = RequestMethod.GET)
    @ResponseBody
//    @PreAuthorize("hasAnyAuthority('PERMIT_ALL','MERCHANT_MANAGE','MERCHANT_STORE_R')")
    public AjaxPageableResponse pageMerchant(HttpServletRequest request, WhereRequest whereRequest) throws Exception {
        AjaxPageableResponse resp = new AjaxPageableResponse();
        try {
            PageResult<Merchant> pageList = merchantService.pageList(whereRequest);

            List<Merchant> merchantList = pageList.getList();
            for (Merchant merchant : merchantList) {
                resp.addDataEntry(objectToEntry(merchant));
            }
            resp.setRecordsTotal(pageList.getRecordsTotal());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
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
            LOGGER.error(e.getMessage(), e);
        }
        return resp;
    }

    @RequestMapping(value = {"admin/merchant/typeList4Select", "shop/merchant/typeList4Select"}, method = RequestMethod.GET)
    @ResponseBody
    public List getMerchantType4Select() {
        return new BusinessType().getMerchantBusinessType4Select();
    }

    @RequestMapping(value = {"admin/merchant/cuisineList4Select", "shop/merchant/cuisineList4Select"}, method = RequestMethod.GET)
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
        entry.put("cuisineType", merchant.getCuisineType());
        entry.put("cuisineTypeKey", (suisineType == null || "".equals(suisineType)) ? "" : new CuisineType().getCuisineTypeName(merchant.getCuisineType()));
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
            //id是Long包装类的比较值要用equals
            if (merchantStoreTemp != null && !merchantStoreTemp.getId().equals(merchantStore.getId())) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("错误:重复的门店码，请修改后重新提交");
                return resp;
            }

            //校验门店名称不能重复
            MerchantStore merchantStoreTemp1 = merchantStoreService.getByMerchantStoreName(merchantStore.getName());
            if (merchantStoreTemp1 != null && !merchantStoreTemp1.getId().equals(merchantStore.getId())) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("错误:重复的门店名称，请修改后重新提交");
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

            //引流关系维护
            List<MerchantStore> bindStores = Lists.newArrayList();;
            if(merchantStore.getId() != null && !("").equals(merchantStore.getId())){
                SaleCorpMerchant saleCorpMerchant = saleCorpMerchantService.findByMerchantId(merchantStore.getId());
                if (saleCorpMerchant != null && !StringUtil.isBlank(saleCorpMerchant.getRelatedMerchants())) {
                    List<Long> bindStore = JSON.parseArray(saleCorpMerchant.getRelatedMerchants(), Long.class);
                    for (Long storeId : bindStore) {
                        MerchantStore store = merchantStoreService.findById(storeId);
                        bindStores.add(store);
                    }
                }
            }
            resp = AjaxResponse.success();
            resp.addEntry(Constants.RESPONSE_UPDATE_RESULT, objectToEntryAdd(merchantStore, bindStores));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
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
            LOGGER.error(e.getMessage(), e);
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
            MerchantStore thisSwitchMerchantStore = (MerchantStore) request.getSession().getAttribute(Constants.ADMIN_STORE);
            if (thisSwitchMerchantStore == null) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("请先切换门店");
                return resp;
            }
            if (thisSwitchMerchantStore.getId() == merchantStoreDelete.getId()) {
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
            LOGGER.error(e.getMessage(), e);
            resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
            resp.setErrorString("该门店在被其他模块使用，无法删除");
        }
        return resp;
    }

//    @RequestMapping(value = {"admin/merchantStore/list", "shop/merchantStore/list"}, method = RequestMethod.GET)
//    @ResponseBody
//    public AjaxPageableResponse listMerchantStore(WhereRequest whereRequest, HttpServletRequest request) throws Exception {
//        AjaxPageableResponse resp = new AjaxPageableResponse();
//        try {
//            String path = request.getServletPath();
//            if (path.contains("/admin/")) {
//                PageResult<MerchantStore> pageList = merchantStoreService.pageListByMerchant(null, whereRequest);
//                List<MerchantStore> merchantStoreList = pageList.getList();
//                for (MerchantStore merchantStore : merchantStoreList) {
//                    resp.addDataEntry(objectToEntry(merchantStore));
//                }
//                resp.setRecordsTotal(pageList.getRecordsTotal());
//            } else if (path.contains("/shop/")) {
//                resp.setRecordsTotal(0);
//            }
//        } catch (Exception e) {
//            LOGGER.error(e.getMessage(), e);
//            resp.setErrorString("出错");
//        }
//        return resp;
//    }

    @RequestMapping(value = {"admin/merchantStore/listSwitch", "shop/merchantStore/listSwitch"}, method = RequestMethod.GET)
    @ResponseBody
    public AjaxPageableResponse listSwitchMerchantStore(WhereRequest whereRequest, HttpServletRequest request) throws Exception {
        AjaxPageableResponse resp = new AjaxPageableResponse();
        try {
            String path = request.getServletPath();
            //从用户自身和用户绑定的门店取可切换的列表
            Set<MerchantStore> storeSet = null;
            MerchantStore defaultStore = null;//用户绑定的默认门店
            if (path.contains("/admin/")) {
                Object o = request.getSession().getAttribute(Constants.ADMIN_USER);
                if(o == null ){
                    return AjaxPageableResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE,"获取用户信息错误！");
                }
                AdminUser adminUser = userService.findById(((AdminUser) o).getId());
                storeSet = adminUser.getAllMerchantStores();
                defaultStore = adminUser.getMerchantStore();
            } else if (path.contains("/shop/")) {
                Object o = request.getSession().getAttribute(Constants.CUSTOMER_USER);
                if(o == null ){
                    return AjaxPageableResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE,"获取用户信息错误！");
                }
                Customer customer = customerService.findById(((Customer)o).getId());
                storeSet = customer.getAllMerchantStores();
                defaultStore = customer.getMerchantStore();
            }
            if(defaultStore == null) {
                return AjaxPageableResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE,"用户信息错误！未绑定默认门店！");
            }
            resp.addDataEntry(objectToEntry(defaultStore));
            if(storeSet != null && storeSet.size() > 0) {
                for (MerchantStore merchantStore : storeSet) {
                    //添加列表时排除用户绑定的默认门店，使列表不重复
                    if(merchantStore.getName().equals(defaultStore.getName())){
                        continue;
                    }
                    resp.addDataEntry(objectToEntry(merchantStore));
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
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

            List<MerchantStore> merchantStoreList = merchantStoreService.pageListByMerchant(merchant.getId(), new WhereRequest()).getList();
            for (MerchantStore merchantStore : merchantStoreList) {
                resp.addDataEntry(objectToEntry(merchantStore));
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
            resp.setErrorString("出错");
        }
        return resp;
    }

    //根据切换的商户查询旗下所有门店
    @RequestMapping(value = "admin/merchantStore/pagingByMerchant", method = RequestMethod.GET)
    @ResponseBody
    public AjaxPageableResponse pagingMerchantStoreByMerchant(HttpServletRequest request, WhereRequest whereRequest) throws Exception {
        AjaxPageableResponse resp = new AjaxPageableResponse();
        try {
            //从session中取账号关联的商户信息
            Object o1 = request.getSession().getAttribute(Constants.ADMIN_MERCHANT);
            if ( o1 == null) {
                resp.setErrorString("请先切换商户");
                return resp;
            }

            Merchant merchant = (Merchant) request.getSession().getAttribute(Constants.ADMIN_MERCHANT);

            PageResult<MerchantStore> pageList = merchantStoreService.pageListByMerchant(merchant.getId(), whereRequest);

            List<MerchantStore> merchantStoreList = pageList.getList();
            for (MerchantStore merchantStore : merchantStoreList) {
                SaleCorpMerchant saleCorpMerchant = saleCorpMerchantService.findByMerchantId(merchantStore.getId());
                List<MerchantStore> bindStores = Lists.newArrayList();
                if (saleCorpMerchant != null && !StringUtil.isBlank(saleCorpMerchant.getRelatedMerchants())) {
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
            LOGGER.error(e.getMessage(), e);
            resp.setErrorString("出错");
        }
        return resp;
    }

    //根据用户绑定门店及当前切换的商户查找
    @RequestMapping(value = "admin/merchantStore/pagingByMerchantUser", method = RequestMethod.GET)
    @ResponseBody
    public AjaxPageableResponse pagingByMerchantUser(HttpServletRequest request, WhereRequest whereRequest) throws Exception {
        AjaxPageableResponse resp = new AjaxPageableResponse();
        try {
            //从session中取账号关联的商户信息
            Object o1 = request.getSession().getAttribute(Constants.ADMIN_MERCHANT);
            if ( o1 == null) {
                resp.setErrorString("请先切换商户");
                return resp;
            }
            Long thisMerchantId = ((Merchant)o1).getId();
            Object o = request.getSession().getAttribute(Constants.ADMIN_USER);
            if( o == null ) {
                return AjaxPageableResponse.failed(-1,"请先登录");
            }
            Set<MerchantStore> merchantStoreList = ((AdminUser)o).getAllMerchantStores();
            if( merchantStoreList != null && merchantStoreList.size() > 0) {
                for (MerchantStore merchantStore : merchantStoreList) {
                    Long merchantStoreId = merchantStore.getId();
                    MerchantStore merchantStoreDB = merchantStoreService.findById(merchantStoreId);
                    //只取当前切换门店所属商户下的门店
                    if( !merchantStoreDB.getMerchant().getId().equals(thisMerchantId) ) {
                        continue;
                    }
                    SaleCorpMerchant saleCorpMerchant = saleCorpMerchantService.findByMerchantId(merchantStoreId);
                    List<MerchantStore> bindStores = Lists.newArrayList();
                    if (saleCorpMerchant != null && !StringUtil.isBlank(saleCorpMerchant.getRelatedMerchants())) {
                        List<Long> bindStore = JSON.parseArray(saleCorpMerchant.getRelatedMerchants(), Long.class);
                        for (Long storeId : bindStore) {
                            MerchantStore store = merchantStoreService.findById(storeId);
                            bindStores.add(store);
                        }
                    }
                    resp.addDataEntry(objectToEntryAdd(merchantStoreDB, bindStores));
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
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
            List<MerchantStore> merchantStoreList = merchantStoreService.pageListByMerchant(merchant.getId(), new WhereRequest()).getList();
            for (MerchantStore merchantStore : merchantStoreList) {
                Map entry = new HashMap();
                entry.put("name", merchantStore.getName());
                entry.put("value", merchantStore.getId());
                resp.addDataEntry(entry);
            }
            return resp.getRows();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
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
                return AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE,"请切换门店！");
            }
            Long numFind = merchantStoreService.getCountById(id, null);
            if (numFind != 1L) {
                //抛出异常给异常处理机制
                return AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE,"要切换的商户信息错误");
            }

            Object o = request.getSession().getAttribute(Constants.ADMIN_USER);
            if(o == null ){
                return AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE,"获取用户信息错误！");
            }
            AdminUser adminUser = (AdminUser)o;
            Set<MerchantStore> storeSet = adminUser.getAllMerchantStores();
            //校验要切换的门店id是否在用户可操作门店列表里面,或者是用户自身所属的门店
            boolean isPermit = id.equals(adminUser.getMerchantStore().getId());
            if( storeSet != null && storeSet.size() > 0 ){
                for( MerchantStore store : storeSet ){
                    if( store.getId().equals(id) ){
                        isPermit = true;
                        break;
                    }
                }
            }
            if(!isPermit){
                return AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE,"没有操作该门店权限！");
            }

            MerchantStore merchantStoreOld = merchantStoreService.findById(id);
            Merchant merchantOld = merchantService.findById(merchantStoreOld.getMerchant().getId());
            //把商户信息写入session
            request.getSession().setAttribute(Constants.ADMIN_STORE, merchantStoreOld);
            request.getSession().setAttribute(Constants.ADMIN_MERCHANT, merchantOld);
            resp.addDataEntry(objectToEntry(merchantStoreOld));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
            resp.setErrorString("切换出错");
        }
        return resp;
    }

    @RequestMapping(value = {"admin/merchantStore/getSwitch", "shop/merchantStore/getSwitch"}, method = RequestMethod.GET)
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
            Map entry = objectToEntry(merchantStore1);

            //因为懒加载可能导致修改商户后，关联查询出来点商户信息没更新，所以强制重新查询一次
            Merchant merchant = merchantService.findById(merchantStore1.getMerchant().getId());
            entry.put("merchant", merchant);
            resp.addDataEntry(entry);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
            resp.setErrorString("出错");
        }
        return resp;
    }

    @RequestMapping(value = "admin/merchantStore/getAllStoreExceptSelf", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResponse getAllStoreExceptSelf() {
        try {
            AjaxResponse resp = new AjaxResponse();
            List<MerchantStore> result = merchantStoreService.list();
            for (MerchantStore merchantStore : result) {
                resp.addDataEntry(objectToEntry(merchantStore));
            }
            return resp;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
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
    private Map objectToEntryAdd(MerchantStore merchantStore, List<MerchantStore> bindStores) {
        Map entry = objectToEntry(merchantStore);
        entry.put("bindStores", bindStores);
        return entry;
    }

}
