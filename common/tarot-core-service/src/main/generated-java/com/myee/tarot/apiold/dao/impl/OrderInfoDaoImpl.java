package com.myee.tarot.apiold.dao.impl;

import com.myee.tarot.apiold.dao.OrderInfoDao;
import com.myee.tarot.apiold.domain.OrderInfo;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import org.springframework.stereotype.Repository;

/**
 * Created by Chay on 2016/8/10.
 */
@Repository
public class OrderInfoDaoImpl extends GenericEntityDaoImpl<Long, OrderInfo> implements OrderInfoDao {
}
