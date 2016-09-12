package com.myee.tarot.apiold.service;

import com.myee.tarot.apiold.domain.RollDetail;
import com.myee.tarot.core.service.GenericEntityService;

/**
 * Created by Chay on 2016/8/10.
 */
public interface RollDetailService extends GenericEntityService<Long, RollDetail> {
    void deleteByRollMain(Long rollMainId);
}
