package com.myee.tarot.configuration.service.impl;

import com.myee.tarot.catalog.domain.ProductUsed;
import com.myee.tarot.configuration.dao.ReceiptPrintedDao;
import com.myee.tarot.configuration.domain.ReceiptPrinted;
import com.myee.tarot.configuration.service.ReceiptPrintedService;
import com.myee.tarot.core.service.GenericEntityServiceImpl;
import com.myee.tarot.core.util.PageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Ray.Fu on 2016/12/19.
 */
@Service
public class ReceiptPrintedServiceImpl extends GenericEntityServiceImpl<Long, ReceiptPrinted> implements ReceiptPrintedService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReceiptPrintedServiceImpl.class);

    protected ReceiptPrintedDao receiptPrintedDao;

    @Autowired
    public ReceiptPrintedServiceImpl(ReceiptPrintedDao receiptPrintedDao) {
        super(receiptPrintedDao);
        this.receiptPrintedDao = receiptPrintedDao;
    }

    @Override
    public PageResult<ReceiptPrinted> listByProductUsed(List<ProductUsed> productUsedList) {
        return receiptPrintedDao.listByProductUsed(productUsedList);
    }

    @Override
    public PageResult<ReceiptPrinted> listByMerchantStoreId(Long id) {
        return receiptPrintedDao.listByMerchantStoreId(id);
    }
}
