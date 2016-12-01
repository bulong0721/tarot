package com.myee.tarot.catalog.dao.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.myee.tarot.catalog.domain.Device;
import com.myee.tarot.catalog.domain.QDevice;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.StringUtil;
import com.myee.tarot.catalog.dao.DeviceDao;
import com.myee.tarot.core.util.WhereRequest;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2016/5/31.
 */
@Repository
public class DeviceDaoImpl extends GenericEntityDaoImpl<Long, Device> implements DeviceDao{
    @Override
    public PageResult<Device> pageList(WhereRequest whereRequest) {
        PageResult<Device> pageList = new PageResult<Device>();
        QDevice qDevice = QDevice.device;
        JPQLQuery<Device> query = new JPAQuery(getEntityManager());
        query.from(qDevice);
        if (whereRequest.getQueryObj() != null) {
            JSONObject map = JSON.parseObject(whereRequest.getQueryObj());
            if (map.get(Constants.SEARCH_OPTION_NAME) != null && !StringUtil.isBlank(map.get(Constants.SEARCH_OPTION_NAME).toString())) {
                query.where(qDevice.name.like("%" + map.get(Constants.SEARCH_OPTION_NAME).toString() + "%"));
            }
            if (map.get(Constants.SEARCH_OPTION_VERSION_NUM) != null && !StringUtil.isBlank(map.get(Constants.SEARCH_OPTION_VERSION_NUM).toString())) {
                query.where(qDevice.versionNum.like("%" + map.get(Constants.SEARCH_OPTION_VERSION_NUM).toString() + "%"));
            }
        } else if(!StringUtil.isBlank(whereRequest.getQueryName())){
            query.where(qDevice.name.like("%" + whereRequest.getQueryName() + "%"));
        }
        pageList.setRecordsTotal(query.fetchCount());
        query.orderBy(qDevice.name.asc());
        if( whereRequest.getCount() > Constants.COUNT_PAGING_MARK){
            query.offset(whereRequest.getOffset()).limit(whereRequest.getCount());
        }
        pageList.setList(query.fetch());
        return pageList;
    }
}
