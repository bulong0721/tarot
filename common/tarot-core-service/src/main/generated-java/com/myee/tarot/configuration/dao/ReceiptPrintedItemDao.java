package com.myee.tarot.configuration.dao;

import com.myee.tarot.catalog.domain.ProductUsed;
import com.myee.tarot.configuration.domain.ReceiptPrinted;
import com.myee.tarot.configuration.domain.ReceiptPrintedItem;
import com.myee.tarot.core.dao.GenericEntityDao;
import com.myee.tarot.core.util.PageResult;

import java.sql.SQLException;
import java.util.List;

public interface ReceiptPrintedItemDao extends GenericEntityDao<Long, ReceiptPrintedItem> {

    public List<ReceiptPrintedItem> listAllByReceiptPrintedId(Long id);

    void deleteByIds(List<Long> ids);
}