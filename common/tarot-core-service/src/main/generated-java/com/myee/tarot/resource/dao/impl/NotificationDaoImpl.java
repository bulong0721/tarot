package com.myee.tarot.resource.dao.impl;

import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.resource.dao.NotificationDao;
import com.myee.tarot.resource.domain.Notification;
import org.springframework.stereotype.Repository;

/**
 * Created by Ray.Fu on 2016/8/10.
 */
@Repository
public class NotificationDaoImpl extends GenericEntityDaoImpl<Long, Notification> implements NotificationDao {
}
