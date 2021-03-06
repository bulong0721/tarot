package com.myee.tarot.web.device.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.myee.djinn.remoting.common.RemotingHelper;
import com.myee.djinn.rpc.bootstrap.ServerBootstrap;
import com.myee.tarot.catalog.domain.*;
import com.myee.tarot.catalog.type.ProductType;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.exception.ServiceException;
import com.myee.tarot.core.util.*;
import com.myee.tarot.core.util.ajax.AjaxPageableResponse;
import com.myee.tarot.core.util.ajax.AjaxResponse;
import com.myee.tarot.catalog.service.DeviceAttributeService;
import com.myee.tarot.catalog.service.DeviceService;
import com.myee.tarot.catalog.service.DeviceUsedAttributeService;
import com.myee.tarot.catalog.service.DeviceUsedService;
import com.myee.tarot.merchant.domain.MerchantStore;
import com.myee.tarot.catalog.service.ProductUsedAttributeService;
import com.myee.tarot.catalog.service.ProductUsedService;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	@Autowired
	private ServerBootstrap serverBootstrap;

    @RequestMapping(value = {"admin/device/paging", "shop/device/paging"}, method = RequestMethod.GET)
    @ResponseBody
    public AjaxPageableResponse pageDevice(Model model, HttpServletRequest request, WhereRequest whereRequest) {
        AjaxPageableResponse resp = new AjaxPageableResponse();
        try {
            PageResult<Device> pageList = deviceService.pageList(whereRequest);
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
        entry.put("id", device.getId());
        entry.put("name", device.getName());
        entry.put("versionNum", device.getVersionNum());
        entry.put("description", device.getDescription());
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

    @RequestMapping(value = {"admin/device/update", "shop/device/update"}, method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse updateDevice(@Valid @RequestBody Device device, HttpServletRequest request) {
        try {
            AjaxResponse resp;
            Object o = request.getSession().getAttribute(Constants.ADMIN_STORE);
            if (o == null) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("请先切换门店");
                return resp;
            }
            device = deviceService.update(device);
            resp = AjaxResponse.success();
            resp.addEntry(Constants.RESPONSE_UPDATE_RESULT, objectToEntry(device));
            return resp;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return AjaxResponse.failed(-1, "失败");
        }

    }

    @RequestMapping(value = {"admin/device/list", "shop/device/list"}, method = RequestMethod.GET)
    @ResponseBody
    public List deviceList(HttpServletRequest request) {
        AjaxResponse resp = new AjaxResponse();
        try {
            List<Device> deviceList = deviceService.list();
            for (Device device : deviceList) {
                Map entry = new HashMap();
                entry.put("name", device.getName());
                entry.put("value", device.getId());
                resp.addDataEntry(entry);
            }
            return resp.getRows();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }


    @RequestMapping(value = {"admin/device/delete", "shop/device/delete"}, method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse deleteDevice(@Valid @RequestBody Device device, HttpServletRequest request) {
        try {
            AjaxResponse resp;
            Object o = request.getSession().getAttribute(Constants.ADMIN_STORE);
            if (o == null) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("请先切换门店");
                return resp;
            }
            //先手动删除所有该对象关联的属性，再删除该对象。因为关联关系是属性多对一该对象，关联字段放在属性表里，不能通过删对象级联删除属性。
            deviceAttributeService.deleteByDeviceId(device.getId());

            Device deviceUsed1 = deviceService.findById(device.getId());
            deviceService.delete(deviceUsed1);
            return AjaxResponse.success();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return AjaxResponse.failed(-1, "在其他地方被使用，无法删除");
        }

    }

    @RequestMapping(value = {"admin/device/attribute/save", "shop/device/attribute/save"}, method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse saveAttribute(@ModelAttribute Device device, @Valid @RequestBody DeviceAttribute attribute, HttpServletRequest request) throws Exception {
        AjaxResponse resp;
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
        resp.addEntry(Constants.RESPONSE_UPDATE_RESULT, entity);
        return resp;
    }

    @RequestMapping(value = {"admin/device/attribute/delete", "shop/device/attribute/delete"}, method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse deleteAttribute(@Valid @RequestBody DeviceAttribute attribute, HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        try {
            if (null != attribute.getId()) {
                DeviceAttribute entity = deviceAttributeService.findById(attribute.getId());
                deviceAttributeService.delete(entity);
            }
            return AjaxResponse.success();
        } catch (Exception e) {
            resp.setErrorString("删除设备类型属性异常");
            LOGGER.error("Error delete productAttributes", e);
        }
        return resp;

    }


    /**
     * devicedUsed--------------------------------------------------------------------------
     */
    @RequestMapping(value = {"admin/device/used/paging", "shop/device/used/paging"}, method = RequestMethod.GET)
    @ResponseBody
    public AjaxPageableResponse pageDeviceUsed(Model model, HttpServletRequest request, WhereRequest whereRequest) {
        AjaxPageableResponse resp = new AjaxPageableResponse();
        try {
            Object o = request.getSession().getAttribute(Constants.ADMIN_STORE);
            if (o == null) {
                resp.setErrorString("请先切换门店");
                return resp;
            }
            MerchantStore merchantStore1 = (MerchantStore) o;

            PageResult<DeviceUsed> pageResult = deviceUsedService.pageByStore(merchantStore1.getId(), whereRequest);
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

    @RequestMapping(value = {"admin/device/used/listByStoreId", "shop/device/used/listByStoreId"}, method = RequestMethod.GET)
    public
    @ResponseBody
    AjaxPageableResponse deviceUsedListByStoreId(HttpServletRequest request, WhereRequest whereRequest) {
        AjaxPageableResponse resp = new AjaxPageableResponse();
        try {
            Object o = request.getSession().getAttribute(Constants.ADMIN_STORE);
            if (o == null) {
                resp.setErrorString("请先切换门店");
                return resp;
            }
            MerchantStore merchantStore1 = (MerchantStore) o;

            whereRequest.setCount(-1);//不分页，查询所有结果
            PageResult<DeviceUsed> pageList = deviceUsedService.pageByStore(merchantStore1.getId(), whereRequest);
            List<DeviceUsed> deviceUsedList = pageList.getList();
            for (DeviceUsed deviceUsed : deviceUsedList) {
                resp.addDataEntry(objectToEntry(deviceUsed));
            }
            resp.setRecordsTotal(pageList.getRecordsTotal());
        } catch (Exception e) {
            LOGGER.error("Error while paging products", e);
        }
        return resp;
    }


    @RequestMapping(value = {"admin/device/used/list4Select", "shop/device/used/list4Select"}, method = RequestMethod.GET)
    @ResponseBody
    public List deviceUsedList(HttpServletRequest request) {
        AjaxResponse resp = new AjaxResponse();
        try {
            Object o = request.getSession().getAttribute(Constants.ADMIN_STORE);
            if (o == null) {
                resp.setErrorString("请先切换门店");
                return null;
            }
            MerchantStore merchantStore1 = (MerchantStore) o;

            WhereRequest whereRequest = new WhereRequest();
            whereRequest.setCount(-1);//不分页，查询所有结果
            PageResult<DeviceUsed> pageList = deviceUsedService.pageByStore(merchantStore1.getId(), whereRequest);
            List<DeviceUsed> deviceUsedList = pageList.getList();
            for (DeviceUsed deviceUsed : deviceUsedList) {
                Map entry = new HashMap();
                entry.put("name", deviceUsed.getName());
                entry.put("value", deviceUsed.getBoardNo());
                resp.addDataEntry(entry);
            }
            return resp.getRows();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
        }
        return null;
    }

    @RequestMapping(value = {"admin/device/used/update", "shop/device/used/update"}, method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse saveUsedProduct(@Valid @RequestBody DeviceUsed deviceUsed, @RequestParam(value = "autoStart") Long autoStart, @RequestParam(value = "autoEnd") Long autoEnd, HttpServletRequest request) throws Exception {
        AjaxResponse resp;
        try {
            Object o = request.getSession().getAttribute(Constants.ADMIN_STORE);
            if (o == null) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("请先切换门店");
                return resp;
            }
            if (deviceUsed.getDevice().getId() == null || StringUtil.isNullOrEmpty(String.valueOf(deviceUsed.getDevice().getId()))) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE, "设备类型不能为空");
                return resp;
            }
            Device device = deviceService.findById(deviceUsed.getDevice().getId());
            if (device == null) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE, "设备类型错误");
                return resp;
            }
            deviceUsed.setDevice(device);
            if (autoEnd != null && autoStart != null && autoEnd < autoStart) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("结束编号不能小于开始编号");
                return resp;
            }
            String boardNo = deviceUsed.getBoardNo();
            if (StringUtil.isNullOrEmpty(boardNo)) {
                return AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE,"主板编号不能为空");
            }
            if (!isBoardNoOk(boardNo)) {
                return AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE,"主板编号不能包含-_#以外的特殊字符,60字以内");
            }
            //校验主板编号
            if (autoEnd != null && autoStart != null) {//批量校验主板编号不能重复
                //....预留看需求是否要验证
            } else {//单个校验主板编号不能重复
                DeviceUsed dU = deviceUsedService.getByBoardNo(boardNo);
                if (dU != null && !dU.getId().equals(deviceUsed.getId())) { //编辑时排除当前设备
                    resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                    resp.setErrorString("门店" + dU.getStore().getName() + "已存在此主板编号，请更换主板编号");
                    return resp;
                }
            }

            //如果手机号不为空，则校验手机号
            if (deviceUsed.getPhone() != null && !ValidatorUtil.isMultiMobile(deviceUsed.getPhone())) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE, "请输入正确的手机号！");
                return resp;
            }

            MerchantStore merchantStore1 = (MerchantStore) o;

            Long storeId = merchantStore1.getId();
            String deviceUsedName = deviceUsed.getName();
            if (storeId != null && !StringUtil.isNullOrEmpty(deviceUsedName)) {
                DeviceUsed deviceUsedDB = deviceUsedService.findByStoreIdAndName(storeId, deviceUsedName);
                if (deviceUsedDB != null && !deviceUsedDB.getId().equals(deviceUsed.getId())) {
                    resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                    resp.setErrorString("设备名称不能重复");
                    return resp;
                }
            }
            List<Object> updateResult = new ArrayList<Object>();
            deviceUsed.setStore(merchantStore1);

            if (autoEnd != null && autoStart != null && autoEnd >= 0 && autoStart >= 0) {//批量新增
                String commonName = deviceUsed.getName();
                String commonBoardNo = boardNo;
                for (Long i = autoStart; i < autoEnd + 1; i++) {
                    try {
                        //重复主板编号的不添加
                        DeviceUsed temp = deviceUsedService.getByBoardNo(commonBoardNo + "-" + i + "#");
                        if (temp != null) {
                            continue;
                        }
                        DeviceUsed deviceUsedResult;
                        deviceUsed.setName(commonName + "-" + i + "#");
                        deviceUsed.setBoardNo(commonBoardNo + "-" + i + "#");
                        deviceUsedResult = deviceUsedService.update(deviceUsed);
                        updateResult.add(objectToEntry(deviceUsedResult));
                    } catch (ServiceException e) {
                        LOGGER.error(e.getMessageCode());
                        continue;
                    }
                }
            } else {//单个新增或修改
                //如果是修改，获取连带的绑定关系
                if(deviceUsed.getId() != null && !("").equals(deviceUsed.getId())){
                    DeviceUsed deviceUsedOld = deviceUsedService.findById(deviceUsed.getId());
                    if (deviceUsedOld != null) {
                        deviceUsed.setProductUsed(deviceUsedOld.getProductUsed());
                    }
                }
                deviceUsed = deviceUsedService.update(deviceUsed);
                updateResult.add(objectToEntry(deviceUsed));
            }

            resp = AjaxResponse.success("批量添加将跳过重复主板编号设备");
            resp.addEntry(Constants.RESPONSE_UPDATE_RESULT, updateResult);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            resp = AjaxResponse.failed(-1, "失败");
        }
        return resp;
    }

    //校验主板编号或设备组编号不能包含-_#以外的特殊字符,60字以内
    private static boolean isBoardNoOk(String boardNo) {
        String regex = "^[a-zA-Z0-9-_#]{1,60}$";
        return match(regex, boardNo);
    }

    /**
     * @param regex
     * 正则表达式字符串
     * @param str
     * 要匹配的字符串
     * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
     */
    private static boolean match(String regex, String str) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    @RequestMapping(value = {"admin/device/used/bindProductUsed", "shop/device/used/bindProductUsed"}, method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse deviceUsedBindProductUsed(@RequestParam(value = "bindString") String bindString, @RequestParam(value = "deviceUsedId") Long deviceUsedId, HttpServletRequest request) {
        try {
            AjaxResponse resp;
            List<Long> bindList = JSON.parseArray(bindString, Long.class);
            DeviceUsed deviceUsed = deviceUsedService.findById(deviceUsedId);
            if (deviceUsed == null) {
                return AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE,"参数不正确");
            }
            List<ProductUsed> productUsedList = productUsedService.listByIDs(bindList);
            deviceUsed.setProductUsed(productUsedList);

            deviceUsed = deviceUsedService.update(deviceUsed);
            resp = AjaxResponse.success();
            resp.addEntry(Constants.RESPONSE_UPDATE_RESULT, objectToEntry(deviceUsed));
            return resp;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            return AjaxResponse.failed(-1, "失败");
        }

    }

    @RequestMapping(value = {"admin/device/used/delete", "shop/device/used/delete"}, method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse deleteDeviceUsed(@Valid @RequestBody DeviceUsed deviceUsed, HttpServletRequest request) {
        try {
            AjaxResponse resp;
            Object o = request.getSession().getAttribute(Constants.ADMIN_STORE);
            if (o == null) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("请先切换门店");
                return resp;
            }
            DeviceUsed deviceUsed1 = deviceUsedService.findById(deviceUsed.getId());

            if (deviceUsed1.getProductUsed() != null && deviceUsed1.getProductUsed().size() > 0) {
                return AjaxResponse.failed(-1, "已有关联设备组，无法删除！请取消关联后重试。");
            }
            MerchantStore merchantStore1 = (MerchantStore) o;
            if (!merchantStore1.getId().equals(deviceUsed1.getStore().getId())) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE, "要删除的设备不属于与当前切换的门店");
                return resp;
            }


            //先手动删除所有该对象关联的属性，再删除该对象。因为关联关系是属性多对一该对象，关联字段放在属性表里，不能通过删对象级联删除属性。
            deviceUsedAttributeService.deleteByDeviceUsedId(deviceUsed.getId());

            deviceUsedService.delete(deviceUsed1);
            return AjaxResponse.success();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            return AjaxResponse.failed(-1, "在其他地方被使用，无法删除");
        }
    }

	@RequestMapping(value = {"admin/device/used/listOnlineDevice"}, method = RequestMethod.GET)
	@ResponseBody
	public AjaxPageableResponse listOnlineDevice( HttpServletRequest request, WhereRequest whereRequest) {
		try {
			AjaxPageableResponse resp = new AjaxPageableResponse();
			if (request.getSession().getAttribute(Constants.ADMIN_STORE) == null) {
				resp = AjaxPageableResponse.failed(-1,"请先切换门店");
				return resp;
			}

			String deviceNameCondition = null;
			if (whereRequest.getQueryObj() != null) {
				JSONObject object = JSON.parseObject(whereRequest.getQueryObj());
				Object obj = object.get("deviceName");
				deviceNameCondition = null == obj ? null : obj.toString();
			}
			ConcurrentMap<String, Channel> map = serverBootstrap.getAllChannels();
			DeviceUsed deviceUsed;
			for (Map.Entry<String, Channel> entry : map.entrySet()) {
				String boardNo = entry.getKey();
				Channel channel = entry.getValue();
				deviceUsed = deviceUsedService.getByBoardNo(boardNo);
				if(null != deviceUsed && null != channel){
					String deviceName = deviceUsed.getName();
					if(StringUtil.isNullOrEmpty(deviceNameCondition) ){
						resp.addDataEntry(objectToEntry(deviceUsed,channel));
					}else if(deviceName.indexOf(deviceNameCondition) >= 0){
						resp.addDataEntry(objectToEntry(deviceUsed,channel));
					}
				}
			}
			return resp;
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return AjaxPageableResponse.failed(-1, "查询设备出错");
		}
	}

	//把类转换成entry返回给前端，解耦和
	private Map objectToEntry(DeviceUsed deviceUsed, Channel channel) {
		Map entry = new HashMap();
		entry.put("boardNo", deviceUsed.getBoardNo());
		entry.put("deviceUsedName", deviceUsed.getName());
		entry.put("merchantName", deviceUsed.getStore().getMerchant().getName());
		entry.put("storeName", deviceUsed.getStore().getName());
		entry.put("IP", RemotingHelper.parseChannelRemoteAddr(channel));
		entry.put("comment", "");
		return entry;
	}

    //把类转换成entry返回给前端，解耦和
    private Map objectToEntry(DeviceUsed deviceUsed) {
        Map entry = new HashMap();
        entry.put("id", deviceUsed.getId());
        entry.put("name", deviceUsed.getName());
        entry.put("boardNo", deviceUsed.getBoardNo());
        entry.put("deviceNum", deviceUsed.getDeviceNum());
        entry.put("description", deviceUsed.getDescription());
        entry.put("phone", deviceUsed.getPhone());
        List<ProductUsed> productUsedList = deviceUsed.getProductUsed();
        if (productUsedList != null) {
            for (ProductUsed productUsed : productUsedList) {
                productUsed.setDeviceUsed(null);
                productUsed.setAttributes(null);
            }
        }
        entry.put("productUsedList", productUsedList);

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

    @RequestMapping(value = {"admin/device/used/attribute/save", "shop/device/used/attribute/save"}, method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse saveAttribute(@ModelAttribute DeviceUsed deviceUsed, @Valid @RequestBody DeviceUsedAttribute attribute, HttpServletRequest request) throws Exception {
        AjaxResponse resp;
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
        resp.addEntry(Constants.RESPONSE_UPDATE_RESULT, entity);
        return resp;
    }

    @RequestMapping(value = {"admin/device/used/attribute/delete", "shop/device/used/attribute/delete"}, method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse deleteAttribute(@Valid @RequestBody DeviceUsedAttribute attribute, HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        try {
            if (null != attribute.getId()) {
                DeviceUsedAttribute entity = deviceUsedAttributeService.findById(attribute.getId());
                deviceUsedAttributeService.delete(entity);
            }
            return AjaxResponse.success();
        } catch (Exception e) {
            resp.setErrorString("删除设备属性异常");
            LOGGER.error("Error delete productAttributes", e);
        }
        return resp;

    }


    @RequestMapping(value = {"admin/device/used/attribute/listByProductId", "shop/device/used/attribute/listByProductId"}, method = RequestMethod.GET)
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
            for (DeviceAttribute commonAttribute : commonAttributeList) {
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
    @RequestMapping(value = {"admin/product/used/paging", "shop/product/used/paging"}, method = RequestMethod.GET)
    public
    @ResponseBody
    AjaxPageableResponse pageUsers(Model model, HttpServletRequest request, WhereRequest whereRequest) {
        AjaxPageableResponse resp = new AjaxPageableResponse();
        String currentUser = request.getRemoteUser();
        try {
            Object o = request.getSession().getAttribute(Constants.ADMIN_STORE);
            if (o == null) {
                resp.setErrorString("请先切换门店");
                return resp;
            }
            MerchantStore merchantStore1 = (MerchantStore) o;

            PageResult<ProductUsed> pageList = productUsedService.pageByStore(merchantStore1.getId(), whereRequest);
            List<ProductUsed> productUsedList = pageList.getList();
            for (ProductUsed productUsed : productUsedList) {
                resp.addDataEntry(objectToEntry(productUsed));
            }
            resp.setRecordsTotal(pageList.getRecordsTotal());
        } catch (Exception e) {
            LOGGER.error("Error while paging products", e);
            resp.setErrorString("出错");
        }
        return resp;
    }

    @RequestMapping(value = {"admin/product/used/listByStoreId", "shop/product/used/listByStoreId"}, method = RequestMethod.GET)
    public
    @ResponseBody
    AjaxPageableResponse productUsedListByStoreId(HttpServletRequest request, WhereRequest whereRequest) {
        AjaxPageableResponse resp = new AjaxPageableResponse();
        String currentUser = request.getRemoteUser();
        try {
            Object o = request.getSession().getAttribute(Constants.ADMIN_STORE);
            if (o == null) {
                resp.setErrorString("请先切换门店");
                return resp;
            }
            MerchantStore merchantStore1 = (MerchantStore) o;

            whereRequest.setCount(-1);//不分页，查询所有结果
            PageResult<ProductUsed> pageList = productUsedService.pageByStore(merchantStore1.getId(), whereRequest);
            List<ProductUsed> productUsedList = pageList.getList();
            for (ProductUsed productUsed : productUsedList) {
                resp.addDataEntry(objectToEntry(productUsed));
            }
            resp.setRecordsTotal(pageList.getRecordsTotal());
        } catch (Exception e) {
            LOGGER.error("Error while paging products", e);
        }
        return resp;
    }

    //获取所有设备组接口
    @RequestMapping(value = {"admin/product/used/list"}, method = RequestMethod.GET)
    @ResponseBody
    public AjaxPageableResponse productUsedList(HttpServletRequest request, WhereRequest whereRequest) {
        AjaxPageableResponse resp = new AjaxPageableResponse();
        try {
            whereRequest.setCount(-1);//不分页，查询所有结果
            PageResult<ProductUsed> pageList = productUsedService.pageByStore(null, whereRequest);
            List<ProductUsed> productUsedList = pageList.getList();
            for (ProductUsed productUsed : productUsedList) {
                resp.addDataEntry(objectToEntry(productUsed));
            }
            resp.setRecordsTotal(pageList.getRecordsTotal());
        } catch (Exception e) {
            LOGGER.error("Error while paging products", e);
        }
        return resp;
    }

    @RequestMapping(value = {"admin/product/used/listByStore4Select", "shop/product/used/listByStore4Select"}, method = RequestMethod.GET)
    @ResponseBody
    public List productUsedListByStore4Select(HttpServletRequest request) {
        AjaxResponse resp = new AjaxResponse();
        try {
            Object o = request.getSession().getAttribute(Constants.ADMIN_STORE);
            if (o == null) {
                resp.setErrorString("请先切换门店");
                return null;
            }
            MerchantStore merchantStore1 = (MerchantStore) o;

            WhereRequest whereRequest = new WhereRequest();
            whereRequest.setCount(-1);//不分页，查询所有结果
            PageResult<ProductUsed> pageList = productUsedService.pageByStore(merchantStore1.getId(), whereRequest);
            List<ProductUsed> productUsedList = pageList.getList();
            for (ProductUsed productUsed : productUsedList) {
                Map entry = new HashMap();
                entry.put("name", productUsed.getCode());
                entry.put("value", productUsed.getCode().replaceAll("#", ""));
                resp.addDataEntry(entry);
            }
            return resp.getRows();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
        }
        return null;
    }

    @RequestMapping(value = {"admin/product/used/update", "shop/product/used/update"}, method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse saveUsedProduct(@Valid @RequestBody ProductUsed productUsed, @RequestParam(value = "autoStart") Long autoStart, @RequestParam(value = "autoEnd") Long autoEnd, HttpServletRequest request) throws Exception {
        AjaxResponse resp;
        try {
            Object o = request.getSession().getAttribute(Constants.ADMIN_STORE);
            if (o == null) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("请先切换门店");
                return resp;
            }
            if (autoEnd != null && autoStart != null && autoEnd < autoStart) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("结束编号不能小于开始编号");
                return resp;
            }
            String productCode = productUsed.getCode();
            if (StringUtil.isNullOrEmpty(productCode)) {
                return AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE,"设备组编号不能为空");
            }
            if (!isBoardNoOk(productCode)) {
                return AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE,"设备组编号不能包含-_#以外的特殊字符,60字以内");
            }
            //校验主板编号
            if (autoEnd != null && autoStart != null) {//批量校验设备组编号不能重复
                //预留看需求是否要验证
            } else {//单个校验设备组编号不能重复
                ProductUsed pU = productUsedService.getByCode(productCode);
                if (pU != null && !pU.getId().equals(productUsed.getId())) { //编辑时排除当前设备
                    resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                    resp.setErrorString("门店" + pU.getStore().getName() + "已存在此设备组编号，请更换编号");
                    return resp;
                }
            }

            MerchantStore merchantStore1 = (MerchantStore) o;

            List<Object> updateResult = new ArrayList<Object>();
            productUsed.setStore(merchantStore1);

            if (autoEnd != null && autoStart != null && autoEnd >= 0 && autoStart >= 0) {//批量新增
                for (Long i = autoStart; i < autoEnd + 1; i++) {
                    try {
                        //重复编号的不添加
                        ProductUsed temp = productUsedService.getByCode(productUsed.getType() + "-" + i + "#");
                        if (temp != null) {
                            continue;
                        }
                        ProductUsed productUsedResult;
                        productUsed.setCode(productUsed.getType() + "-" + i + "#");
                        productUsedResult = productUsedService.update(productUsed);
                        updateResult.add(objectToEntry(productUsedResult));
                    } catch (ServiceException e) {
                        LOGGER.error(e.getMessageCode());
                        continue;
                    }
                }
            } else {//单个新增或修改
                //如果是修改，获取连带的绑定关系
                if(productUsed.getId() != null && !("").equals(productUsed.getId())){
                    ProductUsed productUsedOld = productUsedService.findById(productUsed.getId());
                    if (productUsedOld != null) {
                        productUsed.setDeviceUsed(productUsedOld.getDeviceUsed());
                    }
                }
                productUsed = productUsedService.update(productUsed);
                updateResult.add(objectToEntry(productUsed));
            }

            resp = AjaxResponse.success("批量添加将跳过重复编号设备组");
            resp.addEntry(Constants.RESPONSE_UPDATE_RESULT, updateResult);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            resp = AjaxResponse.failed(-1, "失败");
        }
        return resp;
    }

    @RequestMapping(value = {"admin/product/used/bindDeviceUsed", "shop/product/used/bindDeviceUsed"}, method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse productUsedBindDeviceUsed(@RequestParam(value = "bindString") String bindString, @RequestParam(value = "productUsedId") Long productUsedId, HttpServletRequest request) {
        try {
            AjaxResponse resp;
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
            resp.addEntry(Constants.RESPONSE_UPDATE_RESULT, objectToEntry(productUsed));
            return resp;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            return AjaxResponse.failed(-1, "失败");
        }

    }

    @RequestMapping(value = {"admin/product/used/delete", "shop/product/used/delete"}, method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse deleteProductUsed(@Valid @RequestBody ProductUsed productUsed, HttpServletRequest request) {
        try {
            AjaxResponse resp;
            Object o = request.getSession().getAttribute(Constants.ADMIN_STORE);
            if (o == null) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("请先切换门店");
                return resp;
            }
            ProductUsed productUsed1 = productUsedService.findById(productUsed.getId());
            if(productUsed1 == null) {
                return AjaxResponse.failed(-1, "设备组不存在。");
            }
            if (productUsed1.getDeviceUsed() != null && productUsed1.getDeviceUsed().size() > 0) {
                return AjaxResponse.failed(-1, "已有关联设备，无法删除！请取消关联后重试。");
            }
            MerchantStore merchantStore1 = (MerchantStore) o;
            if (!merchantStore1.getId().equals(productUsed1.getStore().getId())) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE, "要删除的设备组不属于与当前切换的门店");
                return resp;
            }

            //先手动删除所有该对象关联的属性，再删除该对象。因为关联关系是属性多对一该对象，关联字段放在属性表里，不能通过删对象级联删除属性。
            productUsedAttributeService.deleteByProductUsedId(productUsed.getId());

            productUsedService.delete(productUsed1);
            return AjaxResponse.success();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return AjaxResponse.failed(-1, "在其他地方被使用，无法删除");
        }

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
        if (productUsed.getDeviceUsed() != null) {
            for (DeviceUsed deviceUsed : productUsed.getDeviceUsed()) {
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

    @RequestMapping(value = {"admin/product/attribute/save", "shop/product/attribute/save"}, method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse saveAttribute(@ModelAttribute ProductUsed product, @Valid @RequestBody ProductUsedAttribute attribute, HttpServletRequest request) throws Exception {
        AjaxResponse resp;
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
        resp.addEntry(Constants.RESPONSE_UPDATE_RESULT, entity);
        return resp;
    }

    @RequestMapping(value = {"admin/product/attribute/delete", "shop/product/attribute/delete"}, method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse deleteAttributeProduct(@Valid @RequestBody ProductUsedAttribute attribute, HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        try {
            if (null != attribute.getId()) {
                ProductUsedAttribute entity = productUsedAttributeService.findById(attribute.getId());
                productUsedAttributeService.delete(entity);
            }
            return AjaxResponse.success();
        } catch (Exception e) {
            resp.setErrorString("删除设备组属性异常");
            LOGGER.error("Error delete productAttributes", e);
        }
        return resp;

    }

    @RequestMapping(value = {"admin/product/type/productOpts", "shop/product/type/productOpts"}, method = RequestMethod.GET)
    public
    @ResponseBody
    List productUsedType(Model model, HttpServletRequest request) {
        return ProductType.getProductTypeList4Select();
    }

}
