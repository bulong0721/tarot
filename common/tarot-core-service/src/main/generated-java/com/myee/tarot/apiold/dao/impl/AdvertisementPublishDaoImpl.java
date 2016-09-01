package com.myee.tarot.apiold.dao.impl;

import com.myee.tarot.apiold.dao.AdvertisementPublishDao;
import com.myee.tarot.apiold.domain.AdvertisementPublish;
import com.myee.tarot.apiold.domain.QAdvertisementPublish;
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
public class AdvertisementPublishDaoImpl extends GenericEntityDaoImpl<Long, AdvertisementPublish> implements AdvertisementPublishDao {

    public List<AdvertisementPublish> listByStoreTime(Long storeId, Date now){
        QAdvertisementPublish qAdvertisementPublish = QAdvertisementPublish.advertisementPublish;

        JPQLQuery<AdvertisementPublish> query = new JPAQuery(getEntityManager());

        query.from(qAdvertisementPublish);

        if(storeId != null){
            query.where(qAdvertisementPublish.store.id.eq(storeId));
        }
        if(now != null){
            query.where((qAdvertisementPublish.timeStart.before(now))
                    .and(qAdvertisementPublish.timeEnd.after(now)));
        }
        query.where(qAdvertisementPublish.active.eq(true))
                .orderBy(qAdvertisementPublish.id.desc())
                .offset(0).limit(Constants.ADVERTISEMENT_PUBLISH_MAX);//一个店铺下的木爷广告不能超过5个

        return query.fetch();
    }
}
