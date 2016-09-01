package com.myee.tarot.apiold.dao.impl;

import com.myee.tarot.apiold.dao.VideoPublishDao;
import com.myee.tarot.apiold.domain.QVideoPublish;
import com.myee.tarot.apiold.domain.VideoPublish;
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
public class VideoPublishDaoImpl extends GenericEntityDaoImpl<Long, VideoPublish> implements VideoPublishDao {

    public List<VideoPublish> listByStore(Long storeId,Date now){
        QVideoPublish qVideoPublish = QVideoPublish.videoPublish;

        JPQLQuery<VideoPublish> query = new JPAQuery(getEntityManager());

        query.from(qVideoPublish);

        if(storeId != null){
            query.where(qVideoPublish.store.id.eq(storeId));
        }
        if(now != null){
            query.where((qVideoPublish.timeStart.before(now))
                    .and(qVideoPublish.timeEnd.after(now)));
        }
        query.where((qVideoPublish.videoBusiness.type.eq(1)).and(qVideoPublish.active.eq(true)))
                .orderBy(qVideoPublish.id.desc())
                .offset(0).limit(30);

        return query.fetch();
    }
}
