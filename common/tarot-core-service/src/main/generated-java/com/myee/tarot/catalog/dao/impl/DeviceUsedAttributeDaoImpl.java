package com.myee.tarot.catalog.dao.impl;

import com.myee.tarot.catalog.domain.DeviceAttribute;
import com.myee.tarot.catalog.domain.DeviceUsedAttribute;
import com.myee.tarot.catalog.domain.QDeviceAttribute;
import com.myee.tarot.catalog.domain.QDeviceUsedAttribute;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.catalog.dao.DeviceAttributeDao;
import com.myee.tarot.catalog.dao.DeviceUsedAttributeDao;
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
public class DeviceUsedAttributeDaoImpl extends GenericEntityDaoImpl<Long, DeviceUsedAttribute> implements DeviceUsedAttributeDao {
    public static Log log = LogFactory.getLog(DeviceUsedAttributeDaoImpl.class);

    @Override
    public List<DeviceUsedAttribute> listByDeviceUsedId(Long id){
        QDeviceUsedAttribute qDeviceUsedAttribute = QDeviceUsedAttribute.deviceUsedAttribute;
        JPQLQuery<DeviceUsedAttribute> query = new JPAQuery(getEntityManager());
        query.from(qDeviceUsedAttribute)
                .where(qDeviceUsedAttribute.deviceUsed.id.eq( id  ));
        log.info(query.fetchCount());

        return query.fetch();
    }

    @Override
    public void deleteByDeviceUsedId(Long id){
        QDeviceUsedAttribute qDeviceUsedAttribute = QDeviceUsedAttribute.deviceUsedAttribute;
        JPQLQueryFactory queryFactory = new JPAQueryFactory(getEntityManager());

        queryFactory.delete(qDeviceUsedAttribute)
                .where(qDeviceUsedAttribute.deviceUsed.id.eq(id))
                .execute();

    }
}
