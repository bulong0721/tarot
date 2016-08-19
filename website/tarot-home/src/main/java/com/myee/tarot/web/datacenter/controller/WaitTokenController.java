package com.myee.tarot.web.datacenter.controller;

import com.myee.tarot.catering.domain.TableType;
import com.myee.tarot.catering.service.TableTypeService;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.WhereRequest;
import com.myee.tarot.core.util.ajax.AjaxPageableResponse;
import com.myee.tarot.datacenter.service.WaitTokenService;
import com.myee.tarot.weixin.domain.WxWaitToken;
import com.myee.tarot.weixin.domain.WxWaitTokenState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Martin on 2016/4/21.
 */
@Controller
public class WaitTokenController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WaitTokenController.class);

    @Autowired
    private WaitTokenService waitTokenService;

    @Autowired
    private TableTypeService tableTypeService;
    /**
     * 查询排队等位数据
     * @param model
     * @param request
     * @param whereRequest
     * @return
     */
    @RequestMapping(value = "data/waittoken/paging", method = RequestMethod.GET)
    @ResponseBody
    public AjaxPageableResponse pageWaitToken(Model model, HttpServletRequest request, WhereRequest whereRequest) {
        AjaxPageableResponse resp = new AjaxPageableResponse();
        try {
            PageResult<WxWaitToken> pageList = waitTokenService.page(whereRequest);
            List<WxWaitToken> wxWaitTokenList = pageList.getList();
            for (WxWaitToken wxWaitToken : wxWaitTokenList) {
                resp.addDataEntry(objectToEntry(wxWaitToken));
            }
            resp.setRecordsTotal(pageList.getRecordsTotal());
        } catch (Exception e) {
            LOGGER.error("Error while paging waittoken", e);
        }
        return resp;
    }

    //把类转换成entry返回给前端，解耦和
    private Map objectToEntry(WxWaitToken wxWaitToken) {
        TableType tableType = tableTypeService.findById(wxWaitToken.getTableTypeId());
        Map entry = new HashMap();
        entry.put("id",wxWaitToken.getId());
        if(tableType != null){
            entry.put("tableType",tableType.getName());
        }
        entry.put("token",wxWaitToken.getToken());
        entry.put("dinnerCount",wxWaitToken.getDinnerCount());
        entry.put("comment",wxWaitToken.getComment());
        entry.put("timeTook",wxWaitToken.getTimeTook());
        entry.put("state", new WxWaitTokenState().getWxWaitTokenState(wxWaitToken.getState()+""));
        entry.put("waitedCount", wxWaitToken.getWaitedCount());
        entry.put("predictWaitingTime",wxWaitToken.getPredictWaitingTime());
        entry.put("clientID", wxWaitToken.getMerchantId());
        entry.put("orgID",wxWaitToken.getMerchantStoreId());
        entry.put("updated",wxWaitToken.getUpdated());
        return entry;
    }

}
