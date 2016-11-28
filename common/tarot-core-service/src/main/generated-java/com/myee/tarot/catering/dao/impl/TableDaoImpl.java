package com.myee.tarot.catering.dao.impl;

import com.myee.tarot.catering.dao.TableDao;
import com.myee.tarot.catering.domain.QTable;
import com.myee.tarot.catering.domain.Table;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.StringUtil;
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

    @Override
    public PageResult<Table> pageByStore(Long id, PageRequest pageRequest) {
        PageResult<Table> pageList = new PageResult<Table>();
        QTable qTable = QTable.table;
        JPQLQuery<Table> query = new JPAQuery(getEntityManager());

        query.from(qTable)
                .leftJoin(qTable.tableType)
                .fetchJoin()
                .leftJoin(qTable.tableZone)
                .fetchJoin();
        if (!StringUtil.isBlank(pageRequest.getQueryName())) {
            query.where(qTable.name.like("%" + pageRequest.getQueryName() + "%"));
        }
        query.where(qTable.store.id.eq(id));
        pageList.setRecordsTotal(query.fetchCount());
        query.orderBy(qTable.name.asc());
        if (pageRequest.getCount() > Constants.COUNT_PAGING_MARK) {
            query.offset(pageRequest.getOffset()).limit(pageRequest.getCount());
        }
        pageList.setList(query.fetch());
        return pageList;
    }
}
