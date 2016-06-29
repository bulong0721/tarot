package com.myee.tarot.device.dao.impl;

import com.myee.tarot.catalog.domain.Device;
import com.myee.tarot.catalog.domain.QDevice;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.device.dao.DeviceDao;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2016/5/31.
 */
@Repository
public class DeviceDaoImpl extends GenericEntityDaoImpl<Long, Device> implements DeviceDao{
    @Override
    public PageResult<Device> pageList(PageRequest pageRequest) {
        PageResult<Device> pageList = new PageResult<Device>();
        QDevice qDevice = QDevice.device;
        JPQLQuery<Device> query = new JPAQuery(getEntityManager());

        if(StringUtils.isNotBlank(pageRequest.getQueryName())){
            query.where(qDevice.name.like("%" + pageRequest.getQueryName() + "%"));
        }
        pageList.setRecordsTotal(query.from(qDevice).fetchCount());
        pageList.setRecordsFiltered(query.from(qDevice).fetchCount());
        if( pageRequest.getCount() > 0){
            query.offset(pageRequest.getOffset()).limit(pageRequest.getCount());
        }
        pageList.setList(query.fetch());
        return pageList;
    }
}
