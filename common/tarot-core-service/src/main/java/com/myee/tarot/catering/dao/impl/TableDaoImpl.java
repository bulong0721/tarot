package com.myee.tarot.catering.dao.impl;

import com.myee.tarot.catering.dao.TableDao;
import com.myee.tarot.catering.domain.QTable;
import com.myee.tarot.catering.domain.Table;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.lang3.StringUtils;
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
    public PageResult<Table> pageByStore(Long id, PageRequest pageRequest){
        PageResult<Table> pageList = new PageResult<Table>();
        QTable qTable = QTable.table;
        JPQLQuery<Table> query = new JPAQuery(getEntityManager());
        if(StringUtils.isNotBlank(pageRequest.getQueryName())){
            query.where(qTable.name.like("%" + pageRequest.getQueryName() + "%"));
        }
        query.where(qTable.store.id.eq(id));
        pageList.setRecordsTotal(query.from(qTable).fetchCount());
        if( pageRequest.getCount() > 0) {
            query.offset(pageRequest.getOffset()).limit(pageRequest.getCount());
        }
        pageList.setList(query.fetch());
        return pageList;
    }
}
