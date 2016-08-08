package com.myee.tarot.campaign.dao.impl;

import com.myee.tarot.campaign.dao.ModeSwitchDao;
import com.myee.tarot.campaign.domain.ModeSwitch;
import com.myee.tarot.campaign.domain.QModeSwitch;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2016/8/2.
 */
@Repository
public class ModeSwitchDaoImpl extends GenericEntityDaoImpl<Long, ModeSwitch> implements ModeSwitchDao{

    @Override
    public ModeSwitch findByStoreId(Long storeId) {
        QModeSwitch qModeSwitch = QModeSwitch.modeSwitch;
        JPQLQuery<ModeSwitch> query = new JPAQuery(getEntityManager());
        List<ModeSwitch> result =  query.from(qModeSwitch).where(qModeSwitch.storeId.eq(storeId)).fetch();
        return result!=null&&result.size()>0 ? result.get(0):null;
    }
}
