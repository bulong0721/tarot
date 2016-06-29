package com.myee.tarot.catering.dao.impl;

import com.myee.tarot.catering.dao.TableZoneDao;
import com.myee.tarot.catering.domain.QTableZone;
import com.myee.tarot.catering.domain.TableZone;
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
        if(StringUtils.isNotBlank(pageRequest.getQueryName())){
            query.where(qTableZone.name.like("%" + pageRequest.getQueryName() + "%"));
        }
        query.where(qTableZone.store.id.eq(id));
        pageList.setRecordsTotal(query.from(qTableZone).fetchCount());
        if( pageRequest.getLength() > 0){
            query.offset(pageRequest.getStart()).limit(pageRequest.getLength());
        }
        pageList.setList(query.fetch());
        return pageList;
    }
}
