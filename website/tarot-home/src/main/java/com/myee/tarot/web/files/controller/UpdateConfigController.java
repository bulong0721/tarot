package com.myee.tarot.web.files.controller;

import com.myee.djinn.dto.NoticeType;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.util.*;
import com.myee.tarot.core.util.ajax.AjaxPageableResponse;
import com.myee.tarot.core.util.ajax.AjaxResponse;
import com.myee.tarot.resource.domain.UpdateConfig;
import com.myee.tarot.resource.service.UpdateConfigService;
import com.myee.tarot.resource.type.UpdateConfigSeeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
            resp.addEntry("updateResult", objectToEntry(updateConfig));
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
        return entry;
    }

    @ExceptionHandler({SQLException.class, Exception.class})
    protected void handleException(Exception ex, HttpServletResponse resp) {
        LOGGER.error(ex.getMessage(), ex);
    }
}
