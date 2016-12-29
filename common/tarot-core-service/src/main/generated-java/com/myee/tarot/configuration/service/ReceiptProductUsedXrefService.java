package com.myee.tarot.configuration.service;

import com.myee.tarot.configuration.domain.ReceiptProductUsedXref;
import com.myee.tarot.core.service.GenericEntityService;

import java.util.List;

/**
 * Created by Ray.Fu on 2016/12/28.
 */
public interface ReceiptProductUsedXrefService extends GenericEntityService<Long, ReceiptProductUsedXref> {

    List<ReceiptProductUsedXref> listByReceiptPrintedId(Long id);

    ReceiptProductUsedXref getByTypeAndProductUsedId(String type, Long id);
}
