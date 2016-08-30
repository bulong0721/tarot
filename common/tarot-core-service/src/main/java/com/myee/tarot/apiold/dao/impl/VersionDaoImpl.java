package com.myee.tarot.apiold.dao.impl;

import com.myee.tarot.apiold.dao.VersionDao;
import com.myee.tarot.apiold.domain.MenuInfo;
import com.myee.tarot.apiold.domain.QMenuInfo;
import com.myee.tarot.apiold.domain.QVersionInfo;
import com.myee.tarot.apiold.domain.VersionInfo;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;

/**
 * Created by Chay on 2016/8/10.
 */
@Repository
public class VersionDaoImpl extends GenericEntityDaoImpl<Long, VersionInfo> implements VersionDao {
    public VersionInfo getByStoreId(Long shopId){
        QVersionInfo qVersionInfo = QVersionInfo.versionInfo;

        JPQLQuery<VersionInfo> query = new JPAQuery(getEntityManager());

        query.from(qVersionInfo);
        query.where(qVersionInfo.active.eq(true));
        query.where(qVersionInfo.store.id.eq(shopId));

        return query.fetchOne();
    }
}
