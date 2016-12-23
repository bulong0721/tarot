package com.myee.tarot.web.configuration.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.myee.djinn.dto.NoticeType;
import com.myee.tarot.catalog.domain.DeviceUsed;
import com.myee.tarot.catalog.domain.ProductUsed;
import com.myee.tarot.catalog.domain.ProductUsedAttribute;
import com.myee.tarot.catalog.service.ProductUsedService;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.util.*;
import com.myee.tarot.core.util.ajax.AjaxPageableResponse;
import com.myee.tarot.core.util.ajax.AjaxResponse;
import com.myee.tarot.resource.domain.UpdateConfig;
import com.myee.tarot.resource.domain.UpdateConfigProductUsedXREF;
import com.myee.tarot.resource.service.UpdateConfigProductUsedXREFService;
import com.myee.tarot.resource.service.UpdateConfigService;
import com.myee.tarot.resource.type.UpdateConfigSeeType;
import com.myee.tarot.web.device.controller.AttributeDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by Chay on 2016/12/15.
 */
@Controller
public class UpdateConfigController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateConfigController.class);

    @Autowired
    private UpdateConfigService updateConfigService;
    @Autowired
    private UpdateConfigProductUsedXREFService updateConfigProductUsedXREFService;
    @Autowired
    private ProductUsedService productUsedService;

    @RequestMapping(value = {"admin/updateConfig/update"}, method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse addUpdateConfig(@Valid @RequestBody UpdateConfig updateConfig, HttpServletRequest request) throws Exception {
        AjaxResponse resp;
        try {
            String type = updateConfig.getType();
            String typeName = com.myee.djinn.dto.NoticeType.getValue(type);
            if( type == null || StringUtil.isBlank(type) || typeName == null
                    || StringUtil.isBlank(typeName)
                    || Constants.UPDATE_TYPE_EXCEPT_LIST.contains(type)) {
                return AjaxResponse.failed(-1,"参数错误");
            }

            String seeType = updateConfig.getSeeType();
            String seeTypeName = new UpdateConfigSeeType().getUpdateConfigSeeTypeName(seeType);
            //如果设备可见类型没设置，默认所有设备不可见
            if( seeType == null || StringUtil.isBlank(seeType)
                    || seeTypeName == null || StringUtil.isBlank(seeTypeName)) {
                updateConfig.setSeeType(Constants.UPDATE_SEE_TYPE_NONE);
            }

            updateConfig = updateConfigService.update(updateConfig);

            resp = AjaxResponse.success();
            resp.addEntry(Constants.RESPONSE_UPDATE_RESULT, objectToEntry(updateConfig));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE,"出错");
        }
        return resp;
    }

