package com.myee.tarot.merchant.dao.impl;

import com.myee.tarot.core.Constants;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.StringUtil;
import com.myee.tarot.merchant.dao.MerchantDao;
import com.myee.tarot.merchant.domain.Merchant;
import com.myee.tarot.merchant.domain.QMerchant;
import com.querydsl.jpa.JPQLQuery;
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

    @Override
    public PageResult<Merchant> pageList(PageRequest pageRequest) {
        PageResult<Merchant> pageList = new PageResult<Merchant>();
        QMerchant qMerchant = QMerchant.merchant;
        JPQLQuery<Merchant> query = new JPAQuery(getEntityManager());
        query.from(qMerchant);
        if(!StringUtil.isBlank(pageRequest.getQueryName())){
            query.where(qMerchant.name.like("%" + pageRequest.getQueryName() + "%"));
        }
        pageList.setRecordsTotal(query.fetchCount());
        if( pageRequest.getCount() > Constants.COUNT_PAGING_MARK){
            query.offset(pageRequest.getOffset()).limit(pageRequest.getCount());
        }
        pageList.setList(query.fetch());

        return pageList;
    }

    @Override
    public Merchant getByMerchantName(String name) {
        QMerchant qMerchant = QMerchant.merchant;
        JPQLQuery<Merchant> query = new JPAQuery(getEntityManager());
        query.from(qMerchant)
                .where(qMerchant.name.eq(name));
        log.info(query.fetchCount());
        return query.fetchFirst();
    }

}
