package com.myee.tarot.apiold.dao.impl;

import com.myee.tarot.apiold.dao.MaterialPublishDao;
import com.myee.tarot.apiold.domain.MaterialPublish;
import com.myee.tarot.apiold.domain.QMaterialPublish;
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
public class MaterialPublishDaoImpl extends GenericEntityDaoImpl<Long, MaterialPublish> implements MaterialPublishDao {

    public List<MaterialPublish> listByStore(Long storeId,Date now){
        QMaterialPublish qMaterialPublish = QMaterialPublish.materialPublish;

        JPQLQuery<MaterialPublish> query = new JPAQuery(getEntityManager());

        query.from(qMaterialPublish);

        if(storeId != null){
            query.where(qMaterialPublish.store.id.eq(storeId));
        }
        if(now != null){
            query.where((qMaterialPublish.timeStart.before(now))
                    .and(qMaterialPublish.timeEnd.after(now)));
        }
        query.where((qMaterialPublish.materialBusiness.type.eq(1)).and(qMaterialPublish.active.eq(1)))
                .orderBy(qMaterialPublish.id.desc())
                .offset(0).limit(30);

        return query.fetch();
    }
}
