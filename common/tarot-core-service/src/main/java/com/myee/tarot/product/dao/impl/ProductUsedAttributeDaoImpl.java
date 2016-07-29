package com.myee.tarot.product.dao.impl;

import com.myee.tarot.catalog.domain.ProductUsed;
import com.myee.tarot.catalog.domain.ProductUsedAttribute;
import com.myee.tarot.catalog.domain.QProductUsedAttribute;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.product.dao.ProductUsedAttributeDao;
import com.myee.tarot.product.dao.ProductUsedDao;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.JPQLQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Enva on 2016/6/1.
 */
@Repository
public class ProductUsedAttributeDaoImpl extends GenericEntityDaoImpl<Long, ProductUsedAttribute> implements ProductUsedAttributeDao {
    public static Log log = LogFactory.getLog(ProductUsedAttributeDaoImpl.class);

    @Override
    public List<ProductUsedAttribute> listByProductId(Long id){
        QProductUsedAttribute qProductUsedAttribute = QProductUsedAttribute.productUsedAttribute;
        JPQLQuery<ProductUsedAttribute> query = new JPAQuery(getEntityManager());
        query.from(qProductUsedAttribute)
                .where(qProductUsedAttribute.productUsed.id.eq( id  ));
        log.info(query.fetchCount());

        return query.fetch();

    }

    @Override
    public void deleteByProductUsedId(Long id){
        QProductUsedAttribute qProductUsedAttribute = QProductUsedAttribute.productUsedAttribute;

        JPQLQueryFactory queryFactory = new JPAQueryFactory(getEntityManager());

        queryFactory.delete(qProductUsedAttribute)
                .where(qProductUsedAttribute.productUsed.id.eq(id))
                .execute();
    }
}
