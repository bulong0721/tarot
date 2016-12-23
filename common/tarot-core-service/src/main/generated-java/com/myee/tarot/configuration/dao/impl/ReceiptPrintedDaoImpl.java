package com.myee.tarot.configuration.dao.impl;

import com.myee.tarot.catalog.domain.ProductUsed;
import com.myee.tarot.configuration.dao.ReceiptPrintedDao;
import com.myee.tarot.configuration.domain.QReceiptPrinted;
import com.myee.tarot.configuration.domain.ReceiptPrinted;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.core.util.PageResult;
import com.querydsl.core.types.Expression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Ray.Fu on 2016/12/19.
 */
@Repository
public class ReceiptPrintedDaoImpl extends GenericEntityDaoImpl<Long, ReceiptPrinted> implements ReceiptPrintedDao {

    private final static Logger LOGGER = LoggerFactory.getLogger(ReceiptPrintedDaoImpl.class);


    @Override
    public PageResult<ReceiptPrinted> listByProductUsed(List<ProductUsed> productUsedList) {
        PageResult<ReceiptPrinted> pageList = new PageResult<ReceiptPrinted>();
        QReceiptPrinted qReceiptPrinted = QReceiptPrinted.receiptPrinted;
        JPQLQuery<ReceiptPrinted> query = new JPAQuery(getEntityManager());
        query.from(qReceiptPrinted);
        if (productUsedList != null && productUsedList.size() > 0) {
            query.where(qReceiptPrinted.productUsed.contains((Expression<ProductUsed>) productUsedList));
        }
        pageList.setRecordsTotal(query.fetchCount());
        pageList.setList(query.fetch());
        return pageList;
    }

    @Override
    public PageResult<ReceiptPrinted> listByMerchantStoreId(Long id) {
        PageResult<ReceiptPrinted> pageList = new PageResult<ReceiptPrinted>();
        QReceiptPrinted qReceiptPrinted = QReceiptPrinted.receiptPrinted;
        JPQLQuery<ReceiptPrinted> query = new JPAQuery(getEntityManager());
        query.from(qReceiptPrinted);
        if (id != null) {
            query.where(qReceiptPrinted.store.id.eq(id));
        }
        pageList.setRecordsTotal(query.fetchCount());
        pageList.setList(query.fetch());
        return pageList;
    }
}
