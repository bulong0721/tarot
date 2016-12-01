package com.myee.tarot.catalog.dao.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.myee.tarot.catalog.domain.ProductUsed;
import com.myee.tarot.catalog.domain.QProductUsed;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.StringUtil;
import com.myee.tarot.catalog.dao.ProductUsedDao;
import com.myee.tarot.core.util.WhereRequest;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Enva on 2016/6/1.
 */
@Repository
public class ProductUsedDaoImpl extends GenericEntityDaoImpl<Long, ProductUsed> implements ProductUsedDao {

    @Override
    public PageResult<ProductUsed> pageList(PageRequest pageRequest) {
        PageResult<ProductUsed> pageList = new PageResult<ProductUsed>();
        QProductUsed qProductUsed = QProductUsed.productUsed;
        JPQLQuery<ProductUsed> query = new JPAQuery(getEntityManager());
        query.from(qProductUsed);
        if (!StringUtil.isBlank(pageRequest.getQueryName())) {
            query.where(qProductUsed.code.like("%" + pageRequest.getQueryName() + "%"));
        }
        pageList.setRecordsTotal(query.fetchCount());
        if( pageRequest.getCount() > Constants.COUNT_PAGING_MARK){
            query.offset(pageRequest.getOffset()).limit(pageRequest.getCount());
        }
        pageList.setList(query.fetch());
        return pageList;
    }

    @Override
    public PageResult<ProductUsed> pageByStore(Long id, WhereRequest whereRequest) {
        PageResult<ProductUsed> pageList = new PageResult<ProductUsed>();
        QProductUsed qProductUsed = QProductUsed.productUsed;
        JPQLQuery<ProductUsed> query = new JPAQuery(getEntityManager());
        query.from(qProductUsed);
        query.where(qProductUsed.store.id.eq(id));
        if (whereRequest.getQueryObj() != null) {
            JSONObject map = JSON.parseObject(whereRequest.getQueryObj());
            if (map.get(Constants.SEARCH_OPTION_TYPE) != null && !StringUtil.isBlank(map.get(Constants.SEARCH_OPTION_TYPE).toString())) {
                query.where(qProductUsed.type.eq(map.get(Constants.SEARCH_OPTION_TYPE).toString()));
            }
            if (map.get(Constants.SEARCH_OPTION_PRODUCT_NUM) != null && !StringUtil.isBlank(map.get(Constants.SEARCH_OPTION_PRODUCT_NUM).toString())) {
                query.where(qProductUsed.productNum.like("%" + map.get(Constants.SEARCH_OPTION_PRODUCT_NUM).toString() + "%"));
            }
            if (map.get(Constants.SEARCH_OPTION_STORE_NAME) != null && !StringUtil.isBlank(map.get(Constants.SEARCH_OPTION_STORE_NAME).toString())) {
                query.where(qProductUsed.store.name.like("%" + map.get(Constants.SEARCH_OPTION_STORE_NAME).toString() + "%"));
            }
            if (map.get(Constants.SEARCH_OPTION_CODE) != null && !StringUtil.isBlank(map.get(Constants.SEARCH_OPTION_CODE).toString())) {
                query.where(qProductUsed.code.like("%" + map.get(Constants.SEARCH_OPTION_CODE).toString() + "%"));
            }
        } else if (!StringUtil.isBlank(whereRequest.getQueryName())) {
            query.where(qProductUsed.code.like("%" + whereRequest.getQueryName() + "%"));
        }
        pageList.setRecordsTotal(query.fetchCount());
        query.orderBy(qProductUsed.type.asc(),qProductUsed.code.asc());
        if( whereRequest.getCount() > Constants.COUNT_PAGING_MARK){
            query.offset(whereRequest.getOffset()).limit(whereRequest.getCount());
        }
        pageList.setList(query.fetch());
        return pageList;
    }

    @Override
    public List<ProductUsed> listByIDs(List<Long> idList) {
        QProductUsed qProductUsed = QProductUsed.productUsed;
        JPQLQuery<ProductUsed> query = new JPAQuery(getEntityManager());
        query.from(qProductUsed);
        query.where(qProductUsed.id.in(idList));
        return query.fetch();
    }

    @Override
    public ProductUsed getByCode(String code){
        QProductUsed qProductUsed = QProductUsed.productUsed;
        JPQLQuery<ProductUsed> query = new JPAQuery(getEntityManager());
        query.from(qProductUsed);
        query.where(qProductUsed.code.eq(code));
        return query.fetchFirst();
    }

}
