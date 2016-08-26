package com.myee.tarot.web.apiold.controller.v10;

import com.google.common.collect.Lists;
import com.myee.tarot.apiold.domain.BaseDataInfo;
import com.myee.tarot.catering.domain.TableType;
import com.myee.tarot.catering.service.TableTypeService;
import com.myee.tarot.uitl.CacheUtil;
import com.myee.tarot.web.apiold.controller.BaseController;
import com.myee.tarot.weixin.domain.ClientAjaxResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.ignite.Ignite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.cache.Cache;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Info: 商户餐桌类型管理接口
 * User: Gary.zhang@clever-m.com
 * Date: 2016-02-04
 * Time: 14:29
 * Version: 1.0
 * History: <p>如果有修改过程，请记录</P>
 */
@RestController
@RequestMapping("/api/v10/tableType")
public class TableTypeController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(TableTypeController.class);

    @Autowired
    private TableTypeService tableTypeService;
    @Autowired
    private Ignite ignite;

    /**
     * 商户菜品列表
     * @param shopId
     * @return
     */
    @RequestMapping(value = "/list")
    public ClientAjaxResult queryTableTypeList(@RequestParam(value = "shopId") String shopId,
                                              @RequestParam(value = "timestamp", required = false) Long timestamp) {
        logger.info("查询商户餐桌类型信息,shopId:"+shopId);
        try {

            long id = 0l;
            if(StringUtils.isNotEmpty(shopId)){
                id = Long.parseLong(shopId);
            } else {
                return ClientAjaxResult.failed("商户ID不能为空");
            }
            if (timestamp == null) {
                timestamp = 0l;
            }

            Cache<String, BaseDataInfo> tableTypeInfoCache = CacheUtil.tableTypeInfoCache(ignite);
            BaseDataInfo tableTypes = tableTypeInfoCache.get(shopId);
            if (tableTypes!=null) {
                if(timestamp == tableTypes.getTimestamp()){
                    tableTypes.setList(null);
                    tableTypes.setTimestamp(timestamp);
                }
            } else {
                logger.info("query id " + id);
                List<TableType> list = tableTypeService.listByStore(id);
                if (list != null) {
                    List<Map<Object, Object>> resultList = Lists.newArrayList();
                    for (TableType tableType : list) {
                        Map entry = new HashMap();
                        entry.put("id", tableType.getId());
                        entry.put("name", tableType.getName());
                        entry.put("description", tableType.getDescription());
                        resultList.add(entry);
                    }
                    tableTypes = new BaseDataInfo();
                    tableTypes.setList(resultList);
                }
                tableTypes.setTimestamp(new Date().getTime());
                tableTypeInfoCache.put(shopId, tableTypes);
            }

            return ClientAjaxResult.success(tableTypes);
        }  catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return ClientAjaxResult.failed("糟了...系统出错了...");
        }
    }


}