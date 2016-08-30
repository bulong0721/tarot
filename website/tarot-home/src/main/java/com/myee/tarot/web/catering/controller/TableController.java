package com.myee.tarot.web.catering.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.myee.tarot.apiold.domain.MenuInfo;
import com.myee.tarot.apiold.service.MenuService;
import com.myee.tarot.catalog.domain.Device;
import com.myee.tarot.catalog.domain.DeviceUsed;
import com.myee.tarot.catering.domain.Table;
import com.myee.tarot.catering.domain.TableType;
import com.myee.tarot.catering.domain.TableZone;
import com.myee.tarot.catering.service.TableService;
import com.myee.tarot.catering.service.TableTypeService;
import com.myee.tarot.catering.service.TableZoneService;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.StringUtil;
import com.myee.tarot.core.util.ajax.AjaxPageableResponse;
import com.myee.tarot.core.util.ajax.AjaxResponse;
import com.myee.tarot.device.service.DeviceService;
import com.myee.tarot.device.service.DeviceUsedService;
import com.myee.tarot.merchant.domain.Merchant;
import com.myee.tarot.merchant.domain.MerchantStore;
import com.myee.tarot.core.util.ValidatorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Martin on 2016/4/21.
 */
@Controller
public class TableController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TableController.class);

    @Autowired
    private TableTypeService typeService;

    @Autowired
    private TableZoneService zoneService;

    @Autowired
    private TableService tableService;

    @Autowired
    private DeviceUsedService deviceUsedService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private MenuService menuService;

    @RequestMapping(value = {"admin/catering/type/save", "shop/catering/type/save"}, method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse addTableType(@RequestBody TableType type, HttpServletRequest request) throws Exception {
        AjaxResponse resp;
        try {
            Merchant merchant1 = (Merchant) request.getSession().getAttribute(Constants.ADMIN_MERCHANT);
            if (request.getSession().getAttribute(Constants.ADMIN_STORE) == null) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("请先切换门店");
                return resp;
            }
            MerchantStore merchantStore1 = (MerchantStore) request.getSession().getAttribute(Constants.ADMIN_STORE);
            type.setStore(merchantStore1);
            type = typeService.update(type);

            resp = AjaxResponse.success();
            resp.addEntry("updateResult", type);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
            resp.setErrorString("出错");
            return resp;
        }
        return resp;
    }

    @RequestMapping(value = {"admin/catering/type/delete", "shop/catering/type/delete"}, method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse delTableType(@RequestBody TableType type, HttpServletRequest request) throws Exception {
        AjaxResponse resp;
        try {
            if (request.getSession().getAttribute(Constants.ADMIN_STORE) == null) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("请先切换门店");
                return resp;
            }

            if (type.getId() == null || StringUtil.isNullOrEmpty(type.getId().toString())) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("参数错误");
                return resp;
            }
            TableType tableType = typeService.findById(type.getId());
            if (tableType == null) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE, "类型不存在");
                return resp;
            }
            MerchantStore merchantStore1 = (MerchantStore) request.getSession().getAttribute(Constants.ADMIN_STORE);
            if (merchantStore1.getId() != type.getStore().getId()) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE, "要删除的类型不属于与当前切换的门店");
                return resp;
            }

            typeService.delete(tableType);
            return AjaxResponse.success();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
            resp.setErrorString("出错");
            return resp;
        }
    }

    @RequestMapping(value = {"admin/catering/type/paging", "shop/catering/type/paging"}, method = RequestMethod.GET)
    public
    @ResponseBody
    AjaxPageableResponse pageTypes(Model model, HttpServletRequest request, PageRequest pageRequest) {
        AjaxPageableResponse resp = new AjaxPageableResponse();
        if (request.getSession().getAttribute(Constants.ADMIN_STORE) == null) {
            resp.setErrorString("请先切换门店");
            return resp;
        }
        MerchantStore merchantStore1 = (MerchantStore) request.getSession().getAttribute(Constants.ADMIN_STORE);
        PageResult<TableType> pageList = typeService.pageByStore(merchantStore1.getId(), pageRequest);
        List<TableType> typeList = pageList.getList();
        for (TableType type : typeList) {
            Map entry = new HashMap();
            entry.put("id", type.getId());
            entry.put("name", type.getName());
            entry.put("description", type.getDescription());
            entry.put("capacity", type.getCapacity());
            entry.put("minimum", type.getMinimum());
            resp.addDataEntry(entry);
        }
        resp.setRecordsTotal(pageList.getRecordsTotal());
        return resp;
    }

    @RequestMapping(value = {"admin/catering/zone/save", "shop/catering/zone/save"}, method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse addTableZone(@RequestBody TableZone zone, HttpServletRequest request) throws Exception {
        AjaxResponse resp;
        try {
            if (request.getSession().getAttribute(Constants.ADMIN_STORE) == null) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("请先切换门店");
                return resp;
            }
            MerchantStore merchantStore1 = (MerchantStore) request.getSession().getAttribute(Constants.ADMIN_STORE);
            zone.setStore(merchantStore1);
            zone = zoneService.update(zone);

            resp = AjaxResponse.success();
            resp.addEntry("updateResult", zone);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
            resp.setErrorString("出错");
            return resp;
        }
        return resp;
    }

    @RequestMapping(value = {"admin/catering/zone/delete", "shop/catering/zone/delete"}, method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse delTableZone(@RequestBody TableZone zone, HttpServletRequest request) throws Exception {
        AjaxResponse resp;
        try {
            if (request.getSession().getAttribute(Constants.ADMIN_STORE) == null) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("请先切换门店");
                return resp;
            }
            MerchantStore merchantStore1 = (MerchantStore) request.getSession().getAttribute(Constants.ADMIN_STORE);
            if (zone.getId() == null || StringUtil.isNullOrEmpty(zone.getId().toString())) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("参数错误");
                return resp;
            }
            TableZone tableZone = zoneService.findById(zone.getId());

            zoneService.delete(tableZone);
            return AjaxResponse.success();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
            resp.setErrorString("出错");
            return resp;
        }
    }

    @RequestMapping(value = {"admin/catering/zone/paging", "shop/catering/zone/paging"}, method = RequestMethod.GET)
    public
    @ResponseBody
    AjaxPageableResponse pageZones(Model model, HttpServletRequest request, PageRequest pageRequest) {
        AjaxPageableResponse resp = new AjaxPageableResponse();
        try {
            if (request.getSession().getAttribute(Constants.ADMIN_STORE) == null) {
                resp.setErrorString("请先切换门店");
                return resp;
            }
            MerchantStore merchantStore1 = (MerchantStore) request.getSession().getAttribute(Constants.ADMIN_STORE);

            PageResult<TableZone> pageList = zoneService.pageByStore(merchantStore1.getId(), pageRequest);
            List<TableZone> typeList = pageList.getList();
            for (TableZone zone : typeList) {
                Map entry = new HashMap();
                entry.put("id", zone.getId());
                entry.put("name", zone.getName());
                entry.put("description", zone.getDescription());
                resp.addDataEntry(entry);
            }

            resp.setRecordsTotal(pageList.getRecordsTotal());

            return resp;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return resp;
        }
    }

    @RequestMapping(value = {"admin/catering/table/save", "shop/catering/table/save"}, method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse addTable(@RequestBody Table table,
                                 @RequestParam(value = "dUString", required = false) String dUString,
                                 @RequestParam(value = "autoStart") Long autoStart,
                                 @RequestParam(value = "autoEnd") Long autoEnd,
                                 @RequestParam(value = "autoDUStart") Long autoDUStart,
                                 @RequestParam(value = "autoDUEnd") Long autoDUEnd,
                                 HttpServletRequest request) throws Exception {
        AjaxResponse resp;
        try {
            if (request.getSession().getAttribute(Constants.ADMIN_STORE) == null) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("请先切换门店");
                return resp;
            }
            if ((autoEnd != null && autoStart != null && autoEnd < autoStart)
                    || (autoDUEnd != null && autoDUStart != null && autoDUEnd < autoDUStart)) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("结束编号不能小于开始编号");
                return resp;
            }
            if (!validateScanCode(table.getScanCode())) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE, "桌子码只能是000-200之间的三位数字！");
                return resp;
            }
            MerchantStore merchantStore1 = (MerchantStore) request.getSession().getAttribute(Constants.ADMIN_STORE);
            DeviceUsed deviceUsed = null;
            String dUCommonName = null;
            String commonBoardNo = null;
            if (dUString != null) {
                deviceUsed = JSON.parseObject(dUString, DeviceUsed.class);
                if (StringUtil.isNullOrEmpty(deviceUsed.getBoardNo())) {
                    resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                    resp.setErrorString("主板编号不能为空");
                    return resp;
                }
                if (deviceUsed.getPhone() != null && !ValidatorUtil.isMultiMobile(deviceUsed.getPhone())) {
                    resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE, "请输入正确的手机号！");
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
                dUCommonName = deviceUsed.getName();
                commonBoardNo = deviceUsed.getBoardNo();
                deviceUsed.setStore(merchantStore1);
            }

            table.setStore(merchantStore1);
            List<Object> updateResult = new ArrayList<Object>();
            if (autoEnd != null && autoStart != null && autoEnd >= 0 && autoStart >= 0) {//批量新增
                String commonName = table.getName();
                for (Long i = autoStart; i < autoEnd + 1; i++) {
                    Table tableResult = new Table();
                    table.setName(commonName + i);
                    //批量新增桌子码
                    if (!StringUtil.isNullOrEmpty(table.getScanCode())) {
                        table.setScanCode(calAutoScanCode(table.getScanCode(), (i - autoStart)));
                    }

                    //同时新增并关联设备
                    if (autoDUEnd != null && autoDUStart != null
                            && autoDUEnd >= 0 && autoDUStart >= 0
                            && deviceUsed != null && dUCommonName != null && commonBoardNo != null) {
                        deviceUsed.setName(dUCommonName + (autoDUStart + i - autoStart));
                        deviceUsed.setBoardNo(String.valueOf(System.currentTimeMillis()) + "auto" + commonBoardNo + (autoDUStart + i - autoStart));
                        DeviceUsed deviceUsed1 = deviceUsedService.update(deviceUsed);
                        if (deviceUsed1 != null) {
                            List<DeviceUsed> deviceUsedResult = new ArrayList<DeviceUsed>();
                            deviceUsedResult.add(deviceUsed1);
                            table.setDeviceUsed(deviceUsedResult);
                        }
                    }

                    tableResult = tableService.update(table);
                    updateResult.add(objectToEntry(tableResult));
                }
            } else {//单个新增或修改
                table = tableService.update(table);
                updateResult.add(objectToEntry(table));
            }

            resp = AjaxResponse.success();
            resp.addEntry("updateResult", updateResult);


        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
            resp.setErrorString("出错");
            return resp;
        }
        return resp;
    }

    //验证餐桌码只能在000-200之间
    private boolean validateScanCode(String scanCode) {
        if (StringUtil.isNullOrEmpty(scanCode)) {
            return true;
        } else if (scanCode.length() != 3) {
            return false;
        } else if (!scanCode.matches("(([01]{1}[0-9]{2})|([2]{1}[0]{2}))$")) {//判断是不是0/1/2开头的3位纯数字
            return false;
        }
        try {
            int nScanCode = Integer.parseInt(scanCode);
            if (nScanCode >= 0 && nScanCode <= 200) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return false;
        }
    }

    //计算批量生成的餐桌码，超过200的设为200
    private String calAutoScanCode(String scanCode, Long i) {
        try {
            Long nScanCode = Long.parseLong(scanCode) + i;
            if (nScanCode >= 0 && nScanCode <= 200) {
                return nScanCode + "";
            } else if (nScanCode < 0) {
                return "0";
            } else if (nScanCode > 200) {
                return "200";
            } else {
                return "";
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return "";
        }
    }

    @RequestMapping(value = {"admin/catering/table/delete", "shop/catering/table/delete"}, method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse delTable(@RequestBody Table table, HttpServletRequest request) throws Exception {
        AjaxResponse resp;
        try {
            if (request.getSession().getAttribute(Constants.ADMIN_STORE) == null) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("请先切换门店");
                return resp;
            }
            MerchantStore merchantStore1 = (MerchantStore) request.getSession().getAttribute(Constants.ADMIN_STORE);
            if (table.getId() == null || StringUtil.isNullOrEmpty(table.getId().toString())) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("参数错误");
                return resp;
            }
            Table table1 = tableService.findById(table.getId());

            tableService.delete(table1);
            return AjaxResponse.success();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
            resp.setErrorString("出错");
            return resp;
        }
    }

    @RequestMapping(value = {"admin/catering/table/paging", "shop/catering/table/paging"}, method = RequestMethod.GET)
    public
    @ResponseBody
    AjaxPageableResponse pageTables(Model model, HttpServletRequest request, PageRequest pageRequest) {
        AjaxPageableResponse resp = new AjaxPageableResponse();
        try {
            if (request.getSession().getAttribute(Constants.ADMIN_STORE) == null) {
                resp.setErrorString("请先切换门店");
                return resp;
            }
            MerchantStore merchantStore1 = (MerchantStore) request.getSession().getAttribute(Constants.ADMIN_STORE);
            PageResult<Table> pageList = tableService.pageByStore(merchantStore1.getId(), pageRequest);

            List<Table> typeList = pageList.getList();
            for (Table table : typeList) {
                resp.addDataEntry(objectToEntry(table));
            }
            resp.setRecordsTotal(pageList.getRecordsTotal());
            return resp;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return resp;
        }
    }

    @RequestMapping(value = {"admin/catering/table/bindDeviceUsed", "shop/catering/table/bindDeviceUsed"}, method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse productUsedBindDeviceUsed(@RequestParam(value = "bindString") String bindString, @RequestParam(value = "tableId") Long tableId, HttpServletRequest request) {
        try {
            AjaxResponse resp;
            List<Long> bindList = JSON.parseArray(bindString, Long.class);
            Table table = tableService.findById(tableId);
            if (table == null) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE, "参数不正确");
                return resp;
            }
            List<DeviceUsed> deviceUsedList = deviceUsedService.listByIDs(bindList);
            table.setDeviceUsed(deviceUsedList);

            table = tableService.update(table);
            resp = AjaxResponse.success();
            resp.addEntry("updateResult", objectToEntry(table));
            return resp;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return AjaxResponse.failed(-1);
    }

    //把类转换成entry返回给前端，解耦和
    private Map objectToEntry(Table table) {
        Map entry = new HashMap();
        entry.put("id", table.getId());
        entry.put("name", table.getName());
        entry.put("description", table.getDescription());
        entry.put("textId", table.getTextId());
        entry.put("scanCode", table.getScanCode());
        if (table.getDeviceUsed() != null) {
            for (DeviceUsed deviceUsed : table.getDeviceUsed()) {
                deviceUsed.setProductUsed(null);
                deviceUsed.setAttributes(null);
                deviceUsed.getDevice().setAttributes(null);
            }
        }
        entry.put("deviceUsedList", table.getDeviceUsed());
        entry.put("tableType", new TypeDTO(table.getTableType()));
        entry.put("tableZone", new ZoneDTO(table.getTableZone()));
        return entry;
    }

    //superMenu---------------------------------------------------
    @RequestMapping(value = {"admin/catering/superMenu/save", "shop/catering/superMenu/save"}, method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse addSuperMenu(@RequestBody MenuInfo menuInfo, HttpServletRequest request) throws Exception {
        AjaxResponse resp;
        try {
            Merchant merchant1 = (Merchant) request.getSession().getAttribute(Constants.ADMIN_MERCHANT);
            if (request.getSession().getAttribute(Constants.ADMIN_STORE) == null) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("请先切换门店");
                return resp;
            }
            //name字段长度限制判断
            if(menuInfo.getName().length()>60){
                return AjaxResponse.failed(-1,"商户菜品名称字数不得大于60");
            }
            //price字段只能为数字
            if(!StringUtil.isNumeric(menuInfo.getPrice())){
                return AjaxResponse.failed(-1,"商户菜品价格只能为整数");
            }
            //scanCode字段只能为数字
            if(!StringUtil.isNumeric(menuInfo.getScanCode())){
                return AjaxResponse.failed(-1,"商户菜品扫描码只能为整数");
            }
            MerchantStore merchantStore1 = (MerchantStore) request.getSession().getAttribute(Constants.ADMIN_STORE);
            menuInfo.setStore(merchantStore1);
            menuInfo = menuService.update(menuInfo);

            resp = AjaxResponse.success();
            resp.addEntry("updateResult", menuInfo);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
            resp.setErrorString("出错");
            return resp;
        }
        return resp;
    }

    @RequestMapping(value = {"admin/catering/superMenu/delete", "shop/catering/superMenu/delete"}, method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse delSuperMenu(@RequestBody MenuInfo menuInfo, HttpServletRequest request) throws Exception {
        AjaxResponse resp;
        try {
            if (request.getSession().getAttribute(Constants.ADMIN_STORE) == null) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("请先切换门店");
                return resp;
            }

            if (menuInfo.getId() == null || StringUtil.isNullOrEmpty(menuInfo.getId().toString())) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("参数错误");
                return resp;
            }
            MenuInfo menuInfo1 = menuService.findById(menuInfo.getId());
            if (menuInfo1 == null) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE, "菜品不存在");
                return resp;
            }
            MerchantStore merchantStore1 = (MerchantStore) request.getSession().getAttribute(Constants.ADMIN_STORE);
            if (merchantStore1.getId() != menuInfo1.getStore().getId()) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE, "要删除的菜品不属于与当前切换的门店");
                return resp;
            }

            menuService.delete(menuInfo1);
            return AjaxResponse.success();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
            resp.setErrorString("出错");
            return resp;
        }
    }

    @RequestMapping(value = {"admin/catering/superMenu/paging", "shop/catering/superMenu/paging"}, method = RequestMethod.GET)
    public
    @ResponseBody
    AjaxPageableResponse pageSuperMenus(Model model, HttpServletRequest request, PageRequest pageRequest) {
        AjaxPageableResponse resp = new AjaxPageableResponse();
        if (request.getSession().getAttribute(Constants.ADMIN_STORE) == null) {
            resp.setErrorString("请先切换门店");
            return resp;
        }
        MerchantStore merchantStore1 = (MerchantStore) request.getSession().getAttribute(Constants.ADMIN_STORE);
        PageResult<MenuInfo> pageList = menuService.pageByStore(merchantStore1.getId(), pageRequest);
        List<MenuInfo> menuList = pageList.getList();
        for (MenuInfo menuInfo : menuList) {
            resp.addDataEntry(objectToEntry(menuInfo));
        }
        resp.setRecordsTotal(pageList.getRecordsTotal());
        return resp;
    }

    //把类转换成entry返回给前端，解耦和
    private Map objectToEntry(MenuInfo menuInfo) {
        Map entry = new HashMap();
        entry.put("id", menuInfo.getId());
        entry.put("name", menuInfo.getName());
        entry.put("photo", menuInfo.getPhoto());
        entry.put("price", menuInfo.getPrice());
        entry.put("scanCode", menuInfo.getScanCode());
        entry.put("active", menuInfo.getActive());
        entry.put("menuId", menuInfo.getMenuId());
        entry.put("subMenuId", menuInfo.getSubMenuId());
        return entry;
    }
    /**
     * options-------------------------------------------------------------------------------
     */
    @RequestMapping(value = {"admin/catering/table/options", "shop/catering/table/options"}, method = RequestMethod.GET)
    public
    @ResponseBody
    List<TableDTO> tableOptions(Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute(Constants.ADMIN_STORE) == null) {
            return null;
        }
        MerchantStore merchantStore1 = (MerchantStore) request.getSession().getAttribute(Constants.ADMIN_STORE);
        List<Table> tableList = tableService.listByStore(merchantStore1.getId());
        return Lists.transform(tableList, new Function<Table, TableDTO>() {
            @Nullable
            @Override
            public TableDTO apply(Table input) {
                return new TableDTO(input);
            }
        });
    }

    @RequestMapping(value = "admin/catering/type/options", method = RequestMethod.GET)
    public
    @ResponseBody
    List<TypeDTO> typeOptions(Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute(Constants.ADMIN_STORE) == null) {
            return null;
        }
        MerchantStore merchantStore1 = (MerchantStore) request.getSession().getAttribute(Constants.ADMIN_STORE);
        List<TableType> typeList = typeService.listByStore(merchantStore1.getId());
        return Lists.transform(typeList, new Function<TableType, TypeDTO>() {
            @Nullable
            @Override
            public TypeDTO apply(TableType input) {
                return new TypeDTO(input);
            }
        });
    }

    @RequestMapping(value = {"admin/catering/zone/options", "shop/catering/zone/options"}, method = RequestMethod.GET)
    public
    @ResponseBody
    List<ZoneDTO> zoneOptions(Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute(Constants.ADMIN_STORE) == null) {
            return null;
        }
        MerchantStore merchantStore1 = (MerchantStore) request.getSession().getAttribute(Constants.ADMIN_STORE);
        List<TableZone> typeList = zoneService.listByStore(merchantStore1.getId());
        return Lists.transform(typeList, new Function<TableZone, ZoneDTO>() {
            @Nullable
            @Override
            public ZoneDTO apply(TableZone input) {
                return new ZoneDTO(input);
            }
        });
    }

    @ExceptionHandler({SQLException.class, Exception.class})
    protected void handleException(Exception ex, HttpServletResponse resp) {
        LOGGER.error(ex.getMessage(), ex);
    }
}
