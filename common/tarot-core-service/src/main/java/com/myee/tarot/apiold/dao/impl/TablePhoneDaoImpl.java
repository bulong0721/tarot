package com.myee.tarot.apiold.dao.impl;

import com.myee.tarot.apiold.dao.TablePhoneDao;
import com.myee.tarot.apiold.domain.QTablePhone;
import com.myee.tarot.apiold.domain.TablePhone;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;

/**
 * Created by xiaoni on 2016/8/10.
 */
@Repository
public class TablePhoneDaoImpl extends GenericEntityDaoImpl<Long, TablePhone> implements TablePhoneDao {



    public TablePhone findByTableId(Long tableId){
        QTablePhone qTablePhone = QTablePhone.tablePhone;

        JPQLQuery<TablePhone> query = new JPAQuery(getEntityManager());

        query.from(qTablePhone)
                .where(qTablePhone.table.id.eq(tableId));

        return query.fetchOne();
    }
}
