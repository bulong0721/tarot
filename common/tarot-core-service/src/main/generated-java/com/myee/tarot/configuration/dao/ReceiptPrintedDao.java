package com.myee.tarot.configuration.dao;

import com.myee.tarot.catalog.domain.ProductUsed;
import com.myee.tarot.configuration.domain.ReceiptPrinted;
import com.myee.tarot.core.dao.GenericEntityDao;
import com.myee.tarot.core.util.PageResult;

import java.util.List;

public interface ReceiptPrintedDao extends GenericEntityDao<Long, ReceiptPrinted> {

    PageResult<ReceiptPrinted> listByProductUsed(List<ProductUsed> productUsedList);

    PageResult<ReceiptPrinted> listByMerchantStoreId(Long id);
}