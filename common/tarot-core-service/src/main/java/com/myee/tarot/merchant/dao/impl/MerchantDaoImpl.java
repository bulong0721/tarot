package com.myee.tarot.merchant.dao.impl;

import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.merchant.dao.MerchantDao;
import com.myee.tarot.merchant.domain.Merchant;
import com.myee.tarot.merchant.domain.QMerchant;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.hibernate.HibernateQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

/**
 * Created by Chay on 2016/5/25.
 */
@Repository
public class MerchantDaoImpl extends GenericEntityDaoImpl<Long, Merchant> implements MerchantDao {

    public static Log log = LogFactory.getLog(MerchantDaoImpl.class);

    @Override
    public Long getCountById(Long id){
        QMerchant qMerchant = QMerchant.merchant;
        JPQLQuery<Merchant> query = new JPAQuery(getEntityManager());
        query.from(qMerchant)
                .where(qMerchant.id.eq(id));
        log.info(query.fetchCount());

        return query.fetchCount();
    }
}
