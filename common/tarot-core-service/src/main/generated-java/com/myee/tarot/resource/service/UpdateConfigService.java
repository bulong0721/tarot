package com.myee.tarot.resource.service;

import com.myee.tarot.core.service.GenericEntityService;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.WhereRequest;
import com.myee.tarot.resource.domain.Notification;
import com.myee.tarot.resource.domain.UpdateConfig;

import java.text.ParseException;

/**
 * Created by Chay on 2016/12/15.
 */
public interface UpdateConfigService extends GenericEntityService<Long, UpdateConfig> {

    PageResult<UpdateConfig> page(WhereRequest pageRequest) throws ParseException;
}
