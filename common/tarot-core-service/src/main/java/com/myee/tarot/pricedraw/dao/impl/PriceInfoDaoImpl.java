package com.myee.tarot.pricedraw.dao.impl;

import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.pricedraw.dao.PriceInfoDao;
import com.myee.tarot.pricedraw.domain.PriceInfo;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2016/7/14.
 */
@Repository
public class PriceInfoDaoImpl extends GenericEntityDaoImpl<Long ,PriceInfo> implements PriceInfoDao {
}
