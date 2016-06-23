package com.myee.tarot.product.dao.impl;

import com.myee.tarot.catalog.domain.ProductUsed;
import com.myee.tarot.catalog.domain.QProductUsed;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.product.dao.ProductUsedDao;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Visitor;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Nullable;
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
        pageList.setRecordsTotal(query.from(qProductUsed).fetchCount());
        if(StringUtils.isNotBlank(pageRequest.getQueryName())){
            query.where(qProductUsed.code.like("%" + pageRequest.getQueryName() + "%"));
        }
        pageList.setRecordsFiltered(query.fetchCount());
        pageList.setList(query.offset(pageRequest.getStart()).limit(pageRequest.getLength()).fetch());
        return pageList;
    }

    @Override
    public PageResult<ProductUsed> pageListByStore(PageRequest pageRequest, Long id,int ifPaging){
        PageResult<ProductUsed> pageList = new PageResult<ProductUsed>();
        QProductUsed qProductUsed = QProductUsed.productUsed;
        JPQLQuery<ProductUsed> query = new JPAQuery(getEntityManager());
        query.where(qProductUsed.store.id.eq(id));
        pageList.setRecordsTotal(query.from(qProductUsed).fetchCount());
        if(StringUtils.isNotBlank(pageRequest.getQueryName())){
            query.where(qProductUsed.code.like("%" + pageRequest.getQueryName() + "%"));
        }
        pageList.setRecordsFiltered(query.fetchCount());
        query.offset(pageRequest.getStart());
        if(ifPaging == Constants.PAGING){
            query.limit(pageRequest.getLength());
        }
        pageList.setList(query.fetch());
        return pageList;
    }

    @Override
    public List<ProductUsed> listByIDs(List<Long> idList){
        QProductUsed qProductUsed = QProductUsed.productUsed;
        JPQLQuery<ProductUsed> query = new JPAQuery(getEntityManager());
        query.from(qProductUsed);
        query.where(qProductUsed.id.in(idList));
        return query.fetch();
    }

}
