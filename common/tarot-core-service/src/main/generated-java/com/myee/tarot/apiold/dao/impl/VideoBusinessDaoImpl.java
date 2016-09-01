package com.myee.tarot.apiold.dao.impl;

import com.myee.tarot.apiold.dao.VideoBusinessDao;
import com.myee.tarot.apiold.domain.QVideoBusiness;
import com.myee.tarot.apiold.domain.VideoBusiness;
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
public class VideoBusinessDaoImpl extends GenericEntityDaoImpl<Long, VideoBusiness> implements VideoBusinessDao {
    public List<VideoBusiness> listByTypeStoreTime(Long storeId,int type, Date now){
        QVideoBusiness qVideoBusiness = QVideoBusiness.videoBusiness;

        JPQLQuery<VideoBusiness> query = new JPAQuery(getEntityManager());

        query.from(qVideoBusiness);
        if(storeId != null){
            query.where(qVideoBusiness.store.id.eq(storeId));
        }
        if(now != null){
            query.where((qVideoBusiness.timeStart.before(now))
                    .and(qVideoBusiness.timeEnd.after(now)));
        }
        query.where((qVideoBusiness.type.eq(type)).and(qVideoBusiness.active.eq(true)))
                .orderBy(qVideoBusiness.id.desc());

        //本店取两个视频，一个待机视频，一个点播视频
        if(type == Constants.API_OLD_TYPE_SHOP) {
            query.offset(0).limit(2);
        }
        return query.fetch();
    }

    public VideoBusiness findByIdType(Long vBId, Integer type){
        QVideoBusiness qVideoBusiness = QVideoBusiness.videoBusiness;

        JPQLQuery<VideoBusiness> query = new JPAQuery(getEntityManager());

        query.from(qVideoBusiness);
        if(null == type){
            type = 0;
        }
        query.where((qVideoBusiness.type.eq(type)).and(qVideoBusiness.id.eq(vBId)));

        return query.fetchOne();
    }

}
