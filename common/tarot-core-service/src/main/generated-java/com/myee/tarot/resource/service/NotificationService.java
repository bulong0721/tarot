package com.myee.tarot.resource.service;

import com.myee.djinn.dto.NotificationDTO;
import com.myee.tarot.core.service.GenericEntityService;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.WhereRequest;
import com.myee.tarot.resource.domain.Notification;

import java.text.ParseException;

/**
 * Created by Ray.Fu on 2016/8/10.
 */
public interface NotificationService extends GenericEntityService<Long, Notification> {

    PageResult<Notification> pageByStore(Long id, WhereRequest whereRequest) throws ParseException;

}
