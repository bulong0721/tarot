package com.myee.tarot.web.admin.controller.catering;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.myee.tarot.catering.domain.Table;
import com.myee.tarot.catering.domain.TableType;
import com.myee.tarot.catering.domain.TableZone;
import com.myee.tarot.catering.service.TableService;
import com.myee.tarot.catering.service.TableTypeService;
import com.myee.tarot.catering.service.TableZoneService;
import com.myee.tarot.core.util.ajax.AjaxPageableResponse;
import com.myee.tarot.core.util.ajax.AjaxResponse;
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
        typeService.update(type);
        return AjaxResponse.success();
    }

    @RequestMapping(value = "/type/delete", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse delTableType(@RequestBody TableType type, HttpServletRequest request) throws Exception {
        typeService.delete(type);
        return AjaxResponse.success();
    }

    @RequestMapping(value = "/type/paging", method = RequestMethod.GET)
    public
    @ResponseBody
    AjaxPageableResponse pageTypes(Model model, HttpServletRequest request) {
        AjaxPageableResponse resp = new AjaxPageableResponse();
        List<TableType> typeList = typeService.list();
        for (TableType type : typeList) {
            Map entry = new HashMap();
            entry.put("id", type.getId());
            entry.put("name", type.getName());
            entry.put("description", type.getDescription());
            entry.put("capacity", type.getCapacity());
            entry.put("minimum", type.getMinimum());
            resp.addDataEntry(entry);
        }
        return resp;
    }

    @RequestMapping(value = "/zone/save", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse addTableZone(@RequestBody TableZone zone, HttpServletRequest request) throws Exception {
        zoneService.update(zone);
        return AjaxResponse.success();
    }

    @RequestMapping(value = "/zone/delete", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse delTableZone(@RequestBody TableZone zone, HttpServletRequest request) throws Exception {
        zoneService.delete(zone);
        return AjaxResponse.success();
    }

    @RequestMapping(value = "/zone/paging", method = RequestMethod.GET)
    public
    @ResponseBody
    AjaxPageableResponse pageZones(Model model, HttpServletRequest request) {
        AjaxPageableResponse resp = new AjaxPageableResponse();
        List<TableZone> typeList = zoneService.list();
        for (TableZone zone : typeList) {
            Map entry = new HashMap();
            entry.put("id", zone.getId());
            entry.put("name", zone.getName());
            entry.put("description", zone.getDescription());
            resp.addDataEntry(entry);
        }
        return resp;
    }

    @RequestMapping(value = "/table/save", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse addTable(@RequestBody Table table, HttpServletRequest request) throws Exception {
        tableService.update(table);
        return AjaxResponse.success();
    }

    @RequestMapping(value = "/table/delete", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse delTable(@RequestBody Table table, HttpServletRequest request) throws Exception {
        tableService.delete(table);
        return AjaxResponse.success();
    }

    @RequestMapping(value = "/table/paging", method = RequestMethod.GET)
    public
    @ResponseBody
    AjaxPageableResponse pageTables(Model model, HttpServletRequest request) {
        AjaxPageableResponse resp = new AjaxPageableResponse();
        List<Table> typeList = tableService.list();
        for (Table table : typeList) {
            Map entry = new HashMap();
            entry.put("id", table.getId());
            entry.put("name", table.getName());
            entry.put("description", table.getDescription());
            entry.put("tableType", new TypeDTO(table.getTableType()));
            entry.put("tableZone", new ZoneDTO(table.getTableZone()));
            resp.addDataEntry(entry);
        }
        return resp;
    }


    @RequestMapping(value = "/type/options", method = RequestMethod.GET)
    public
    @ResponseBody
    List<TypeDTO> typeOptions(Model model, HttpServletRequest request) {
        List<TableType> typeList = typeService.list();
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
        List<TableZone> typeList = zoneService.list();
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
