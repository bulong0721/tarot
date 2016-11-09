package com.myee.tarot.web.apiold.controller.v10;

import com.myee.djinn.dto.WaitToken;
import com.myee.djinn.rpc.RemoteException;
import com.myee.djinn.server.operations.MealsService;
import com.myee.tarot.apiold.domain.MenuInfo;
import com.myee.tarot.apiold.service.MenuService;
import com.myee.tarot.apiold.view.MenuDataInfo;
import com.myee.tarot.apiold.view.MenuInfoView;
import com.myee.tarot.cache.uitl.RedissonUtil;
import com.myee.tarot.core.util.AutoNumUtil;
import com.myee.tarot.web.ClientAjaxResult;
import com.myee.tarot.web.apiold.controller.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Info: 商户菜品管理接口
 * User: Gary.zhang@clever-m.com
 * Date: 2016-01-20
 * Time: 14:29
 * Version: 1.0
 * History: <p>如果有修改过程，请记录</P>
 */
@RestController
//@Scope("prototype")
@RequestMapping("/api/v10/menu")
public class MenuManageController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(MenuManageController.class);

    @Autowired
    private MenuService    menuManageService;
    @Autowired
    private RedissonClient redisson;
    @Autowired
    private MealsService mealsService;


    /**
     * 商户菜品列表
     *
     * @param shopId
     * @return
     */
    @RequestMapping(value = "/list")
    public ClientAjaxResult queryShopMenuList(@RequestParam(value = "shopId", required = false) String shopId,
                                              @RequestParam(value = "timestamp", required = false) long timestamp) {
        logger.info("查询商户菜品信息,shopId:" + shopId + ",timestamp:" + timestamp);
        try {

            long id = 0l;
            if (StringUtils.isNotEmpty(shopId)) {
                id = Long.parseLong(shopId);
            }
            Map<String, MenuDataInfo> menuInfoCache = RedissonUtil.commonCache(redisson).getMenuCache();
            MenuDataInfo shopMenu = menuInfoCache.get(shopId);
            if (shopMenu != null) {
                if (timestamp == shopMenu.getTimestamp()) {
                    shopMenu.setList(null);
                    shopMenu.setTimestamp(timestamp);
                }
            } else {
                //如果为空，从数据库中查询数据
                List<MenuInfoView> list = null;
                List<MenuInfo> listMenuInfo = menuManageService.listByStoreId(id);
                if (listMenuInfo != null && listMenuInfo.size() > 0) {
                    list = new ArrayList<MenuInfoView>();
                    for (MenuInfo menuInfo : listMenuInfo) {
                        MenuInfoView menuInfoView = new MenuInfoView(menuInfo);
                        list.add(menuInfoView);
                    }
                }
                shopMenu = new MenuDataInfo();
                if (list != null) {
                    shopMenu.setList(list);
                }
                shopMenu.setTimestamp(new Date().getTime());
                menuInfoCache.put(shopId, shopMenu);
            }

            return ClientAjaxResult.success(shopMenu);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return ClientAjaxResult.failed("糟了...系统出错了...");
        }
    }

    @RequestMapping(value = "test")
    public ClientAjaxResult test(){
        try {
            WaitToken waitToken = new WaitToken();
            waitToken.setTableTypeId(4L);
            waitToken.setShopId(100L);
            waitToken.setIdentityCode(AutoNumUtil.getCode(6, 3));
            waitToken.setToken("A2");
            waitToken.setWaitStatus(0);
            mealsService.takeNumber(waitToken);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return ClientAjaxResult.failed();
    }

    @RequestMapping(value = "test1")
    public ClientAjaxResult test1(){
        try {
            WaitToken waitToken = new WaitToken();
            waitToken.setTableTypeId(4L);
            waitToken.setShopId(100L);
            waitToken.setIdentityCode(AutoNumUtil.getCode(6, 3));
            waitToken.setToken("A1");
            waitToken.setWaitStatus(1);
            mealsService.skipNumber(waitToken);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return ClientAjaxResult.failed();
    }


}