//    @RequestMapping(value = {"admin/updateConfig/delete"}, method = RequestMethod.POST)
//    @ResponseBody
//    public AjaxResponse delUpdateConfig(@RequestBody TableType type, HttpServletRequest request) throws Exception {
//        AjaxResponse resp;
//        try {
//            return AjaxResponse.success();
//        } catch (Exception e) {
//            LOGGER.error(e.getMessage(),e);
//            return AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE,"出错");
//        }
//    }

    @RequestMapping(value = {"admin/updateConfig/bindProductUsed"}, method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse deviceUsedBindProductUsed(@RequestParam(value = "bindString") String bindString, @RequestParam(value = "configId") Long configId, HttpServletRequest request) {
        try {
            AjaxResponse resp;
            List<Long> bindList = JSON.parseArray(bindString, Long.class);
            UpdateConfig updateConfig = updateConfigService.findById(configId);
            if (updateConfig == null) {
                return AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE,"配置项不存在");
            }
            List<ProductUsed> productUsedList = productUsedService.listByIDs(bindList);

            if(productUsedList == null || productUsedList.size() == 0) {
                return AjaxResponse.failed(-1, "设备组参数无效");
            }
            updateConfig.setProductUsed(productUsedList);
            updateConfig.setDeviceGroupNOList(bindString);
            updateConfig = updateConfigService.update(updateConfig);

            //手动更新关联关系表
            UpdateConfigProductUsedXREF updateConfigProductUsedXREF = null;
            String type = updateConfig.getType();
            for (ProductUsed productUsed : productUsedList) {
                UpdateConfigProductUsedXREF updateConfigProductUsedXREF_DB = null;
                //同一类型下同一设备的绑定关系只能有一条记录，自研平板类型除外
                if( !type.equals(Constants.UPDATE_TYPE_SELF_DESIGN_BOARD) ){
                    updateConfigProductUsedXREF_DB = updateConfigProductUsedXREFService.getByTypeAndDeviceGroupNO(updateConfig.getType(),productUsed.getCode());
                }
                if(updateConfigProductUsedXREF_DB != null) {
                    updateConfigProductUsedXREF = updateConfigProductUsedXREF_DB;
                }
                else {
                    updateConfigProductUsedXREF = new UpdateConfigProductUsedXREF();
                }
                updateConfigProductUsedXREF.setProductUsed(productUsed);
                updateConfigProductUsedXREF.setUpdateConfig(updateConfig);
                updateConfigProductUsedXREF.setType(updateConfig.getType());
                updateConfigProductUsedXREFService.update(updateConfigProductUsedXREF);
            }

            resp = AjaxResponse.success();
            resp.addEntry(Constants.RESPONSE_UPDATE_RESULT, objectToEntry(updateConfig));
            return resp;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            return AjaxResponse.failed(-1, "失败");
        }

    }

    @RequestMapping(value = {"admin/updateConfig/paging"}, method = RequestMethod.GET)
    @ResponseBody
    public AjaxPageableResponse pageUpdateConfig(Model model, HttpServletRequest request, WhereRequest pageRequest) {
        AjaxPageableResponse resp = new AjaxPageableResponse();
        try {
            PageResult<UpdateConfig> pageList = updateConfigService.page(pageRequest);
            List<UpdateConfig> updateConfigList = pageList.getList();
            for (UpdateConfig updateConfig : updateConfigList) {
                resp.addDataEntry(objectToEntry(updateConfig));
            }
            resp.setRecordsTotal(pageList.getRecordsTotal());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return AjaxPageableResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE,"出错");
        }
        return resp;
    }

    @RequestMapping(value = {"admin/updateConfig/listType"}, method = RequestMethod.GET)
    @ResponseBody
    public List listType(Model model, HttpServletRequest request, WhereRequest pageRequest) {
        try {
            List<Map> resp = new ArrayList();
            NoticeType[] noticeTypes = com.myee.djinn.dto.NoticeType.values();
            for( com.myee.djinn.dto.NoticeType noticeType : noticeTypes ) {
                String key = noticeType.getCaption();
                //是排除列表的类型我们不使用
                if( Constants.UPDATE_TYPE_EXCEPT_LIST.contains(key) ) {
                    continue;
                }
                Map entry = new HashMap();
                entry.put("name", noticeType.getValue());
                entry.put("value", key);//跟前端select刚好是反的
                resp.add(entry);
            }
            return resp;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    @RequestMapping(value = {"admin/updateConfig/listSeeType"}, method = RequestMethod.GET)
    @ResponseBody
    public List listSeeType(Model model, HttpServletRequest request, WhereRequest pageRequest) {
        try {
            return new UpdateConfigSeeType().getUpdateConfigSeeType4Select();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    //把类转换成entry返回给前端，解耦和
    private Map objectToEntry(UpdateConfig updateConfig) {
        Map entry = new HashMap();
        entry.put("id", updateConfig.getId());
        entry.put("name", updateConfig.getName());
        entry.put("description", updateConfig.getDescription());
        String type = updateConfig.getType();
        entry.put("type", type);
        entry.put("typeName", (type == null || "".equals(type)) ? "" : com.myee.djinn.dto.NoticeType.getValue(type));
        String seeType = updateConfig.getSeeType();
        entry.put("seeType", seeType);
        entry.put("seeTypeName", (seeType == null || "".equals(seeType)) ? "" : new UpdateConfigSeeType().getUpdateConfigSeeTypeName(seeType));
        entry.put("createTime", updateConfig.getCreateTime());
        entry.put("path", updateConfig.getPath());
        entry.put("boardNoList", updateConfig.getPath());
        entry.put("productUsedList", listBindProductUsedByConfig(updateConfig));
        return entry;
    }

    //根据UpdateConfig去查询关联的设备组.传入对象中如果已经有关联设备组列表了，就不去数据库查了
    private List<ProductUsed> listBindProductUsedByConfig(UpdateConfig updateConfig) {
        if(updateConfig == null || updateConfig.getId() == null) {
            return Collections.EMPTY_LIST;
        }
        //传入对象如果已经有关联列表了，就不去数据库查了
        List<ProductUsed> listAlready = updateConfig.getProductUsed();
        List<ProductUsed> listResult = new ArrayList<ProductUsed>();
        if( listAlready != null && listAlready.size() > 0 ) {
            for( ProductUsed productUsed : listAlready ) {
                listResult.add(prepareObject(productUsed));
            }
            return listResult;
        }
        //传入对象没有关联列表则取数据库查
        List<UpdateConfigProductUsedXREF> updateConfigProductUsedXREFList = updateConfigProductUsedXREFService.listByConfigId(updateConfig.getId());
        if( updateConfigProductUsedXREFList == null || updateConfigProductUsedXREFList.size() == 0 ) {
            return Collections.EMPTY_LIST;
        }
        for( UpdateConfigProductUsedXREF updateConfigProductUsedXREF : updateConfigProductUsedXREFList ) {
            listResult.add(prepareObject(updateConfigProductUsedXREF.getProductUsed()));
        }
        return listResult;
    }

    //把productUsed关联会无限循环的字段设为null
    private ProductUsed prepareObject(ProductUsed productUsed) {
        productUsed.setDeviceUsed(null);
        productUsed.setAttributes(null);
        return productUsed;
    }

    @ExceptionHandler({SQLException.class, Exception.class})
    protected void handleException(Exception ex, HttpServletResponse resp) {
        LOGGER.error(ex.getMessage(), ex);
    }
}
