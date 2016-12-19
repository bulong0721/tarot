package com.myee.tarot.resource.service.impl;

import com.myee.tarot.core.service.GenericEntityServiceImpl;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.WhereRequest;
import com.myee.tarot.resource.dao.NotificationDao;
import com.myee.tarot.resource.dao.UpdateConfigDao;
import com.myee.tarot.resource.domain.Notification;
import com.myee.tarot.resource.domain.UpdateConfig;
import com.myee.tarot.resource.service.NotificationService;
import com.myee.tarot.resource.service.UpdateConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;

/**
 * Created by Chay on 2016/12/15.
 */
@Service
public class UpdateConfigServiceImpl extends GenericEntityServiceImpl<Long, UpdateConfig> implements UpdateConfigService {

    protected UpdateConfigDao updateConfigDao;

    @Autowired
    public UpdateConfigServiceImpl(UpdateConfigDao updateConfigDao) {
        super(updateConfigDao);
        this.updateConfigDao = updateConfigDao;
    }

    @Override
    public PageResult<UpdateConfig> page(WhereRequest pageRequest) throws ParseException{
        return updateConfigDao.page(pageRequest);
    }

}
