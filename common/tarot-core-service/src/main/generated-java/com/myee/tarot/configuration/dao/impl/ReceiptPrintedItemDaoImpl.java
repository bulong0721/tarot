package com.myee.tarot.configuration.dao.impl;

import com.myee.tarot.configuration.dao.ReceiptPrintedItemDao;
import com.myee.tarot.configuration.domain.QReceiptPrintedItem;
import com.myee.tarot.configuration.domain.ReceiptPrinted;
import com.myee.tarot.configuration.domain.ReceiptPrintedItem;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPADeleteClause;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Ray.Fu on 2016/12/19.
 */
@Repository
public class ReceiptPrintedItemDaoImpl extends GenericEntityDaoImpl<Long, ReceiptPrintedItem> implements ReceiptPrintedItemDao {

    private final static Logger LOGGER = LoggerFactory.getLogger(ReceiptPrintedItemDaoImpl.class);


    @Override
    public List<ReceiptPrintedItem> listAllByReceiptPrintedId(Long id) {
        QReceiptPrintedItem qReceiptPrintedItem = QReceiptPrintedItem.receiptPrintedItem;
        JPQLQuery<ReceiptPrintedItem> query = new JPAQuery(getEntityManager());
        query.from(qReceiptPrintedItem);
        if (id != null) {
            query.where(qReceiptPrintedItem.receiptPrinted.id.eq(id));
        }
        return query.fetch();
    }

    @Override
    public void deleteByIds(List<Long> ids) {
        QReceiptPrintedItem qReceiptPrintedItem = QReceiptPrintedItem.receiptPrintedItem;
        JPADeleteClause query = new JPADeleteClause(getEntityManager(), qReceiptPrintedItem);
        if (ids != null && ids.size() > 0) {
            query.where(qReceiptPrintedItem.id.in(ids));
        }
        query.execute();
    }
}
