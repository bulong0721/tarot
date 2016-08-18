package com.myee.tarot.merchant.dao.impl;

import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.StringUtil;
import com.myee.tarot.merchant.dao.MerchantStoreDao;
import com.myee.tarot.merchant.domain.MerchantStore;
import com.myee.tarot.merchant.domain.QMerchantStore;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Martin on 2016/4/21.
 */
@Repository
public class MerchantStoreDaoImpl extends GenericEntityDaoImpl<Long, MerchantStore> implements MerchantStoreDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(MerchantDaoImpl.class);

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
    public Long getCountById(Long merchantStoreId, Long merchantId) {
        QMerchantStore qMerchantStore = QMerchantStore.merchantStore;
        JPQLQuery<MerchantStore> query = new JPAQuery(getEntityManager());
        query.from(qMerchantStore);
        if (merchantStoreId != null) {
            query.where(qMerchantStore.id.eq(merchantStoreId));
        }
        if (merchantId != null) {
            query.where(qMerchantStore.merchant.id.eq(merchantId));
        }

        LOGGER.info("总条数=>",query.fetchCount());

        return query.fetchCount();
    }

    @Override
    public MerchantStore findOne(Long merchantStoreId) {
        QMerchantStore qMerchantStore = QMerchantStore.merchantStore;
        JPQLQuery<MerchantStore> query = new JPAQuery(getEntityManager());
        query.from(qMerchantStore);
        if (merchantStoreId != null) {
            query.where(qMerchantStore.id.eq(merchantStoreId));
        }
        LOGGER.info("总条数=>",query.fetchCount());
        return query.fetchFirst();
    }

    @Override
    public List<MerchantStore> selectAllByMerchantId(Long merchantId) {
        QMerchantStore qMerchantStore = QMerchantStore.merchantStore;
        JPQLQuery<MerchantStore> query = new JPAQuery(getEntityManager());
        query.from(qMerchantStore);
        if (merchantId != null) {
            query.where(qMerchantStore.merchant.id.eq(merchantId));
        }
        LOGGER.info("总条数=>",query.fetchCount());
        return query.fetch();
    }

    @Override
    public MerchantStore getByCode(String storeCode) {
        QMerchantStore qMerchantStore = QMerchantStore.merchantStore;
        JPQLQuery<MerchantStore> query = new JPAQuery(getEntityManager());
        query.from(qMerchantStore);
        if (storeCode != null) {
            query.where(qMerchantStore.merchantStore.code.eq(storeCode));
        }
        return query.fetchFirst();
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
    public PageResult<MerchantStore> pageListByMerchant(Long id, PageRequest pageRequest) {
        PageResult<MerchantStore> pageList = new PageResult<MerchantStore>();
        QMerchantStore qMerchantStore = QMerchantStore.merchantStore;
        JPQLQuery<MerchantStore> query = new JPAQuery(getEntityManager());

        if (!StringUtil.isBlank(pageRequest.getQueryName())) {
            query.where(qMerchantStore.name.like("%" + pageRequest.getQueryName() + "%"));
        }
        if (id != null) {
            query.where(qMerchantStore.merchant.id.eq(id));
        }
        pageList.setRecordsTotal(query.from(qMerchantStore).fetchCount());
        if (pageRequest.getCount() > 0) {
            query.offset(pageRequest.getOffset()).limit(pageRequest.getCount());
        }
        pageList.setList(query.fetch());
        return pageList;
    }

}
