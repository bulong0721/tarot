package com.myee.tarot.catering.dao.impl;

import com.myee.tarot.catering.dao.TableTypeDao;
import com.myee.tarot.catering.domain.QTableType;
import com.myee.tarot.catering.domain.TableType;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Martin on 2016/4/21.
 */
@Repository
public class TableTypeDaoImpl extends GenericEntityDaoImpl<Long, TableType> implements TableTypeDao {

    @Override
    public List<TableType> listByStore(long storeId) {
        QTableType qTableType = QTableType.tableType;

        JPQLQuery<TableType> query = new JPAQuery(getEntityManager());

        query.from(qTableType)
                .where(qTableType.store.id.eq(storeId));

        return query.fetch();
    }
}
