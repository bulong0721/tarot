package com.myee.tarot.apiold.service.impl;

import com.myee.tarot.apiold.dao.OrderInfoDao;
import com.myee.tarot.apiold.domain.OrderInfo;
import com.myee.tarot.apiold.service.OrderInfoService;
import com.myee.tarot.core.service.GenericEntityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Chay on 2016/8/10.
 */
@Service
public class OrderInfoServiceImpl extends GenericEntityServiceImpl<Long, OrderInfo> implements OrderInfoService {

    protected OrderInfoDao orderInfoDao;

    @Autowired
    public OrderInfoServiceImpl(OrderInfoDao orderInfoDao) {
        super(orderInfoDao);
        this.orderInfoDao = orderInfoDao;
    }

    public int createOrder(OrderInfo orderInfo){
        return 0;
    }

    public OrderInfo getById(String orderId){
        return null;
    }
}
