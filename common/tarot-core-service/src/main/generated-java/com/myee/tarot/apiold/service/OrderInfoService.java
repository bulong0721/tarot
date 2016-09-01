package com.myee.tarot.apiold.service;

import com.myee.tarot.apiold.domain.OrderInfo;
import com.myee.tarot.core.service.GenericEntityService;

/**
 * Created by Chay on 2016/8/10.
 */
public interface OrderInfoService extends GenericEntityService<Long, OrderInfo> {
    int createOrder(OrderInfo orderInfo);

    OrderInfo getById(String orderId);
}
