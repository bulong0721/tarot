package com.myee.tarot.apiold.dao.impl;

import com.myee.tarot.apiold.dao.RollMainDao;
import com.myee.tarot.apiold.domain.QRollMain;
import com.myee.tarot.apiold.domain.RollMain;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by Chay on 2016/8/10.
 */
@Repository
public class RollMainDaoImpl extends GenericEntityDaoImpl<Long, RollMain> implements RollMainDao {

    public List<RollMain> listByTypeStoreTime(Long storeId, int type,Date now){
        QRollMain qRollMain = QRollMain.rollMain;

        JPQLQuery<RollMain> query = new JPAQuery(getEntityManager());

        query.from(qRollMain);

        if(storeId != null){
            query.where(qRollMain.store.id.eq(storeId));
        }
        if(now != null){
            query.where((qRollMain.timeStart.before(now))
                    .and(qRollMain.timeEnd.after(now)));
        }
        query.where(qRollMain.active.eq(true))
            .where(qRollMain.type.eq(type))
            .orderBy(qRollMain.id.desc());

        //本店活动限制10个
        if(type == Constants.API_OLD_TYPE_SHOP){
            query.offset(0).limit(Constants.ROLL_MAIN_SHOP_MAX);
        }

        return query.fetch();
    }
}
