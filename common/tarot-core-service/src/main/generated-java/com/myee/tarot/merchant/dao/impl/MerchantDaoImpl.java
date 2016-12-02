package com.myee.tarot.merchant.dao.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.StringUtil;
import com.myee.tarot.core.util.WhereRequest;
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
    public PageResult<Merchant> pageList(WhereRequest whereRequest) {
        PageResult<Merchant> pageList = new PageResult<Merchant>();
        QMerchant qMerchant = QMerchant.merchant;
        JPQLQuery<Merchant> query = new JPAQuery(getEntityManager());
        query.from(qMerchant);
        if (whereRequest.getQueryObj() != null) {
            JSONObject map = JSON.parseObject(whereRequest.getQueryObj());
            if (map.get(Constants.SEARCH_OPTION_NAME) != null && !StringUtil.isBlank(map.get(Constants.SEARCH_OPTION_NAME).toString())) {
                query.where(qMerchant.name.like("%" + map.get(Constants.SEARCH_OPTION_NAME) + "%"));
            }
            if (map.get(Constants.SEARCH_OPTION_BUSINESS_TYPE) != null && !StringUtil.isBlank(map.get(Constants.SEARCH_OPTION_BUSINESS_TYPE).toString())) {
                query.where(qMerchant.businessType.eq(map.get(Constants.SEARCH_OPTION_BUSINESS_TYPE).toString()));
            }
            if (map.get(Constants.SEARCH_OPTION_CUISINE_TYPE) != null && !StringUtil.isBlank(map.get(Constants.SEARCH_OPTION_CUISINE_TYPE).toString())) {
                query.where(qMerchant.cuisineType.eq(map.get(Constants.SEARCH_OPTION_CUISINE_TYPE).toString()));
            }
            if (map.get(Constants.SEARCH_OPTION_DESCRIPTION) != null && !StringUtil.isBlank(map.get(Constants.SEARCH_OPTION_DESCRIPTION).toString())) {
                query.where(qMerchant.description.like("%" + map.get(Constants.SEARCH_OPTION_DESCRIPTION) + "%"));
            }
        } else {
            if(!StringUtil.isBlank(whereRequest.getQueryName())){
                query.where(qMerchant.name.like("%" + whereRequest.getQueryName() + "%"));
            }
        }
        query.orderBy(qMerchant.merchant.name.asc());
        pageList.setRecordsTotal(query.fetchCount());
        if( whereRequest.getCount() > Constants.COUNT_PAGING_MARK){
            query.offset(whereRequest.getOffset()).limit(whereRequest.getCount());
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
