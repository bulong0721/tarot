package com.myee.tarot.device.dao.impl;

import com.myee.tarot.catalog.domain.Device;
import com.myee.tarot.catalog.domain.QDevice;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.device.dao.DeviceDao;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.jpa.criteria.CriteriaQueryImpl;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

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
        pageList.setRecordsTotal(query.from(qDevice).fetchCount());
        if(StringUtils.isNotBlank(pageRequest.getQueryName())){
            query.where(qDevice.name.like("%" + pageRequest.getQueryName() + "%"));
        }
        pageList.setRecordsFiltered(query.fetchCount());
        pageList.setList(query.offset(pageRequest.getStart()).limit(pageRequest.getLength()).fetch());
        return pageList;
    }
}
