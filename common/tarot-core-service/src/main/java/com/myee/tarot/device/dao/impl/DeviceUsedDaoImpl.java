package com.myee.tarot.device.dao.impl;

import com.myee.tarot.catalog.domain.DeviceUsed;
import com.myee.tarot.catalog.domain.QDeviceUsed;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.device.dao.DeviceUsedDao;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2016/5/31.
 */
@Repository
public class DeviceUsedDaoImpl extends GenericEntityDaoImpl<Long, DeviceUsed> implements DeviceUsedDao {
    @Override
    public PageResult<DeviceUsed> pageList(PageRequest pageRequest) {
        PageResult<DeviceUsed> pageList = new PageResult<DeviceUsed>();
        QDeviceUsed qDeviceUsed = QDeviceUsed.deviceUsed;
        JPQLQuery<DeviceUsed> query = new JPAQuery(getEntityManager());
        pageList.setRecordsTotal(query.from(qDeviceUsed).fetchCount());
        if(StringUtils.isNotBlank(pageRequest.getQueryName())){
            query.where(qDeviceUsed.name.like("%" + pageRequest.getQueryName() + "%"));
        }
        pageList.setRecordsFiltered(query.fetchCount());
        pageList.setList(query.offset(pageRequest.getStart()).limit(pageRequest.getLength()).fetch());
        return pageList;
    }

    @Override
    public PageResult<DeviceUsed> pageListByStore(PageRequest pageRequest, Long id){
        PageResult<DeviceUsed> pageList = new PageResult<DeviceUsed>();
        QDeviceUsed qDeviceUsed = QDeviceUsed.deviceUsed;
        JPQLQuery<DeviceUsed> query = new JPAQuery(getEntityManager());
        pageList.setRecordsTotal(query.from(qDeviceUsed).fetchCount());
        if(StringUtils.isNotBlank(pageRequest.getQueryName())){
            query.where(qDeviceUsed.name.like("%" + pageRequest.getQueryName() + "%"));
        }
        query.where(qDeviceUsed.store.id.eq(id));
        pageList.setRecordsFiltered(query.fetchCount());
        pageList.setList(query.offset(pageRequest.getStart()).limit(pageRequest.getLength()).fetch());
        return pageList;
    }
}
