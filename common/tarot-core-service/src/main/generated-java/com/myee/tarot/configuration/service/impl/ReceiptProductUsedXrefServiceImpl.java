package com.myee.tarot.configuration.service.impl;

import com.myee.tarot.configuration.dao.ReceiptProductUsedXrefDao;
import com.myee.tarot.configuration.domain.ReceiptProductUsedXref;
import com.myee.tarot.configuration.service.ReceiptProductUsedXrefService;
import com.myee.tarot.core.service.GenericEntityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Ray.Fu on 2016/12/28.
 */
@Service
public class ReceiptProductUsedXrefServiceImpl extends GenericEntityServiceImpl<Long, ReceiptProductUsedXref> implements ReceiptProductUsedXrefService {

    protected ReceiptProductUsedXrefDao receiptProductUsedXrefDao;

    @Autowired
    public ReceiptProductUsedXrefServiceImpl(ReceiptProductUsedXrefDao receiptProductUsedXrefDao) {
        super(receiptProductUsedXrefDao);
        this.receiptProductUsedXrefDao = receiptProductUsedXrefDao;
    }

    @Override
    public List<ReceiptProductUsedXref> listByReceiptPrintedId(Long id) {
        return receiptProductUsedXrefDao.listByReceiptPrintedId(id);
    }

    @Override
    public ReceiptProductUsedXref getByTypeAndProductUsedId(String type, Long id) {
        return receiptProductUsedXrefDao.getByTypeAndProductUsedId(type, id);
    }
}
