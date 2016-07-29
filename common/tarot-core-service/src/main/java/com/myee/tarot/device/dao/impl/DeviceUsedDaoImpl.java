package com.myee.tarot.device.dao.impl;

import com.myee.tarot.catalog.domain.DeviceUsed;
import com.myee.tarot.catalog.domain.QDevice;
import com.myee.tarot.catalog.domain.QDeviceUsed;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.StringUtil;
import com.myee.tarot.device.dao.DeviceUsedDao;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2016/5/31.
 */
@Repository
public class DeviceUsedDaoImpl extends GenericEntityDaoImpl<Long, DeviceUsed> implements DeviceUsedDao {

    @Override
    public PageResult<DeviceUsed> pageByStore(Long id,PageRequest pageRequest){
        PageResult<DeviceUsed> pageList = new PageResult<DeviceUsed>();
        QDeviceUsed qDeviceUsed = QDeviceUsed.deviceUsed;
        JPQLQuery<DeviceUsed> query = new JPAQuery(getEntityManager());
        if(id != null) {
            query.where(qDeviceUsed.store.id.eq(id));
        }
        if(!StringUtil.isBlank(pageRequest.getQueryName())){
            query.where(qDeviceUsed.name.like("%" + pageRequest.getQueryName() + "%"));
        }
        pageList.setRecordsTotal(query.from(qDeviceUsed).fetchCount());
        if( pageRequest.getCount() > 0){
            query.offset(pageRequest.getOffset()).limit(pageRequest.getCount());
        }
        pageList.setList(query.fetch());
        return pageList;
    }

    @Override
    public List<DeviceUsed> listByIDs(List<Long> bindList){
        QDeviceUsed qDeviceUsed = QDeviceUsed.deviceUsed;
        JPQLQuery<DeviceUsed> query = new JPAQuery(getEntityManager());
        query.from(qDeviceUsed);
        query.where(qDeviceUsed.id.in(bindList));
        return query.fetch();
    }

    /**
     * 根据设备的主板编号查询对应的店铺信息
     * @param mainBoardCode
     * @return
     */
    @Override
    public DeviceUsed getStoreInfoByMbCode(String mainBoardCode) {
        QDeviceUsed qDeviceUsed = QDeviceUsed.deviceUsed;
        JPQLQuery<DeviceUsed> query = new JPAQuery(getEntityManager());
        query.from(qDeviceUsed);
        query.where(qDeviceUsed.boardNo.eq(mainBoardCode));
        DeviceUsed deviceUsed = query.fetchFirst();
        if(deviceUsed != null){
            Hibernate.initialize(deviceUsed.getStore());
            Hibernate.initialize(deviceUsed.getProductUsed());
            Hibernate.initialize(deviceUsed.getAttributes());
            Hibernate.initialize(deviceUsed.getStore().getMerchant());
        }
        return deviceUsed;
    }
}
