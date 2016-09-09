package com.myee.tarot.apiold.dao.impl;

import com.myee.tarot.apiold.dao.RollDetailDao;
import com.myee.tarot.apiold.domain.QRollDetail;
import com.myee.tarot.apiold.domain.RollDetail;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.querydsl.jpa.JPQLQueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

/**
 * Created by Chay on 2016/8/10.
 */
@Repository
public class RollDetailDaoImpl extends GenericEntityDaoImpl<Long, RollDetail> implements RollDetailDao {

    public void deleteByRollMain(Long rollMainId){
        QRollDetail qDeviceAttribute = QRollDetail.rollDetail;
        JPQLQueryFactory queryFactory = new JPAQueryFactory(getEntityManager());

        queryFactory.delete(qDeviceAttribute)
                .where(qDeviceAttribute.rollMain.id.eq(rollMainId))
                .execute();
    }
}
