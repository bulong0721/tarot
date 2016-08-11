package com.myee.tarot.apiold.service;

import com.myee.tarot.apiold.domain.TablePhone;
import com.myee.tarot.core.service.GenericEntityService;

/**
 * Created by xiaoni on 2016/8/10.
 */
public interface TablePhoneService extends GenericEntityService<Long, TablePhone> {
    TablePhone findByTableId(Long tableId);
}
