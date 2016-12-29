package com.myee.tarot.configuration.dao.impl;

import com.myee.tarot.configuration.dao.ReceiptProductUsedXrefDao;
import com.myee.tarot.configuration.domain.QReceiptProductUsedXref;
import com.myee.tarot.configuration.domain.ReceiptProductUsedXref;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.core.util.StringUtil;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Ray.Fu on 2016/12/28.
 */
@Repository
public class ReceiptProductUsedXrefDaoImpl extends GenericEntityDaoImpl<Long, ReceiptProductUsedXref> implements ReceiptProductUsedXrefDao {

    private final static Logger LOGGER = LoggerFactory.getLogger(ReceiptProductUsedXrefDaoImpl.class);

    @Override
    public List<ReceiptProductUsedXref> listByReceiptPrintedId(Long id) {
        QReceiptProductUsedXref qReceiptProductUsedXref = QReceiptProductUsedXref.receiptProductUsedXref;
        JPQLQuery<ReceiptProductUsedXref> query = new JPAQuery(getEntityManager());
        query.from(qReceiptProductUsedXref);
        if (id != null) {
            query.where(qReceiptProductUsedXref.receiptId.eq(id));
        }
        return query.fetch();
    }

    @Override
    public ReceiptProductUsedXref getByTypeAndProductUsedId(String type, Long productUsedId) {
        QReceiptProductUsedXref qReceiptProductUsedXref = QReceiptProductUsedXref.receiptProductUsedXref;
        JPQLQuery<ReceiptProductUsedXref> query = new JPAQuery(getEntityManager());
        query.from(qReceiptProductUsedXref);
        if (productUsedId != null) {
            query.where(qReceiptProductUsedXref.productUsedId.eq(productUsedId));
        }
        if (!StringUtil.isNullOrEmpty(type)) {
            query.where(qReceiptProductUsedXref.type.eq(type));
        }
        return query.fetchOne();
    }
}
