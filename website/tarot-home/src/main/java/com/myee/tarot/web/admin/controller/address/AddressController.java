package com.myee.tarot.web.admin.controller.address;

import com.myee.tarot.address.service.GeoZoneService;
import com.myee.tarot.core.util.ajax.AjaxResponse;
import com.myee.tarot.reference.domain.GeoZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Chay on 2016/6/3.
 */
@Controller
public class AddressController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddressController.class);

    @Autowired
    private GeoZoneService geoZoneService;

    /**
     * 省市区县商圈
     */
    @RequestMapping(value = "admin/province/list", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResponse listProvince(HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        try {
            List<GeoZone> list = geoZoneService.listProvince();
            for (GeoZone geoZone : list) {
                resp.addDataEntry(objectToEntry(geoZone));
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.setErrorString("出错");
        }
        return resp;
    }

    @RequestMapping(value = "admin/province/list4Select", method = RequestMethod.GET)
    @ResponseBody
    public List listProvince4Select(HttpServletRequest request) throws Exception {
        List resp = new ArrayList();
        try {
            List<GeoZone> list = geoZoneService.listProvince();
            for (GeoZone geoZone : list) {
                Map entry = new HashMap();
                entry.put("name",geoZone.getName());
                entry.put("value",geoZone.getId());
                resp.add(entry);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resp;
    }

    @RequestMapping(value = "admin/city/listByProvince", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResponse listCityByProvince(@RequestParam Long id,HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        try {
            List<GeoZone> list = geoZoneService.listCityByProvince(id);
            for (GeoZone geoZone : list) {
                resp.addDataEntry(objectToEntry(geoZone));
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.setErrorString("出错");
        }
        return resp;
    }

    @RequestMapping(value = "admin/city/listByProvince4Select", method = RequestMethod.GET)
    @ResponseBody
    public List listCityByProvince4Select(@RequestParam Long id,HttpServletRequest request) throws Exception {
        List resp = new ArrayList();
        try {
            List<GeoZone> list = geoZoneService.listCityByProvince(id);
            for (GeoZone geoZone : list) {
                Map entry = new HashMap();
                entry.put("name",geoZone.getName());
                entry.put("value",geoZone.getId());
                resp.add(entry);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resp;
    }

    @RequestMapping(value = "admin/district/listByCity", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResponse listDistrictByCity(@RequestParam Long id,HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        try {
            List<GeoZone> list = geoZoneService.listDistrictByCity(id);
            for (GeoZone geoZone : list) {
                resp.addDataEntry(objectToEntry(geoZone));
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.setErrorString("出错");
        }
        return resp;
    }

    @RequestMapping(value = "admin/district/listByCity4Select", method = RequestMethod.GET)
    @ResponseBody
    public List listDistrictByCity4Select(@RequestParam Long id,HttpServletRequest request) throws Exception {
        List resp = new ArrayList();
        try {
            List<GeoZone> list = geoZoneService.listDistrictByCity(id);
            for (GeoZone geoZone : list) {
                Map entry = new HashMap();
                entry.put("name",geoZone.getName());
                entry.put("value",geoZone.getId());
                resp.add(entry);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resp;
    }

    private Map<String, Object> objectToEntry(GeoZone geoZone) {
        Map entry = new HashMap();
        entry.put("id", geoZone.getId());
        entry.put("name", geoZone.getName());
        entry.put("zipcode", geoZone.getCode());
        entry.put("level", geoZone.getLevel());
        return entry;
    }

}
