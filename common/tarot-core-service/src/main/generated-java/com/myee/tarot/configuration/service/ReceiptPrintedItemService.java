package com.myee.tarot.configuration.service;

import com.myee.tarot.configuration.domain.ReceiptPrintedItem;
import com.myee.tarot.core.service.GenericEntityService;

import java.util.List;

/**
 * Created by Ray.Fu on 2016/12/20.
 */
public interface ReceiptPrintedItemService extends GenericEntityService<Long, ReceiptPrintedItem> {

    List<ReceiptPrintedItem> listAllByReceiptPrintedId (Long id);

    void deleteByReceiptPrintedId(Long id);
}
