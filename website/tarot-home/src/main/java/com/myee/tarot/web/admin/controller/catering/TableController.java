package com.myee.tarot.web.admin.controller.catering;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.myee.tarot.catering.domain.Table;
import com.myee.tarot.catering.domain.TableType;
import com.myee.tarot.catering.domain.TableZone;
import com.myee.tarot.catering.service.TableService;
import com.myee.tarot.catering.service.TableTypeService;
import com.myee.tarot.catering.service.TableZoneService;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.exception.ServiceException;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.ajax.AjaxPageableResponse;
import com.myee.tarot.core.util.ajax.AjaxResponse;
import com.myee.tarot.merchant.domain.Merchant;
import com.myee.tarot.merchant.domain.MerchantStore;
import com.myee.tarot.web.util.StringUtil;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Martin on 2016/4/21.
 */
@Controller
@RequestMapping("/admin/catering")
public class TableController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TableController.class);

    @Autowired
    private TableTypeService typeService;

    @Autowired
    private TableZoneService zoneService;

    @Autowired
    private TableService tableService;

    @RequestMapping(value = "/type/save", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse addTableType(@RequestBody TableType type, HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        try {
            Merchant merchant1 = (Merchant) request.getSession().getAttribute(Constants.ADMIN_MERCHANT);
            if (request.getSession().getAttribute(Constants.ADMIN_STORE) == null) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("请先切换门店");
                return resp;
            }
            MerchantStore merchantStore1 = (MerchantStore) request.getSession().getAttribute(Constants.ADMIN_STORE);
            type.setStore(merchantStore1);
            typeService.update(type);
        } catch (ServiceException e) {
            e.printStackTrace();
            resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
            resp.setErrorString("出错");
            return resp;
        }
        return AjaxResponse.success();
    }

    @RequestMapping(value = "/type/delete", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse delTableType(@RequestBody TableType type, HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        try {
            if (request.getSession().getAttribute(Constants.ADMIN_STORE) == null) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("请先切换门店");
                return resp;
            }
            MerchantStore merchantStore1 = (MerchantStore) request.getSession().getAttribute(Constants.ADMIN_STORE);
            if (type.getId() == null || StringUtil.isNullOrEmpty(type.getId().toString())) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("参数错误");
                return resp;
            }
            TableType tableType = typeService.getEntity(TableType.class,type.getId());
//            if(tableType == null || (tableType.getStore().getId() != merchantStore1.getId())){
//                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
//                resp.setErrorString("要删除的数据与门店不匹配");
//                return resp;
//            }

            typeService.delete(tableType);
            return AjaxResponse.success();
        } catch (ServiceException e) {
            e.printStackTrace();
            resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
            resp.setErrorString("出错");
            return resp;
        }
    }

    @RequestMapping(value = "/type/paging", method = RequestMethod.GET)
    public
    @ResponseBody
    AjaxPageableResponse pageTypes(Model model, HttpServletRequest request,PageRequest pageRequest) {
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

    @RequestMapping(value = "/zone/save", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse addTableZone(@RequestBody TableZone zone, HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        try {
            if (request.getSession().getAttribute(Constants.ADMIN_STORE) == null) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("请先切换门店");
                return resp;
            }
            MerchantStore merchantStore1 = (MerchantStore) request.getSession().getAttribute(Constants.ADMIN_STORE);
            zone.setStore(merchantStore1);
            zoneService.update(zone);
        } catch (ServiceException e) {
            e.printStackTrace();
            resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
            resp.setErrorString("出错");
            return resp;
        }
        return AjaxResponse.success();
    }

    @RequestMapping(value = "/zone/delete", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse delTableZone(@RequestBody TableZone zone, HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
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
            TableZone tableZone = zoneService.getEntity(TableZone.class,zone.getId());
//            System.out.println("tableZone Store Id:"+tableZone.getStore().getId());
//            if(tableZone == null || (tableZone.getStore().getId() != merchantStore1.getId())){
//                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
//                resp.setErrorString("要删除的数据与门店不匹配");
//                return resp;
//            }

            zoneService.delete(tableZone);
            return AjaxResponse.success();
        } catch (ServiceException e) {
            e.printStackTrace();
            resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
            resp.setErrorString("出错");
            return resp;
        }
    }

    @RequestMapping(value = "/zone/paging", method = RequestMethod.GET)
    public
    @ResponseBody
    AjaxPageableResponse pageZones(Model model, HttpServletRequest request,PageRequest pageRequest) {
        AjaxPageableResponse resp = new AjaxPageableResponse();
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
    }

    @RequestMapping(value = "/table/save", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse addTable(@RequestBody Table table, HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        try {
            if (request.getSession().getAttribute(Constants.ADMIN_STORE) == null) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("请先切换门店");
                return resp;
            }
            MerchantStore merchantStore1 = (MerchantStore) request.getSession().getAttribute(Constants.ADMIN_STORE);
            table.setStore(merchantStore1);
            tableService.update(table);
        } catch (ServiceException e) {
            e.printStackTrace();
            resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
            resp.setErrorString("出错");
            return resp;
        }
        return AjaxResponse.success();
    }

    @RequestMapping(value = "/table/delete", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse delTable(@RequestBody Table table, HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
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
            Table table1 = tableService.getEntity(Table.class,table.getId());
            //20160616奇怪:type和zone查询出来的对象，store的id也为空，但是能执行下去不报错，而table不行，查询出来store的id为空，报错？？？
//            System.out.println("table Store Id:"+table.getStore().getId());
//            if(table == null || (table.getStore().getId() != merchantStore1.getId())){
//                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
//                resp.setErrorString("要删除的数据与门店不匹配");
//                return resp;
//            }

            tableService.delete(table1);
            return AjaxResponse.success();
        } catch (ServiceException e) {
            e.printStackTrace();
            resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
            resp.setErrorString("出错");
            return resp;
        }

    }

    @RequestMapping(value = "/table/paging", method = RequestMethod.GET)
    public
    @ResponseBody
    AjaxPageableResponse pageTables(Model model, HttpServletRequest request,PageRequest pageRequest) {
        AjaxPageableResponse resp = new AjaxPageableResponse();
        if (request.getSession().getAttribute(Constants.ADMIN_STORE) == null) {
            resp.setErrorString("请先切换门店");
            return resp;
        }
        MerchantStore merchantStore1 = (MerchantStore) request.getSession().getAttribute(Constants.ADMIN_STORE);
        PageResult<Table> pageList = tableService.pageByStore(merchantStore1.getId(), pageRequest);

        List<Table> typeList = pageList.getList();
        for (Table table : typeList) {
            Map entry = new HashMap();
            entry.put("id", table.getId());
            entry.put("name", table.getName());
            entry.put("description", table.getDescription());
            entry.put("tableType", new TypeDTO(table.getTableType()));
            entry.put("tableZone", new ZoneDTO(table.getTableZone()));
            resp.addDataEntry(entry);
        }
        resp.setRecordsTotal(pageList.getRecordsTotal());
        resp.setRecordsFiltered(pageList.getRecordsFiltered());
        return resp;
    }


    @RequestMapping(value = "/type/options", method = RequestMethod.GET)
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

    @RequestMapping(value = "/zone/options", method = RequestMethod.GET)
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
