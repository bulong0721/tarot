package com.myee.tarot.apiold.dao.impl;

import com.myee.tarot.apiold.dao.AkSkDao;
import com.myee.tarot.apiold.domain.AkSk;
import com.myee.tarot.apiold.domain.QAkSk;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;

/**
 * Created by xiaoni on 2016/8/10.
 */
@Repository
public class AkSkDaoImpl extends GenericEntityDaoImpl<Long, AkSk> implements AkSkDao {

    public AkSk getByToken(String token){
        QAkSk qAkSk = QAkSk.akSk;

        JPQLQuery<AkSk> query = new JPAQuery(getEntityManager());

        query.from(qAkSk)
                .where(qAkSk.accessKey.eq(token));

        return query.fetchOne();
    }
}
