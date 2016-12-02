package com.myee.tarot.catalog.dao.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.myee.tarot.catalog.domain.DeviceUsed;
import com.myee.tarot.catalog.domain.QDevice;
import com.myee.tarot.catalog.domain.QDeviceUsed;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.StringUtil;
import com.myee.tarot.catalog.dao.DeviceUsedDao;
import com.myee.tarot.core.util.WhereRequest;
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
    public PageResult<DeviceUsed> pageByStore(Long id,WhereRequest whereRequest){
        PageResult<DeviceUsed> pageList = new PageResult<DeviceUsed>();
        QDeviceUsed qDeviceUsed = QDeviceUsed.deviceUsed;
        JPQLQuery<DeviceUsed> query = new JPAQuery(getEntityManager());
        query.from(qDeviceUsed)
                .leftJoin(qDeviceUsed.device)
                .fetchJoin();
        if(id != null) {
            query.where(qDeviceUsed.store.id.eq(id));
        }
        if (whereRequest.getQueryObj() != null) {
            JSONObject map = JSON.parseObject(whereRequest.getQueryObj());
            if (map.get(Constants.SEARCH_OPTION_NAME) != null && !StringUtil.isBlank(map.get(Constants.SEARCH_OPTION_NAME).toString())) {
                query.where(qDeviceUsed.name.like("%" + map.get(Constants.SEARCH_OPTION_NAME).toString() + "%"));
            }
            if (map.get(Constants.SEARCH_OPTION_STORE_NAME) != null && !StringUtil.isBlank(map.get(Constants.SEARCH_OPTION_STORE_NAME).toString())) {
                query.where(qDeviceUsed.store.name.like("%" + map.get(Constants.SEARCH_OPTION_STORE_NAME).toString() + "%"));
            }
            if (map.get(Constants.SEARCH_OPTION_BOARD_NO) != null && !StringUtil.isBlank(map.get(Constants.SEARCH_OPTION_BOARD_NO).toString())) {
                query.where(qDeviceUsed.boardNo.like("%" + map.get(Constants.SEARCH_OPTION_BOARD_NO).toString() + "%"));
            }
            if (map.get(Constants.SEARCH_OPTION_DEVICE_NUM) != null && !StringUtil.isBlank(map.get(Constants.SEARCH_OPTION_DEVICE_NUM).toString())) {
                query.where(qDeviceUsed.deviceNum.like("%" + map.get(Constants.SEARCH_OPTION_DEVICE_NUM).toString() + "%"));
            }
        } else if(!StringUtil.isBlank(whereRequest.getQueryName())){
            query.where(qDeviceUsed.name.like("%" + whereRequest.getQueryName() + "%"));
        }
        pageList.setRecordsTotal(query.fetchCount());
        query.orderBy(qDeviceUsed.name.asc());
        if( whereRequest.getCount() > Constants.COUNT_PAGING_MARK){
            query.offset(whereRequest.getOffset()).limit(whereRequest.getCount());
        }
        pageList.setList(query.fetch());
        return pageList;
    }

    @Override
    public List<DeviceUsed> listByIDs(List<Long> bindList){
        QDeviceUsed qDeviceUsed = QDeviceUsed.deviceUsed;
        JPQLQuery<DeviceUsed> query = new JPAQuery(getEntityManager());
        query.from(qDeviceUsed)
                .leftJoin(qDeviceUsed.device)
                .fetchJoin();
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
        query.from(qDeviceUsed)
                .leftJoin(qDeviceUsed.device)
                .fetchJoin();
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
