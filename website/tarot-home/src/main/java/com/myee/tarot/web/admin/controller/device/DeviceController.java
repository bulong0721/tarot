package com.myee.tarot.web.admin.controller.device;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.myee.tarot.catalog.domain.*;
import com.myee.tarot.catalog.type.ProductType;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.exception.ServiceException;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.ajax.AjaxPageableResponse;
import com.myee.tarot.core.util.ajax.AjaxResponse;
import com.myee.tarot.device.service.DeviceAttributeService;
import com.myee.tarot.device.service.DeviceService;
import com.myee.tarot.device.service.DeviceUsedAttributeService;
import com.myee.tarot.device.service.DeviceUsedService;
import com.myee.tarot.merchant.domain.MerchantStore;
import com.myee.tarot.device.service.ProductUsedAttributeService;
import com.myee.tarot.device.service.ProductUsedService;
import com.myee.tarot.web.util.StringUtil;
import com.myee.tarot.web.util.ValidatorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/31.
 */

@Controller
public class DeviceController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceController.class);

    @Autowired
    private DeviceService deviceService;
    @Autowired
    private DeviceAttributeService deviceAttributeService;
    @Autowired
    private DeviceUsedService deviceUsedService;
    @Autowired
    private DeviceUsedAttributeService deviceUsedAttributeService;
    @Autowired
    private ProductUsedService productUsedService;
    @Autowired
    private ProductUsedAttributeService productUsedAttributeService;

    @RequestMapping(value = "device/paging", method = RequestMethod.GET)
    @ResponseBody
    public AjaxPageableResponse pageDevice(Model model, HttpServletRequest request, PageRequest pageRequest) {
        AjaxPageableResponse resp = new AjaxPageableResponse();
        try {
            PageResult<Device> pageList = deviceService.pageList(pageRequest);
            List<Device> deviceList = pageList.getList();
            for (Device device : deviceList) {
                resp.addDataEntry(objectToEntry(device));
            }
            resp.setRecordsTotal(pageList.getRecordsTotal());
        } catch (Exception e) {
            LOGGER.error("Error while paging products", e);
        }
        return resp;
    }

    //把类转换成entry返回给前端，解耦和
    private Map objectToEntry(Device device) {
        Map entry = new HashMap();
        entry.put("id",device.getId());
        entry.put("name",device.getName());
        entry.put("versionNum",device.getVersionNum());
        entry.put("description",device.getDescription());
        List<AttributeDTO> attributeDTOs = Lists.transform(device.getAttributes(), new Function<DeviceAttribute, AttributeDTO>() {
            @Nullable
            @Override
            public AttributeDTO apply(DeviceAttribute input) {
                return new AttributeDTO(input);
            }
        });
        entry.put("attributes", attributeDTOs);
        return entry;
    }

    @RequestMapping(value = "device/update", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse updateDevice(@Valid @RequestBody Device device, HttpServletRequest request) {
        try {
            AjaxResponse resp = new AjaxResponse();
            device = deviceService.update(device);
            resp = AjaxResponse.success();
            resp.addEntry("updateResult", objectToEntry(device));
            return resp;
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return AjaxResponse.failed(-1);
    }

    @RequestMapping(value = "device/list", method = RequestMethod.GET)
    @ResponseBody
    public List deviceList(HttpServletRequest request) {
        AjaxResponse resp = new AjaxResponse();
        try {
            List<Device> deviceList = deviceService.list();
            for (Device device : deviceList) {
                Map entry = new HashMap();
                entry.put("name",device.getName());
                entry.put("value",device.getId());
                resp.addDataEntry(entry);
            }
            return resp.getRows();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    @RequestMapping(value = "device/delete", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse deleteDevice(@Valid @RequestBody Device device, HttpServletRequest request) {
        try {
            AjaxResponse resp = new AjaxResponse();
            if (request.getSession().getAttribute(Constants.ADMIN_STORE) == null) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("请先切换门店");
                return resp;
            }
            //先手动删除所有该对象关联的属性，再删除该对象。因为关联关系是属性多对一该对象，关联字段放在属性表里，不能通过删对象级联删除属性。
            deviceAttributeService.deleteByDeviceId(device.getId());

            Device deviceUsed1 = deviceService.findById(device.getId());
            deviceService.delete(deviceUsed1);
            return AjaxResponse.success();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return AjaxResponse.failed(-1);
    }

    @RequestMapping(value = "device/attribute/save", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse saveAttribute(@ModelAttribute Device device, @Valid @RequestBody DeviceAttribute attribute, HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        DeviceAttribute entity = attribute;
        if (null != attribute.getId()) {
            entity = deviceAttributeService.findById(attribute.getId());
            entity.setName(attribute.getName());
            entity.setValue(attribute.getValue());
        } else {
            entity.setDevice(device);
        }
        entity = deviceAttributeService.update(entity);
        entity.setDevice(null);
        resp = AjaxResponse.success();
        resp.addEntry("updateResult", entity);
        return resp;
    }

    @RequestMapping(value = "device/attribute/delete", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse deleteAttribute(@Valid @RequestBody DeviceAttribute attribute,  HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        try {
            if (null != attribute.getId()) {
                DeviceAttribute entity = deviceAttributeService.findById(attribute.getId());
                deviceAttributeService.delete(entity);
            }
            return AjaxResponse.success();
        } catch (Exception e) {
            e.printStackTrace();
            resp.setErrorString("删除设备类型属性异常");
            LOGGER.error("Error delete productAttributes", e);
        }
        return resp;

    }


    /**
     * devicedUsed--------------------------------------------------------------------------
     */
    @RequestMapping(value = "device/used/paging", method = RequestMethod.GET)
    @ResponseBody
    public AjaxPageableResponse pageDeviceUsed(Model model, HttpServletRequest request, PageRequest pageRequest) {
        AjaxPageableResponse resp = new AjaxPageableResponse();
        try {
            if (request.getSession().getAttribute(Constants.ADMIN_STORE) == null) {
                resp.setErrorString("请先切换门店");
                return resp;
            }
            MerchantStore merchantStore1 = (MerchantStore) request.getSession().getAttribute(Constants.ADMIN_STORE);

            PageResult<DeviceUsed> pageResult = deviceUsedService.pageByStore(merchantStore1.getId(), pageRequest);
            List<DeviceUsed> deviceUsedList = pageResult.getList();
            for (DeviceUsed deviceUsed : deviceUsedList) {
                resp.addDataEntry(objectToEntry(deviceUsed));
            }
            resp.setRecordsTotal(pageResult.getRecordsTotal());
        } catch (Exception e) {
            LOGGER.error("Error while paging products", e);
            resp.setErrorString("出错");
        }
        return resp;
    }

    @RequestMapping(value = "device/used/listByStoreId", method = RequestMethod.GET)
    public
    @ResponseBody
    AjaxPageableResponse deviceUsedListByStoreId( HttpServletRequest request, PageRequest pageRequest) {
        AjaxPageableResponse resp = new AjaxPageableResponse();
        try {
            if (request.getSession().getAttribute(Constants.ADMIN_STORE) == null) {
                resp.setErrorString("请先切换门店");
                return resp;
            }
            MerchantStore merchantStore1 = (MerchantStore) request.getSession().getAttribute(Constants.ADMIN_STORE);

            pageRequest.setCount(-1);//不分页，查询所有结果
            PageResult<DeviceUsed> pageList = deviceUsedService.pageByStore(merchantStore1.getId(), pageRequest);
            List<DeviceUsed> deviceUsedList = pageList.getList();
            for (DeviceUsed deviceUsed : deviceUsedList) {
                resp.addDataEntry(objectToEntry(deviceUsed));
            }
            resp.setRecordsTotal(pageList.getRecordsTotal());
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Error while paging products", e);
        }
        return resp;
    }


    @RequestMapping(value = "device/used/list4Select", method = RequestMethod.GET)
    @ResponseBody
    public List deviceUsedList(HttpServletRequest request) {
        AjaxResponse resp = new AjaxResponse();
        try {
            if (request.getSession().getAttribute(Constants.ADMIN_STORE) == null) {
                resp.setErrorString("请先切换门店");
                return null;
            }
            MerchantStore merchantStore1 = (MerchantStore) request.getSession().getAttribute(Constants.ADMIN_STORE);

            PageRequest pageRequest = new PageRequest();
            pageRequest.setCount(-1);//不分页，查询所有结果
            PageResult<DeviceUsed> pageList = deviceUsedService.pageByStore(merchantStore1.getId(), pageRequest);
            List<DeviceUsed> deviceUsedList = pageList.getList();
            for (DeviceUsed deviceUsed : deviceUsedList) {
                Map entry = new HashMap();
                entry.put("name",deviceUsed.getName());
                entry.put("value",deviceUsed.getBoardNo());
                resp.addDataEntry(entry);
            }
            return resp.getRows();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "device/used/update", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse saveUsedProduct(@Valid @RequestBody DeviceUsed deviceUsed,@RequestParam(value = "autoStart")Long autoStart,@RequestParam(value = "autoEnd")Long autoEnd, HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        try {
            if (request.getSession().getAttribute(Constants.ADMIN_STORE) == null) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("请先切换门店");
                return resp;
            }
            if(autoEnd != null && autoStart !=null && autoEnd < autoStart){
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("结束编号不能小于开始编号");
                return resp;
            }
            if(StringUtil.isNullOrEmpty(deviceUsed.getBoardNo())){
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("主板编号不能为空");
                return resp;
            }
            if(deviceUsed.getPhone() != null && !validatePhone(deviceUsed.getPhone())){
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE, "请输入正确的手机号！");
                return resp;
            }
            MerchantStore merchantStore1 = (MerchantStore) request.getSession().getAttribute(Constants.ADMIN_STORE);

            List<Object> updateResult = new ArrayList<Object>();
            deviceUsed.setStore(merchantStore1);
            //校验主板编号
            DeviceUsed dU = deviceUsedService.getStoreInfoByMbCode(deviceUsed.getBoardNo());
            if (dU != null && dU.getId() != deviceUsed.getId()) { //编辑时排除当前设备
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("已存在的主板编号");
                return resp;
            }
            if(autoEnd != null && autoStart !=null &&  autoEnd >= 0 && autoStart >= 0 ){//批量新增
                String commonName = deviceUsed.getName();
                String commonBoardNo = deviceUsed.getBoardNo();
                for(Long i=autoStart;i < autoEnd+1;i++){
                    DeviceUsed deviceUsedResult = new DeviceUsed();
                    deviceUsed.setName(commonName + i);
                    deviceUsed.setBoardNo(String.valueOf(System.currentTimeMillis())+"auto" +commonBoardNo+ i);
                    deviceUsedResult = deviceUsedService.update(deviceUsed);
                    updateResult.add(objectToEntry(deviceUsedResult));
                }
            }
            else {//单个新增或修改
                deviceUsed = deviceUsedService.update(deviceUsed);
                updateResult.add(objectToEntry(deviceUsed));
            }

            resp = AjaxResponse.success();
            resp.addEntry("updateResult", updateResult);
        } catch (ServiceException e) {
            e.printStackTrace();
            resp = AjaxResponse.failed(-1);
        }
        return resp;
    }

    /**
     * 当手机号为空或“”时，说明用户不填，也是可以的
     * @param phone
     * @return true验证通过，false验证失败
     */
    private boolean validatePhone(String phone) {
        if(phone == null || phone.equals("")){
            return true;
        }

        String phones[]= phone.split(",");
        for(int i=0;i<phones.length;i++){
            if(!ValidatorUtil.isMobile(phones[i])){
                return false;
            }

        }
        return true;
    }

    @RequestMapping(value = "device/used/bindProductUsed", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse deviceUsedBindProductUsed(@RequestParam(value = "bindString") String bindString,@RequestParam(value = "deviceUsedId") Long deviceUsedId, HttpServletRequest request) {
        try {
            AjaxResponse resp = new AjaxResponse();
            List<Long> bindList = JSON.parseArray(bindString, Long.class);
            DeviceUsed deviceUsed = deviceUsedService.findById(deviceUsedId);
            if (deviceUsed == null) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("参数不正确");
                return resp;
            }
            List<ProductUsed> productUsedList = productUsedService.listByIDs(bindList);
            deviceUsed.setProductUsed(productUsedList);

            deviceUsed = deviceUsedService.update(deviceUsed);
            resp = AjaxResponse.success();
            resp.addEntry("updateResult", objectToEntry(deviceUsed));
            return resp;
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return AjaxResponse.failed(-1);
    }

    @RequestMapping(value = "device/used/delete", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse deleteDeviceUsed(@Valid @RequestBody DeviceUsed deviceUsed, HttpServletRequest request) {
        try {
            AjaxResponse resp = new AjaxResponse();
            if (request.getSession().getAttribute(Constants.ADMIN_STORE) == null) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("请先切换门店");
                return resp;
            }
            //先手动删除所有该对象关联的属性，再删除该对象。因为关联关系是属性多对一该对象，关联字段放在属性表里，不能通过删对象级联删除属性。
            deviceUsedAttributeService.deleteByDeviceUsedId(deviceUsed.getId());

            DeviceUsed deviceUsed1 = deviceUsedService.findById(deviceUsed.getId());
            deviceUsedService.delete(deviceUsed1);
            return AjaxResponse.success();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return AjaxResponse.failed(-1);
    }

    //把类转换成entry返回给前端，解耦和
    private Map objectToEntry(DeviceUsed deviceUsed) {
        Map entry = new HashMap();
        entry.put("id",deviceUsed.getId());
        entry.put("name",deviceUsed.getName());
        entry.put("heartbeat",deviceUsed.getHeartbeat());
        entry.put("boardNo",deviceUsed.getBoardNo());
        entry.put("deviceNum", deviceUsed.getDeviceNum());
        entry.put("description",deviceUsed.getDescription());
        entry.put("phone",deviceUsed.getPhone());

        if(deviceUsed.getProductUsed() != null ){
            for(ProductUsed productUsed : deviceUsed.getProductUsed()){
                productUsed.setDeviceUsed(null);
                productUsed.setAttributes(null);
            }
        }
        entry.put("productUsedList",deviceUsed.getProductUsed());

        //把device的属性作为公共属性传到前端
//        List<AttributeDTO> attributeDTOs = Lists.transform(deviceUsed.getDevice().getAttributes(), new Function<DeviceAttribute, AttributeDTO>() {
//            @Nullable
//            @Override
//            public AttributeDTO apply(DeviceAttribute input) {
//                input.setId(0L);//公共属性的id设为0，前端不显示编辑和删除按钮
//                return new AttributeDTO(input);
//            }
//        });

        //清空关联的设备类型的属性，防止前端无限循环
        deviceUsed.getDevice().setAttributes(null);
        entry.put("device", deviceUsed.getDevice());

        //把deviceUsed的私有属性传到前端
        List<AttributeDTO> attributePrivateDTOs = Lists.transform(deviceUsed.getAttributes(), new Function<DeviceUsedAttribute, AttributeDTO>() {
            @Nullable
            @Override
            public AttributeDTO apply(DeviceUsedAttribute input) {
                return new AttributeDTO(input);
            }
        });
//        attributeDTOs.addAll(attributePrivateDTOs);
//        entry.put("attributes", attributeDTOs);
        entry.put("attributes", attributePrivateDTOs);
        return entry;
    }

    @RequestMapping(value = "device/used/attribute/save", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse saveAttribute(@ModelAttribute DeviceUsed deviceUsed, @Valid @RequestBody DeviceUsedAttribute attribute, HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        DeviceUsedAttribute entity = attribute;
        if (null != attribute.getId()) {
            entity = deviceUsedAttributeService.findById(attribute.getId());
            entity.setName(attribute.getName());
            entity.setValue(attribute.getValue());
        } else {
            entity.setDeviceUsed(deviceUsed);
        }
        entity = deviceUsedAttributeService.update(entity);

        entity.setDeviceUsed(null);
        resp = AjaxResponse.success();
        resp.addEntry("updateResult", entity);
        return resp;
    }

    @RequestMapping(value = "device/used/attribute/delete", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse deleteAttribute(@Valid @RequestBody DeviceUsedAttribute attribute,  HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        try {
            if (null != attribute.getId()) {
                DeviceUsedAttribute entity = deviceUsedAttributeService.findById(attribute.getId());
                deviceUsedAttributeService.delete(entity);
            }
            return AjaxResponse.success();
        } catch (Exception e) {
            e.printStackTrace();
            resp.setErrorString("删除设备属性异常");
            LOGGER.error("Error delete productAttributes", e);
        }
        return resp;

    }


    @RequestMapping(value = "device/used/attribute/listByProductId", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResponse listByDeviceId(@RequestParam Long parentId, HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        try {
            if (StringUtil.isNullOrEmpty(parentId.toString())) {
                resp.setErrorString("参数不能为空");
                return resp;
            }
            //查询设备实例的设备类型的公共属性
            DeviceUsed deviceUsed = deviceUsedService.findById(parentId);
            List<DeviceAttribute> commonAttributeList = deviceAttributeService.listByDeviceId(deviceUsed.getDevice().getId());
            for(DeviceAttribute commonAttribute :commonAttributeList){
                Map entry = new HashMap();
                entry.put("id", 0);//公共属性的id设为0，前端不显示编辑和删除按钮
                entry.put("name", commonAttribute.getName());
                entry.put("value", commonAttribute.getValue());
                resp.addDataEntry(entry);
            }

            //查询设备实例的私有属性
            List<DeviceUsedAttribute> attributeList = deviceUsedAttributeService.listByDeviceUsedId(parentId);
            for (DeviceUsedAttribute attribute : attributeList) {
                Map entry = new HashMap();
//                List<DeviceAttribute> attributes = new ArrayList<DeviceAttribute>();
                entry.put("id", attribute.getId());
                entry.put("name", attribute.getName());
                entry.put("value", attribute.getValue());
                resp.addDataEntry(entry);
            }
        } catch (Exception e) {
            LOGGER.error("Error while paging productAttributes", e);
        }
        return resp;

    }

    /**
     * productUsed------------------------------------------------------------------------------
     */
    @RequestMapping(value = "product/used/paging", method = RequestMethod.GET)
    public
    @ResponseBody
    AjaxPageableResponse pageUsers(Model model, HttpServletRequest request, PageRequest pageRequest) {
        AjaxPageableResponse resp = new AjaxPageableResponse();
        String currentUser = request.getRemoteUser();
        try {
            if (request.getSession().getAttribute(Constants.ADMIN_STORE) == null) {
                resp.setErrorString("请先切换门店");
                return resp;
            }
            MerchantStore merchantStore1 = (MerchantStore) request.getSession().getAttribute(Constants.ADMIN_STORE);

            PageResult<ProductUsed> pageList = productUsedService.pageByStore(merchantStore1.getId(), pageRequest);
            List<ProductUsed> productUsedList = pageList.getList();
            for (ProductUsed productUsed : productUsedList) {
                resp.addDataEntry(objectToEntry(productUsed));
            }
            resp.setRecordsTotal(pageList.getRecordsTotal());
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Error while paging products", e);
        }
        return resp;
    }

    @RequestMapping(value = "product/used/listByStoreId", method = RequestMethod.GET)
    public
    @ResponseBody
    AjaxPageableResponse productUsedlistByStoreId( HttpServletRequest request, PageRequest pageRequest) {
        AjaxPageableResponse resp = new AjaxPageableResponse();
        String currentUser = request.getRemoteUser();
        try {
            if (request.getSession().getAttribute(Constants.ADMIN_STORE) == null) {
                resp.setErrorString("请先切换门店");
                return resp;
            }
            MerchantStore merchantStore1 = (MerchantStore) request.getSession().getAttribute(Constants.ADMIN_STORE);

            pageRequest.setCount(-1);//不分页，查询所有结果
            PageResult<ProductUsed> pageList = productUsedService.pageByStore(merchantStore1.getId(), pageRequest);
            List<ProductUsed> productUsedList = pageList.getList();
            for (ProductUsed productUsed : productUsedList) {
                resp.addDataEntry(objectToEntry(productUsed));
            }
            resp.setRecordsTotal(pageList.getRecordsTotal());
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Error while paging products", e);
        }
        return resp;
    }

    @RequestMapping(value = "product/used/save", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse saveUsedProduct(@Valid @RequestBody ProductUsed productUsed,@RequestParam(value = "autoStart")Long autoStart,@RequestParam(value = "autoEnd")Long autoEnd, HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        try {
            if (request.getSession().getAttribute(Constants.ADMIN_STORE) == null) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("请先切换门店");
                return resp;
            }
            if(autoEnd != null && autoStart !=null && autoEnd < autoStart){
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("结束编号不能小于开始编号");
                return resp;
            }
            MerchantStore merchantStore1 = (MerchantStore) request.getSession().getAttribute(Constants.ADMIN_STORE);

            List<Object> updateResult = new ArrayList<Object>();
            productUsed.setStore(merchantStore1);
            if(autoEnd != null && autoStart !=null &&  autoEnd >= 0 && autoStart >= 0 ){//批量新增
                String commonName = productUsed.getName();
                for(Long i=autoStart;i < autoEnd+1;i++){
                    ProductUsed productUsedResult = new ProductUsed();
                    productUsed.setCode(i + "");
                    productUsedResult = productUsedService.update(productUsed);
                    updateResult.add(objectToEntry(productUsedResult));
                }
            }
            else {//单个新增或修改
                productUsed = productUsedService.update(productUsed);
                updateResult.add(objectToEntry(productUsed));
            }

            resp = AjaxResponse.success();
            resp.addEntry("updateResult", updateResult);
        } catch (ServiceException e) {
            e.printStackTrace();
            resp = AjaxResponse.failed(-1);
        }
        return resp;
    }

    @RequestMapping(value = "product/used/bindDeviceUsed", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse productUsedBindDeviceUsed(@RequestParam(value = "bindString") String bindString,@RequestParam(value = "productUsedId") Long productUsedId, HttpServletRequest request) {
        try {
            AjaxResponse resp = new AjaxResponse();
            List<Long> bindList = JSON.parseArray(bindString, Long.class);
            ProductUsed productUsed = productUsedService.findById(productUsedId);
            if (productUsed == null) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("参数不正确");
                return resp;
            }
            List<DeviceUsed> deviceUsedList = deviceUsedService.listByIDs(bindList);
            productUsed.setDeviceUsed(deviceUsedList);

            productUsed = productUsedService.update(productUsed);
            resp = AjaxResponse.success();
            resp.addEntry("updateResult", objectToEntry(productUsed));
            return resp;
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return AjaxResponse.failed(-1);
    }

    @RequestMapping(value = "product/used/delete", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse deleteProductUsed(@Valid @RequestBody ProductUsed productUsed, HttpServletRequest request) {
        try {
            AjaxResponse resp = new AjaxResponse();
            if (request.getSession().getAttribute(Constants.ADMIN_STORE) == null) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("请先切换门店");
                return resp;
            }
            //先手动删除所有该对象关联的属性，再删除该对象。因为关联关系是属性多对一该对象，关联字段放在属性表里，不能通过删对象级联删除属性。
            productUsedAttributeService.deleteByProductUsedId(productUsed.getId());

            ProductUsed productUsed1 = productUsedService.findById(productUsed.getId());
            productUsedService.delete(productUsed1);
            return AjaxResponse.success();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return AjaxResponse.failed(-1);
    }

    //把类转换成entry返回给前端，解耦和
    private Map objectToEntry(ProductUsed productUsed) {
        Map entry = new HashMap();
        entry.put("id", productUsed.getId());
        entry.put("code", productUsed.getCode());
        entry.put("name", productUsed.getName());
        entry.put("type", productUsed.getType());
        entry.put("productNum", productUsed.getProductNum());
        entry.put("description", productUsed.getDescription());
        if(productUsed.getDeviceUsed() != null ){
            for(DeviceUsed deviceUsed : productUsed.getDeviceUsed()){
                deviceUsed.setProductUsed(null);
                deviceUsed.setAttributes(null);
                deviceUsed.getDevice().setAttributes(null);
            }
        }
        entry.put("deviceUsedList", productUsed.getDeviceUsed());
        List<AttributeDTO> attributeDTOs = Lists.transform(productUsed.getAttributes(), new Function<ProductUsedAttribute, AttributeDTO>() {
            @Nullable
            @Override
            public AttributeDTO apply(ProductUsedAttribute input) {
                return new AttributeDTO(input);
            }
        });
        entry.put("attributes", attributeDTOs);
        return entry;
    }

    @RequestMapping(value = "product/attribute/save", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse saveAttribute(@ModelAttribute ProductUsed product, @Valid @RequestBody ProductUsedAttribute attribute, HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        ProductUsedAttribute entity = attribute;
        if (null != attribute.getId()) {
            entity = productUsedAttributeService.findById(attribute.getId());
            entity.setName(attribute.getName());
            entity.setValue(attribute.getValue());
        } else {
            entity.setProductUsed(product);
        }
        entity = productUsedAttributeService.update(entity);

//        entity.getProductUsed().setAttributes(null);
        entity.setProductUsed(null);
        resp = AjaxResponse.success();
        resp.addEntry("updateResult", entity);
        return resp;
    }

    @RequestMapping(value = "product/attribute/delete", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse deleteAttributeProduct(@Valid @RequestBody ProductUsedAttribute attribute,  HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        try {
            if (null != attribute.getId()) {
                ProductUsedAttribute entity = productUsedAttributeService.findById(attribute.getId());
                productUsedAttributeService.delete(entity);
            }
            return AjaxResponse.success();
        } catch (Exception e) {
            e.printStackTrace();
            resp.setErrorString("删除设备组属性异常");
            LOGGER.error("Error delete productAttributes", e);
        }
        return resp;

    }

    @RequestMapping(value = "product/type/productOpts", method = RequestMethod.GET)
    public
    @ResponseBody
    List<ProductType> productUsedType(Model model, HttpServletRequest request) {
        return ProductType.getProductTypeList();
    }

}
