package com.myee.tarot.configuration.dao;

import com.myee.tarot.configuration.domain.ReceiptProductUsedXref;
import com.myee.tarot.core.dao.GenericEntityDao;

import java.util.List;

/**
 * Created by Ray.Fu on 2016/12/28.
 */
public interface ReceiptProductUsedXrefDao extends GenericEntityDao<Long, ReceiptProductUsedXref> {

    List<ReceiptProductUsedXref> listByReceiptPrintedId(Long id);

    ReceiptProductUsedXref getByTypeAndProductUsedId(String type, Long id);
}
