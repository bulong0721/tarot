package com.myee.tarot.catering.dao.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.myee.tarot.catering.dao.TableDao;
import com.myee.tarot.catering.domain.QTable;
import com.myee.tarot.catering.domain.Table;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.StringUtil;
import com.myee.tarot.core.util.WhereRequest;
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
    public PageResult<Table> pageByStore(Long id, WhereRequest whereRequest) {
        PageResult<Table> pageList = new PageResult<Table>();
        QTable qTable = QTable.table;
        JPQLQuery<Table> query = new JPAQuery(getEntityManager());

        query.from(qTable)
                .leftJoin(qTable.tableType)
                .fetchJoin()
                .leftJoin(qTable.tableZone)
                .fetchJoin();
        if (whereRequest.getQueryObj() != null) {
            JSONObject map = JSON.parseObject(whereRequest.getQueryObj());
            if (map.get(Constants.SEARCH_OPTION_TABLE_TYPE) != null && !StringUtil.isBlank(map.get(Constants.SEARCH_OPTION_TABLE_TYPE).toString())) {
                query.where(qTable.tableType.id.eq(Long.valueOf(map.get(Constants.SEARCH_OPTION_TABLE_TYPE).toString())));
            }
            if (map.get(Constants.SEARCH_OPTION_TABLE_ZONE) != null && !StringUtil.isBlank(map.get(Constants.SEARCH_OPTION_TABLE_ZONE).toString())) {
                query.where(qTable.tableZone.id.eq(Long.valueOf(map.get(Constants.SEARCH_OPTION_TABLE_ZONE).toString())));
            }
            if (map.get(Constants.SEARCH_OPTION_SCAN_CODE) != null && !StringUtil.isBlank(map.get(Constants.SEARCH_OPTION_SCAN_CODE).toString())) {
                query.where(qTable.scanCode.like("%" + map.get(Constants.SEARCH_OPTION_SCAN_CODE).toString() + "%"));
            }
            if (map.get(Constants.SEARCH_OPTION_TEXT_ID) != null && !StringUtil.isBlank(map.get(Constants.SEARCH_OPTION_TEXT_ID).toString())) {
                query.where(qTable.textId.like("%" + map.get(Constants.SEARCH_OPTION_TEXT_ID).toString() + "%"));
            }
            if (map.get(Constants.SEARCH_OPTION_NAME) != null && !StringUtil.isBlank(map.get(Constants.SEARCH_OPTION_NAME).toString())) {
                query.where(qTable.name.like("%" + map.get(Constants.SEARCH_OPTION_NAME).toString() + "%"));
            }
        } else if (!StringUtil.isBlank(whereRequest.getQueryName())) {
                query.where(qTable.name.like("%" + whereRequest.getQueryName() + "%"));
        }
        query.where(qTable.store.id.eq(id));
        pageList.setRecordsTotal(query.fetchCount());
        query.orderBy(qTable.name.asc());
        if (whereRequest.getCount() > Constants.COUNT_PAGING_MARK) {
            query.offset(whereRequest.getOffset()).limit(whereRequest.getCount());
        }
        pageList.setList(query.fetch());
        return pageList;
    }
}
