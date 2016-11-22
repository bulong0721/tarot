package com.myee.tarot.resource.dao.impl;

import com.myee.tarot.catalog.domain.DeviceUsed;
import com.myee.tarot.catalog.domain.QDeviceUsed;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.StringUtil;
import com.myee.tarot.resource.dao.NotificationDao;
import com.myee.tarot.resource.domain.Notification;
import com.myee.tarot.resource.domain.QNotification;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;

/**
 * Created by Ray.Fu on 2016/8/10.
 */
@Repository
public class NotificationDaoImpl extends GenericEntityDaoImpl<Long, Notification> implements NotificationDao {

    @Override
    public PageResult<Notification> pageByStore(Long id, PageRequest pageRequest){
        PageResult<Notification> pageList = new PageResult<Notification>();
        QNotification qNotification = QNotification.notification;
        JPQLQuery<Notification> query = new JPAQuery(getEntityManager());
        query.from(qNotification);
        if(!StringUtil.isBlank(pageRequest.getQueryName())){
            query.where(qNotification.content.like("%" + pageRequest.getQueryName() + "%"));
        }
        query.where(qNotification.store.id.eq(id));
        pageList.setRecordsTotal(query.fetchCount());

        query.orderBy(qNotification.createTime.desc());
        if( pageRequest.getCount() > Constants.COUNT_PAGING_MARK){
            query.offset(pageRequest.getOffset()).limit(pageRequest.getCount());
        }
        pageList.setList(query.fetch());
        return pageList;
    }
}
