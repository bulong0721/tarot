package com.myee.tarot.merchant.dao.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.core.util.*;
import com.myee.tarot.merchant.dao.MerchantStoreDao;
import com.myee.tarot.merchant.domain.MerchantStore;
import com.myee.tarot.merchant.domain.QMerchantStore;
import com.myee.tarot.profile.domain.Address;
import com.myee.tarot.profile.domain.GeoZone;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

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
    public MerchantStore findById(Long merchanStoreId) {
        QMerchantStore qMerchantStore = QMerchantStore.merchantStore;
        JPQLQuery<MerchantStore> query = new JPAQuery(getEntityManager());
        query.from(qMerchantStore);
        if (merchanStoreId != null) {
            query.where(qMerchantStore.id.eq(merchanStoreId));
        }
        LOGGER.info("总条数=>",query.fetchCount());
        return query.fetchFirst();
    }

    @Override
    public List<MerchantStore> listByMerchantId(Long merchantId) {
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

    @Override
    public MerchantStore getByMerchantStoreName(String name) {
        QMerchantStore qMerchantStore = QMerchantStore.merchantStore;
        JPQLQuery<MerchantStore> query = new JPAQuery(getEntityManager());
        query.from(qMerchantStore);
        if (name != null) {
            query.where(qMerchantStore.merchantStore.name.eq(name));
        }
        return query.fetchFirst();
    }

    @Override
    public List<MerchantStore> listByIds(List<Long> bindList) {
        QMerchantStore qMerchantStore = QMerchantStore.merchantStore;
        JPQLQuery<MerchantStore> query = new JPAQuery(getEntityManager());
        query.from(qMerchantStore);
        if (bindList != null && bindList.size() > 0) {
            query.where(qMerchantStore.merchantStore.id.in(bindList));
        }
        return query.fetch();
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
    public PageResult<MerchantStore> pageListByMerchant(Long id, WhereRequest whereRequest) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, NoSuchFieldException {
        PageResult<MerchantStore> pageList = new PageResult<MerchantStore>();
        QMerchantStore qMerchantStore = QMerchantStore.merchantStore;
        JPQLQuery<MerchantStore> query = new JPAQuery(getEntityManager());
        if (whereRequest.getQueryObj() != null) {
            JSONObject map = JSON.parseObject(whereRequest.getQueryObj());
            Object optionName = map.get(Constants.SEARCH_OPTION_NAME);
            Object optionAddress = map.get(Constants.SEARCH_OPTION_ADDRESS);
            Object addressProvince = map.get(Constants.SEARCH_OPTION_ADDRESS_PROVINCE);
            Object addressCity = map.get(Constants.SEARCH_OPTION_ADDRESS_CITY);
            Object optionCode = map.get(Constants.SEARCH_OPTION_CODE);
            Object optionPhone = map.get(Constants.SEARCH_OPTION_PHONE);
            if (optionName != null && !StringUtil.isBlank(optionName.toString())) {
                query.where(qMerchantStore.name.like("%" + optionName + "%"));
            }
            if (optionAddress != null && !StringUtil.isBlank(optionAddress.toString())) {
                query.where(qMerchantStore.address.address.like("%" + optionAddress + "%"));
            }
            if (addressProvince != null && !StringUtil.isBlank(addressProvince.toString())) {
                query.where(qMerchantStore.address.province.id.eq(Long.valueOf(addressProvince.toString())));
            }
            if (addressCity != null && !StringUtil.isBlank(addressCity.toString())) {
                query.where(qMerchantStore.address.city.id.eq(Long.valueOf(addressCity + "%")));
            }
            if (optionCode != null && !StringUtil.isBlank(optionCode.toString())) {
                query.where(qMerchantStore.code.like("%" + optionCode + "%"));
            }
            if (optionPhone != null && !StringUtil.isBlank(optionPhone.toString())) {
                query.where(qMerchantStore.phone.like("%" + optionPhone + "%"));
            }
        } else {
            if (!StringUtil.isBlank(whereRequest.getQueryName())) {
                query.where(qMerchantStore.name.like("%" + whereRequest.getQueryName() + "%"));
            }
        }
        if (id != null) {
            query.where(qMerchantStore.merchant.id.eq(id));
        }
        pageList.setRecordsTotal(query.from(qMerchantStore).fetchCount());
        if (whereRequest.getCount() > Constants.COUNT_PAGING_MARK) {
            query.offset(whereRequest.getOffset()).limit(whereRequest.getCount());
        }
        query.orderBy(qMerchantStore.merchantStore.name.asc());
        pageList.setList(query.fetch());
        return pageList;
    }
}
