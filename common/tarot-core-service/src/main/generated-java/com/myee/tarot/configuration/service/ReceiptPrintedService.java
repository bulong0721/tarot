package com.myee.tarot.configuration.service;

import com.myee.tarot.catalog.domain.ProductUsed;
import com.myee.tarot.configuration.domain.ReceiptPrinted;
import com.myee.tarot.core.service.GenericEntityService;
import com.myee.tarot.core.util.PageResult;

import java.util.List;

public interface ReceiptPrintedService extends GenericEntityService<java.lang.Long, ReceiptPrinted> {

    PageResult<ReceiptPrinted> listByProductUsed(List<ProductUsed> productUsedList);

    PageResult<ReceiptPrinted> listByMerchantStoreId(Long id);
}