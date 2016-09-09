package com.myee.tarot.web.apiold.controller.v10;

import com.myee.tarot.apiold.domain.MaterialBusiness;
import com.myee.tarot.apiold.domain.MaterialPublish;
import com.myee.tarot.apiold.service.MaterialBusinessService;
import com.myee.tarot.apiold.service.MaterialPublishService;
import com.myee.tarot.apiold.view.MaterialBusinessDTView;
import com.myee.tarot.core.Constants;
import com.myee.tarot.web.apiold.controller.BaseController;
import com.myee.tarot.web.ClientAjaxResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Info: 视频接口
 * User: chay.ni@clever-m.com
 * Date: 2016-08-22
 * Time: 15:29
 * Version: 1.0
 * History: <p>如果有修改过程，请记录</P>
 */
@RestController
//@Scope("prototype")
@RequestMapping("/api/v10/material")
public class MaterialManageController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(MaterialManageController.class);

    @Autowired
    private MaterialBusinessService materialBusinessManageService;
    @Autowired
    private MaterialPublishService materialPublishManageService;

    /**
     * 点点笔素材
     * @param orgId
     * @param type: 0或null只显示商户,1需要显示商业
     * @return
     */
    @RequestMapping(value = "/list")
    public ClientAjaxResult queryMaterialList(@RequestParam(value = "orgId") Long orgId,
                                              @RequestParam(value = "type", required = false) Integer type) {
        logger.info("查询点点笔素材列表,orgId:" + orgId);
        try {
            Date now = new Date();
            //获取本店素材列表,每个店铺限定一个
            List<MaterialBusiness> listBusiness = materialBusinessManageService.listByTypeStoreTime(orgId, Constants.API_OLD_TYPE_SHOP, now);
            List<MaterialPublish> listPublish = null;
            //获取推送商业视频列表
            if(type == Constants.API_OLD_TYPE_MUYE){
                listPublish = materialPublishManageService.listByStoreTime(orgId, now);
            }

            if( listBusiness == null && listPublish == null ){
                return ClientAjaxResult.success();
            }

            List<MaterialBusinessDTView> list = new ArrayList<MaterialBusinessDTView>();
            if(listBusiness != null && listBusiness.size() > 0){
                for(MaterialBusiness materialBusiness:listBusiness){
                    MaterialBusinessDTView materialBusinessDTView = new MaterialBusinessDTView(materialBusiness);
                    list.add(materialBusinessDTView);
                }
            }
            if(listPublish != null && listPublish.size() > 0){
                for(MaterialPublish materialPublish:listPublish){
                    MaterialBusinessDTView materialBusinessDTView = new MaterialBusinessDTView(materialPublish);
                    list.add(materialBusinessDTView);
                }
            }

            return ClientAjaxResult.success(list);
        }  catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return ClientAjaxResult.failed("糟了...系统出错了...");
        }
    }

    /**
     * 点点笔固件升级素材
     * @return
     */
    @RequestMapping(value = "/binList")
    public ClientAjaxResult queryBinMaterialList() {
        logger.info("查询点点固件升级素材列表" );
        try {
            //只查找出bin/img文件，且只查找type=1代表是木爷上传的商业素材
            List<MaterialBusiness> list = materialBusinessManageService.listByMaterialFileKind("bin/img");
            if(list == null){
                return ClientAjaxResult.success();
            }
            return ClientAjaxResult.success(list);
        }  catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return ClientAjaxResult.failed("糟了...系统出错了...");
        }
    }

}