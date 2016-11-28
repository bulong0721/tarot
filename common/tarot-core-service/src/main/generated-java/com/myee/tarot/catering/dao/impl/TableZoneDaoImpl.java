package com.myee.tarot.catering.dao.impl;

import com.myee.tarot.catering.dao.TableZoneDao;
import com.myee.tarot.catering.domain.QTableZone;
import com.myee.tarot.catering.domain.TableZone;
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
public class TableZoneDaoImpl extends GenericEntityDaoImpl<Long, TableZone> implements TableZoneDao {

    @Override
    public List<TableZone> listByStore(long storeId) {
        QTableZone qTableZone = QTableZone.tableZone;

        JPQLQuery<TableZone> query = new JPAQuery(getEntityManager());

        query.from(qTableZone)
                .where(qTableZone.store.id.eq(storeId));

        return query.fetch();
    }

    @Override
    public PageResult<TableZone> pageByStore(Long id, PageRequest pageRequest){
        PageResult<TableZone> pageList = new PageResult<TableZone>();
        QTableZone qTableZone = QTableZone.tableZone;
        JPQLQuery<TableZone> query = new JPAQuery(getEntityManager());
        query.from(qTableZone);
        if(!StringUtil.isBlank(pageRequest.getQueryName())){
            query.where(qTableZone.name.like("%" + pageRequest.getQueryName() + "%"));
        }
        query.where(qTableZone.store.id.eq(id));
        pageList.setRecordsTotal(query.fetchCount());
        query.orderBy(qTableZone.name.asc());
        if( pageRequest.getCount() > Constants.COUNT_PAGING_MARK){
            query.offset(pageRequest.getOffset()).limit(pageRequest.getCount());
        }
        pageList.setList(query.fetch());
        return pageList;
    }
}
