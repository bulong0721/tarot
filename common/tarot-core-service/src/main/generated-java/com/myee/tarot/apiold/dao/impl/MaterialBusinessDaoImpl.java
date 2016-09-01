package com.myee.tarot.apiold.dao.impl;

import com.myee.tarot.apiold.dao.MaterialBusinessDao;
import com.myee.tarot.apiold.domain.MaterialBusiness;
import com.myee.tarot.apiold.domain.QMaterialBusiness;
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
public class MaterialBusinessDaoImpl extends GenericEntityDaoImpl<Long, MaterialBusiness> implements MaterialBusinessDao {
    public List<MaterialBusiness> listByTypeStoreTime(Long storeId,int type, Date now){
        QMaterialBusiness qMaterialBusiness = QMaterialBusiness.materialBusiness;

        JPQLQuery<MaterialBusiness> query = new JPAQuery(getEntityManager());

        query.from(qMaterialBusiness);
        if(storeId != null){
            query.where(qMaterialBusiness.store.id.eq(storeId));
        }
        if(now != null){
            query.where((qMaterialBusiness.timeStart.before(now))
                    .and(qMaterialBusiness.timeEnd.after(now)));
        }
        query.where((qMaterialBusiness.type.eq(type)).and(qMaterialBusiness.active.eq(true)))
            .orderBy(qMaterialBusiness.id.desc());
        //本店素材推送只能有一个
        if(type == Constants.API_OLD_TYPE_SHOP) {
            query.offset(0).limit(1);
        }
        return query.fetch();
    }

    public List<MaterialBusiness> listByMaterialFileKind(String materialFileKind){
        QMaterialBusiness qMaterialBusiness = QMaterialBusiness.materialBusiness;

        JPQLQuery<MaterialBusiness> query = new JPAQuery(getEntityManager());

        query.from(qMaterialBusiness)
                .where(qMaterialBusiness.material.materialFileKind.name.eq(materialFileKind))
                .where((qMaterialBusiness.type.eq(Constants.API_OLD_TYPE_MUYE)).and(qMaterialBusiness.active.eq(true)))
                .orderBy(qMaterialBusiness.id.desc());

        return query.fetch();
    }
}
