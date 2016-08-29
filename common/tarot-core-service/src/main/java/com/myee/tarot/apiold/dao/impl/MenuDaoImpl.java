package com.myee.tarot.apiold.dao.impl;

import com.myee.tarot.apiold.dao.MenuDao;
import com.myee.tarot.apiold.domain.MenuInfo;
import com.myee.tarot.apiold.domain.QMenuInfo;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Chay on 2016/8/10.
 */
@Repository
public class MenuDaoImpl extends GenericEntityDaoImpl<Long, MenuInfo> implements MenuDao {

    public List<MenuInfo> listByStoreId(long id){
        QMenuInfo qMenuInfo = QMenuInfo.menuInfo;

        JPQLQuery<MenuInfo> query = new JPAQuery(getEntityManager());

        query.from(qMenuInfo);
        query.where(qMenuInfo.active.eq(1));
        query.where(qMenuInfo.store.id.eq(id));

        return query.fetch();
    }
}
