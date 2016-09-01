package com.myee.tarot.apiold.dao.impl;

import com.myee.tarot.apiold.dao.RollMainPublishDao;
import com.myee.tarot.apiold.domain.QRollMainPublish;
import com.myee.tarot.apiold.domain.RollMainPublish;
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
public class RollMainPublishDaoImpl extends GenericEntityDaoImpl<Long, RollMainPublish> implements RollMainPublishDao {
    public List<RollMainPublish> listByStoreTime(Long storeId, Date now){
        QRollMainPublish qRollMainPublish = QRollMainPublish.rollMainPublish;

        JPQLQuery<RollMainPublish> query = new JPAQuery(getEntityManager());

        query.from(qRollMainPublish);

        if(storeId != null){
            query.where(qRollMainPublish.store.id.eq(storeId));
        }
        if(now != null){
            query.where((qRollMainPublish.timeStart.before(now))
                    .and(qRollMainPublish.timeEnd.after(now)));
        }
        query.where((qRollMainPublish.rollMain.type.eq(Constants.API_OLD_TYPE_MUYE)).and(qRollMainPublish.active.eq(true)))
                .orderBy(qRollMainPublish.id.desc())
                .offset(0).limit(Constants.ROLL_MAIN_PUBLISH_MAX);//一个店铺下的木爷活动不能超过5个

        return query.fetch();
    }
}
