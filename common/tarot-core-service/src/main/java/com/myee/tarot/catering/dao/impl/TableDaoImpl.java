package com.myee.tarot.catering.dao.impl;

import com.myee.tarot.catering.dao.TableDao;
import com.myee.tarot.catering.domain.QTable;
import com.myee.tarot.catering.domain.Table;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Martin on 2016/4/21.
 */
@Repository
public class TableDaoImpl extends GenericEntityDaoImpl<Long, Table> implements TableDao {

    @Override
    public List<Table> listByStore(long storeId) {
        QTable qTable = QTable.table;

        JPQLQuery<Table> query = new JPAQuery(getEntityManager());

        query.from(qTable)
                .where(qTable.store.id.eq(storeId));

        return query.fetch();
    }
}
