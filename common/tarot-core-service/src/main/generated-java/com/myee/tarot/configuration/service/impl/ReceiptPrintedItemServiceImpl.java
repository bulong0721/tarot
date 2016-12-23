package com.myee.tarot.configuration.service.impl;

import com.myee.tarot.configuration.ReceiptPrintedItemService;
import com.myee.tarot.configuration.dao.ReceiptPrintedItemDao;
import com.myee.tarot.configuration.domain.ReceiptPrintedItem;
import com.myee.tarot.core.service.GenericEntityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Ray.Fu on 2016/12/20.
 */
@Service
public class ReceiptPrintedItemServiceImpl extends GenericEntityServiceImpl<Long, ReceiptPrintedItem> implements ReceiptPrintedItemService {

    protected ReceiptPrintedItemDao receiptPrintedItemDao;

    @Autowired
    public ReceiptPrintedItemServiceImpl(ReceiptPrintedItemDao receiptPrintedItemDao) {
        super(receiptPrintedItemDao);
        this.receiptPrintedItemDao = receiptPrintedItemDao;
    }

    @Override
    public List<ReceiptPrintedItem> listAllByReceiptPrintedId(Long id) {
        return receiptPrintedItemDao.listAllByReceiptPrintedId(id);
    }

    @Override
    public void deleteByIds(List<Long> ids) {
        receiptPrintedItemDao.deleteByIds(ids);
    }
}


