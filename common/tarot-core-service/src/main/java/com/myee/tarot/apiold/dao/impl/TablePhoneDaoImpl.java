package com.myee.tarot.apiold.dao.impl;

import com.myee.tarot.apiold.dao.TablePhoneDao;
import com.myee.tarot.apiold.domain.QTablePhone;
import com.myee.tarot.apiold.domain.TablePhone;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.StringUtil;
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

    public PageResult<TablePhone> pageByStore(Long id, PageRequest pageRequest){
        PageResult<TablePhone> pageList = new PageResult<TablePhone>();
        QTablePhone qTablePhone = QTablePhone.tablePhone;
        JPQLQuery<TablePhone> query = new JPAQuery(getEntityManager());

        query.from(qTablePhone)
                .leftJoin(qTablePhone.table)
                .fetchJoin();
        if (!StringUtil.isBlank(pageRequest.getQueryName())) {
            query.where(qTablePhone.table.name.like("%" + pageRequest.getQueryName() + "%"));
        }
        query.where(qTablePhone.store.id.eq(id));
        pageList.setRecordsTotal(query.fetchCount());
        if (pageRequest.getCount() > 0) {
            query.offset(pageRequest.getOffset()).limit(pageRequest.getCount());
        }
        pageList.setList(query.fetch());
        return pageList;
    }
}
