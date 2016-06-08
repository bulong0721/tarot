package com.myee.tarot.merchant.dao.impl;

import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.merchant.dao.MerchantStoreDao;
import com.myee.tarot.merchant.domain.MerchantStore;
import com.myee.tarot.merchant.domain.QMerchantStore;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Martin on 2016/4/21.
 */
@Repository
public class MerchantStoreDaoImpl extends GenericEntityDaoImpl<Long, MerchantStore> implements MerchantStoreDao {
    public static Log log = LogFactory.getLog(MerchantDaoImpl.class);

    @Override
    public Long getCountById(MerchantStore merchantStore){
        QMerchantStore qMerchantStore = QMerchantStore.merchantStore;
        JPQLQuery<MerchantStore> query = new JPAQuery(getEntityManager());
        query.from(qMerchantStore)
                .where(qMerchantStore.id.eq(merchantStore.getId()));
        log.info(query.fetchCount());

        return query.fetchCount();
    }

    @Override
    public List<MerchantStore> listByMerchant(Long id){
        QMerchantStore qMerchantStore = QMerchantStore.merchantStore;
        JPQLQuery<MerchantStore> query = new JPAQuery(getEntityManager());
        query.from(qMerchantStore)
                .where(qMerchantStore.merchant.id.eq( id  ));
        log.info(query.fetchCount());

        return query.fetch();

    }
}
