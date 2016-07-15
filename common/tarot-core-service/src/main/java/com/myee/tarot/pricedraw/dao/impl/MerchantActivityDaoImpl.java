package com.myee.tarot.pricedraw.dao.impl;

import com.myee.tarot.admin.domain.AdminUser;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.pricedraw.dao.MerchantActivityDao;
import com.myee.tarot.pricedraw.domain.MerchantActivity;
import com.myee.tarot.pricedraw.domain.QMerchantActivity;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2016/7/11.
 */
@Repository
public class MerchantActivityDaoImpl extends GenericEntityDaoImpl<Long, MerchantActivity> implements MerchantActivityDao{
    @Override
    public List<MerchantActivity> findStoreActivity(Long storeId) {
        QMerchantActivity qMerchantActivity = QMerchantActivity.merchantActivity;
        JPQLQuery<MerchantActivity> query = new JPAQuery(getEntityManager());
        List<MerchantActivity> result = query.from(qMerchantActivity).where(qMerchantActivity.store.id.eq(storeId)).fetch();
        return result;
    }
}
