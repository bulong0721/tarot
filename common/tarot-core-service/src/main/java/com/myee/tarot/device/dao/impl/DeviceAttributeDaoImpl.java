package com.myee.tarot.device.dao.impl;

import com.myee.tarot.catalog.domain.*;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.device.dao.DeviceAttributeDao;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.JPQLQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Chay on 2016/6/19.
 */
@Repository
public class DeviceAttributeDaoImpl extends GenericEntityDaoImpl<Long, DeviceAttribute> implements DeviceAttributeDao {
    public static Log log = LogFactory.getLog(DeviceAttributeDaoImpl.class);

    @Override
    public List<DeviceAttribute> listByDeviceId(Long id){
        QDeviceAttribute qDeviceAttribute = QDeviceAttribute.deviceAttribute;
        JPQLQuery<DeviceAttribute> query = new JPAQuery(getEntityManager());
        query.from(qDeviceAttribute)
                .where(qDeviceAttribute.device.id.eq( id  ));
        log.info(query.fetchCount());

        return query.fetch();
    }

    @Override
    public void deleteByDeviceId(Long id){
        QDeviceAttribute qDeviceAttribute = QDeviceAttribute.deviceAttribute;
        JPQLQueryFactory queryFactory = new JPAQueryFactory(getEntityManager());

        queryFactory.delete(qDeviceAttribute)
                .where(qDeviceAttribute.device.id.eq(id))
                .execute();
    }
}
