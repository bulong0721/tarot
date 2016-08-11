package com.myee.tarot.web.apiold.api.controller.v10;

import com.google.common.collect.Lists;
import com.myee.tarot.apiold.domain.BaseDataInfo;
import com.myee.tarot.campaign.service.impl.redis.RedisUtil;
import com.myee.tarot.catering.domain.Table;
import com.myee.tarot.catering.domain.TableType;
import com.myee.tarot.catering.service.TableService;
import com.myee.tarot.catering.service.TableTypeService;
import com.myee.tarot.web.apiold.api.controller.BaseController;
import com.myee.tarot.weixin.domain.ClientAjaxResult;
import me.chanjar.weixin.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Info: 商户餐桌管理接口
 * User: Gary.zhang@clever-m.com
 * Date: 2016-01-20
 * Time: 14:29
 * Version: 1.0
 * History: <p>如果有修改过程，请记录</P>
 */
@RestController
@Scope("prototype")
public class TableInfoController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(TableInfoController.class);

    @Autowired
    private TableService tableService;
    @Autowired
    private TableTypeService tableTypeService;
    @Autowired
    private RedisUtil redisUtil;


    /**
     * 商户餐桌列表
     * @param shopId
     * @return
     */
    @RequestMapping(value = "/api/v10/table/list")
    public ClientAjaxResult queryTableInfoList(@RequestParam(value = "shopId") String shopId,
                                              @RequestParam(value = "timestamp", required = false) Long timestamp) {
        logger.info("查询商户餐桌信息,shopId:"+shopId);
        try {

            long id = 0l;
            if(StringUtils.isNotEmpty(shopId)){
                id = Long.parseLong(shopId);
            } else {
                return ClientAjaxResult.failed("商户ID不能为空");
            }
            if(timestamp==null){
                timestamp = 0l;
            }

            BaseDataInfo tableInfos = redisUtil.get("shop:table:info:"+shopId, BaseDataInfo.class);
            if (tableInfos!=null){
                if(timestamp == tableInfos.getTimestamp()){
                    tableInfos.setList(null);
                    tableInfos.setList2(null);
                    tableInfos.setTimestamp(timestamp);
                }
            } else {
                List<Table> list = tableService.listByStore(id);
                List<TableType> tableTypes = tableTypeService.listByStore(id);

                if (list != null) {
                    List<Map<Object,Object>> resultList = Lists.newArrayList();//将我们的查询结果转换成兼容以前接口规范的结果
                    for (Table table : list) {
                        Map entry = new HashMap();
                        entry.put("id", table.getId());
                        entry.put("name", table.getName());
                        entry.put("typeId", table.getTableType().getId());
                        entry.put("textId", table.getTextId());
                        entry.put("scanCode", table.getScanCode());
                        resultList.add(entry);
                    }

                    tableInfos = new BaseDataInfo();
                    tableInfos.setList(resultList);

                    List<Map<Object,Object>> resultList2 = Lists.newArrayList();//将我们的查询结果转换成兼容以前接口规范的结果
                    if(tableTypes != null){
                        for(TableType tableType : tableTypes){
                            Map entry = new HashMap();
                            entry.put("id", tableType.getId());
                            entry.put("name", tableType.getName());
                            entry.put("description", tableType.getDescription());
                            resultList2.add(entry);
                        }
                    }
                    tableInfos.setList2(resultList2);
                }
                tableInfos.setTimestamp(new Date().getTime());
                redisUtil.set("shop:table:info:" + shopId, tableInfos, 365, TimeUnit.DAYS);
            }

            Map<Object,Object> entity = new HashMap<Object,Object>();
            entity.put("timestamp",tableInfos.getTimestamp());
            entity.put("list",tableInfos.getList());
            entity.put("tableTypeList",tableInfos.getList2());
            return ClientAjaxResult.success(entity);
        }  catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return ClientAjaxResult.failed("糟了...系统出错了...");
        }
    }


}