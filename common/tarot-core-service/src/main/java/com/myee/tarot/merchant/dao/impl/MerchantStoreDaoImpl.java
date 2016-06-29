package com.myee.tarot.merchant.dao.impl;

import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.merchant.dao.MerchantStoreDao;
import com.myee.tarot.merchant.domain.MerchantStore;
import com.myee.tarot.merchant.domain.QMerchantStore;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.lang.StringUtils;
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

//    @Override
//    public Long getCountById(Long id) {
//        QMerchantStore qMerchantStore = QMerchantStore.merchantStore;
//        JPQLQuery<MerchantStore> query = new JPAQuery(getEntityManager());
//        query.from(qMerchantStore)
//                .where(qMerchantStore.id.eq(id));
//        log.info(query.fetchCount());
//
//        return query.fetchCount();
//    }

    @Override
    public Long getCountById(Long merchantStoreId, Long merchantId){
        QMerchantStore qMerchantStore = QMerchantStore.merchantStore;
        JPQLQuery<MerchantStore> query = new JPAQuery(getEntityManager());
        query.from(qMerchantStore);
        if(merchantStoreId != null){
            query.where(qMerchantStore.id.eq(merchantStoreId));
        }
        if(merchantId != null){
            query.where(qMerchantStore.merchant.id.eq(merchantId));
        }

        log.info(query.fetchCount());

        return query.fetchCount();
    }

//    @Override
//    public List<MerchantStore> listByMerchant(Long id) {
//        QMerchantStore qMerchantStore = QMerchantStore.merchantStore;
//        JPQLQuery<MerchantStore> query = new JPAQuery(getEntityManager());
//        query.from(qMerchantStore)
//                .where(qMerchantStore.merchant.id.eq(id));
//        log.info(query.fetchCount());
//
//        return query.fetch();
//
//    }

    @Override
    public PageResult<MerchantStore> pageListByMerchant(Long id ,PageRequest pageRequest ) {
        PageResult<MerchantStore> pageList = new PageResult<MerchantStore>();
        QMerchantStore qMerchantStore = QMerchantStore.merchantStore;
        JPQLQuery<MerchantStore> query = new JPAQuery(getEntityManager());

        if (StringUtils.isNotBlank(pageRequest.getQueryName())) {
            query.where(qMerchantStore.name.like("%" + pageRequest.getQueryName() + "%"));
        }
        if(id != null) {
            query.where(qMerchantStore.merchant.id.eq(id));
        }
        pageList.setRecordsTotal(query.from(qMerchantStore).fetchCount());
        pageList.setRecordsFiltered(query.from(qMerchantStore).fetchCount());
        if( pageRequest.getLength() > 0){
            query.offset(pageRequest.getStart()).limit(pageRequest.getLength());
        }
        pageList.setList(query.fetch());
        return pageList;
    }

}
