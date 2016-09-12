package com.myee.tarot.web.apiold.util;

import com.myee.tarot.admin.domain.AdminUser;
import com.myee.tarot.core.Constants;
import com.myee.tarot.customer.domain.Customer;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chay on 2016/9/9.
 */
public class CommonLoginParam {
    /**
     * 根据路径获取一些固定参数
     * @param request
     * @return
     */
    public static Map<String,Object> getRequestInfo(HttpServletRequest request){
        Map<String,Object> map = new HashMap<String, Object>();
        String path = request.getServletPath();
        Long userId = null;//查询时根据userId和type可以联合查询出创建者，不关联user表是因为有两个表
        if (path.contains("/admin/")) {
            map.put(Constants.REQUEST_INFO_SESSION ,Constants.ADMIN_STORE);
            map.put(Constants.REQUEST_INFO_USERTYPE,Constants.API_OLD_TYPE_MUYE);
            AdminUser user = (AdminUser)request.getSession().getAttribute(Constants.ADMIN_USER);
            if( null != user ){
                userId = user.getId();
            }
            map.put(Constants.REQUEST_INFO_USERID,userId);
        } else if (path.contains("/shop/")) {
            map.put(Constants.REQUEST_INFO_SESSION ,Constants.CUSTOMER_STORE);
            map.put(Constants.REQUEST_INFO_USERTYPE,Constants.API_OLD_TYPE_SHOP);
            Customer user = (Customer)request.getSession().getAttribute(Constants.CUSTOMER_USER);
            if( null != user ){
                userId = user.getId();
            }
            map.put(Constants.REQUEST_INFO_USERID,userId);
        }
        return map;
    }
}
