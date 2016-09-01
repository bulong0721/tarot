package com.myee.tarot.apiold.service;

import com.myee.tarot.apiold.domain.AkSk;
import com.myee.tarot.core.service.GenericEntityService;

/**
 * Created by Chay on 2016/8/10.
 */
public interface AkSkService extends GenericEntityService<Long, AkSk> {

    AkSk getByToken(String token);
}
