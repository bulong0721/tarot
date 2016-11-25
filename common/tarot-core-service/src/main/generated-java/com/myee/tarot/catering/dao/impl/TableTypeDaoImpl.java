package com.myee.tarot.catering.dao.impl;

import com.myee.tarot.catering.dao.TableTypeDao;
import com.myee.tarot.catering.domain.QTableType;
import com.myee.tarot.catering.domain.TableType;
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
public class TableTypeDaoImpl extends GenericEntityDaoImpl<Long, TableType> implements TableTypeDao {

    @Override
    public List<TableType> listByStore(long storeId) {
        QTableType qTableType = QTableType.tableType;

        JPQLQuery<TableType> query = new JPAQuery(getEntityManager());

        query.from(qTableType)
                .where(qTableType.store.id.eq(storeId));

        return query.fetch();
    }

    @Override
    public PageResult<TableType> pageByStore(Long id, PageRequest pageRequest){
        PageResult<TableType> pageList = new PageResult<TableType>();
        QTableType qTableType = QTableType.tableType;
        JPQLQuery<TableType> query = new JPAQuery(getEntityManager());
        query.from(qTableType);
        if(!StringUtil.isBlank(pageRequest.getQueryName())){
            query.where(qTableType.name.like("%" + pageRequest.getQueryName() + "%"));
        }
        query.where(qTableType.store.id.eq(id));
        pageList.setRecordsTotal(query.fetchCount());
        query.orderBy(qTableType.name.asc());
        if( pageRequest.getCount() > Constants.COUNT_PAGING_MARK){
            query.offset(pageRequest.getOffset()).limit(pageRequest.getCount());
        }
        pageList.setList(query.fetch());
        return pageList;
    }

    @Override
    public List<TableType> listByMerchantStoreId(Long merchantStoreId) {
        return null;
    }
}
