package com.myee.tarot.web.admin.controller.merchant;

import com.myee.tarot.core.util.ajax.AjaxResponse;
import com.myee.tarot.merchant.domain.Merchant;
import com.myee.tarot.merchant.service.MerchantService;
import com.myee.tarot.web.util.DateUtil;
import com.myee.tarot.web.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Martin on 2016/4/21.
 */
@Controller
public class MerchantController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MerchantController.class);

    @Autowired
    private MerchantService merchantService;

    @RequestMapping(value = "/admin/merchant/save.html", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse saveMerchant(@ModelAttribute Merchant merchant) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        try {
            merchantService.save(merchant);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setErrorString("新增出错");
        }
        return resp;
    }

    @RequestMapping(value = "/admin/merchant/edit.html", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse editMerchant(@ModelAttribute Merchant merchant) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        try {
            Long numFind = merchantService.getCountById( merchant);
            if (numFind <= 0L) {
                //抛出异常给异常处理机制
                resp.setErrorString("要修改的商户不存在");
                return resp;
            }
            Merchant merchantOld = merchantService.getEntity(Merchant.class,merchant.getId());
            merchantOld.setName(StringUtil.nullToString(merchant.getName())); //不能为空
            merchantOld.setLogo(merchant.getLogo());
            merchantOld.setCuisineType(StringUtil.nullToString(merchant.getCuisineType()));//不能为空
            merchantOld.setDescription(merchant.getDescription());
            merchantOld.setBusinessType(StringUtil.nullToString(merchant.getBusinessType()));//不能为空
            merchantService.save(merchantOld);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setErrorString("更新出错");
        }
        return resp;
    }

    @RequestMapping(value = "/admin/merchant/get.html", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse getMerchant(@RequestParam Long id) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        try {
            Merchant merchant1 = merchantService.getEntity(Merchant.class, id);
            resp.addDataEntry(objectToEntry(merchant1));
        } catch (Exception e) {
            e.printStackTrace();
            resp.setErrorString("出错");
        }
        return resp;
    }

    @RequestMapping(value = "/admin/merchant/delete.html", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse deleteMerchant(@RequestParam Long id) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        try {
            Merchant merchant = merchantService.getEntity(Merchant.class,id);
            if(merchant!= null)merchantService.delete(merchant);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setErrorString("删除出错");
        }
        return resp;
    }

    @RequestMapping(value = "/admin/merchant/list.html", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse listMerchant() throws Exception {
        AjaxResponse resp = new AjaxResponse();
        try {
            List<Merchant> merchantList = merchantService.list();
            for (Merchant merchant : merchantList) {
                resp.addDataEntry(objectToEntry(merchant));
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.setErrorString("出错");
        }
        return resp;
    }

    //把类转换成entry返回给前端，解耦和
    private Map objectToEntry(Merchant merchant){
        Map entry = new HashMap();
        entry.put("id",merchant.getId());
        entry.put("name", merchant.getName());
        entry.put("businessType", merchant.getBusinessType());
        entry.put("cuisineType", merchant.getCuisineType());
        entry.put("logo", merchant.getLogo());
        entry.put("description", merchant.getDescription());
        return entry;
    }


}
