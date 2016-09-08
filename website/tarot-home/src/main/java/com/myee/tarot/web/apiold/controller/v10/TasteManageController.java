package com.myee.tarot.web.apiold.controller.v10;

import com.myee.tarot.apiold.domain.BaseDataInfo;
import com.myee.tarot.apiold.domain.TasteInfo;
import com.myee.tarot.apiold.service.TasteService;
import com.myee.tarot.cache.uitl.RedissonUtil;
import com.myee.tarot.web.apiold.controller.BaseController;
import com.myee.tarot.web.ClientAjaxResult;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Info: 小超人菜品口味管理
 * User: Gary.zhang@clever-m.com
 * Date: 2016-02-04
 * Time: 15:12
 * Version: 1.0
 * History: <p>如果有修改过程，请记录</P>
 */
@RestController
@Scope("prototype")
@RequestMapping("/api/v10/taste")
public class TasteManageController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(TasteManageController.class);

    @Autowired
    private TasteService   tasteManageService;
    @Autowired
    private RedissonClient redisson;


    /**
     * 菜品口味列表
     * @return
     */
    @RequestMapping(value = "/list")
    public ClientAjaxResult queryMenuTasteList(@RequestParam(value = "timestamp", required = false) long timestamp) {
        logger.info("查询菜品口味信息:");
        try {

            //BaseDataInfo baseData = redisUtil.get("system:menu:taste", BaseDataInfo.class);
            //if (baseData == null) {
                //如果为空，从数据库中查询数据

//            BaseDataInfo baseData = redisUtil.get("system:menu:taste", BaseDataInfo.class);
            Map<String, BaseDataInfo> tasteInfoCache = RedissonUtil.commonCache(redisson).getTasteCache();
            BaseDataInfo baseData = tasteInfoCache.get("menu_taste");
            if (baseData!=null) {
                if(timestamp == baseData.getTimestamp()){
                    baseData.setList(null);
                    baseData.setTimestamp(timestamp);
                }
            } else {
                List<TasteInfo> list = tasteManageService.list();
                if (list != null) {
                    baseData = new BaseDataInfo();
                    baseData.setList(list);
                }
                baseData.setTimestamp(new Date().getTime());
//                redisUtil.set("system:menu:taste", baseData, 365, TimeUnit.DAYS);
                tasteInfoCache.put("menu_taste", baseData);
            }

            return ClientAjaxResult.success(baseData);
        }  catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return ClientAjaxResult.failed("糟了...系统出错了...");
        }
    }


}