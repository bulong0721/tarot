package com.myee.tarot.pricedraw.dao.impl;

import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.pricedraw.dao.MerchantPriceDao;
import com.myee.tarot.pricedraw.domain.MerchantActivity;
import com.myee.tarot.pricedraw.domain.MerchantPrice;
import com.myee.tarot.pricedraw.domain.QMerchantPrice;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2016/7/12.
 */
@Repository
public class MerchantPriceDaoImpl extends GenericEntityDaoImpl<Long , MerchantPrice> implements MerchantPriceDao {
    @Override
    public List<MerchantPrice> findPriceByActivityId(Long activityId) {
        QMerchantPrice qMerchantPrice = QMerchantPrice.merchantPrice;
        JPQLQuery<MerchantPrice> query = new JPAQuery(getEntityManager());
        List<MerchantPrice> result = query.from(qMerchantPrice).where(qMerchantPrice.activity.id.eq(activityId)).fetch();
        return result;
    }
}
