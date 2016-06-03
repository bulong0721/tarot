package com.myee.tarot.catering.dao.impl;

import com.myee.tarot.catering.dao.TableTypeDao;
import com.myee.tarot.catering.dao.TableZoneDao;
import com.myee.tarot.catering.domain.QTableType;
import com.myee.tarot.catering.domain.QTableZone;
import com.myee.tarot.catering.domain.TableType;
import com.myee.tarot.catering.domain.TableZone;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Martin on 2016/4/21.
 */
@Repository
public class TableZoneDaoImpl extends GenericEntityDaoImpl<Long, TableZone> implements TableZoneDao {

    @Override
    public List<TableZone> listByStore(long storeId) {
        QTableZone qTableZone = QTableZone.tableZone;

        JPQLQuery<TableZone> query = new JPAQuery(getEntityManager());

        query.from(qTableZone)
                .where(qTableZone.store.id.eq(storeId));

        return query.fetch();
    }
}
