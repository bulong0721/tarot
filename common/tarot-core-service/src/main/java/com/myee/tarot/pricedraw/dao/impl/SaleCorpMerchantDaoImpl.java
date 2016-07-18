package com.myee.tarot.pricedraw.dao.impl;

import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.pricedraw.dao.SaleCorpMerchantDao;
import com.myee.tarot.pricedraw.domain.MerchantPrice;
import com.myee.tarot.pricedraw.domain.QSaleCorpMerchant;
import com.myee.tarot.pricedraw.domain.SaleCorpMerchant;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2016/7/13.
 */
@Repository
public class SaleCorpMerchantDaoImpl extends GenericEntityDaoImpl<Long, SaleCorpMerchant> implements SaleCorpMerchantDao{
    @Override
    public SaleCorpMerchant findByMerchantId(Long merchantId) {
        QSaleCorpMerchant qSaleCorpMerchant = QSaleCorpMerchant.saleCorpMerchant;
        JPQLQuery<SaleCorpMerchant> query = new JPAQuery(getEntityManager());
        SaleCorpMerchant result = query.from(qSaleCorpMerchant).where(qSaleCorpMerchant.merchantId.eq(merchantId)).fetchOne();
        return result;
    }
}
