package com.myee.tarot.apiold.dao.impl;

import com.myee.tarot.apiold.dao.MenuDao;
import com.myee.tarot.apiold.domain.MenuInfo;
import com.myee.tarot.apiold.domain.QMenuInfo;
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
 * Created by Chay on 2016/8/10.
 */
@Repository
public class MenuDaoImpl extends GenericEntityDaoImpl<Long, MenuInfo> implements MenuDao {

    public List<MenuInfo> listByStoreId(long id){
        QMenuInfo qMenuInfo = QMenuInfo.menuInfo;

        JPQLQuery<MenuInfo> query = new JPAQuery(getEntityManager());

        query.from(qMenuInfo);
        query.where(qMenuInfo.active.eq(true));
        query.where(qMenuInfo.store.id.eq(id));

        return query.fetch();
    }

    @Override
    public PageResult<MenuInfo> pageByStore(Long id, PageRequest pageRequest) {
        PageResult<MenuInfo> pageList = new PageResult<MenuInfo>();
        QMenuInfo qMenuInfo = QMenuInfo.menuInfo;
        JPQLQuery<MenuInfo> query = new JPAQuery(getEntityManager());

        query.from(qMenuInfo);

        if (!StringUtil.isBlank(pageRequest.getQueryName())) {
            query.where(qMenuInfo.name.like("%" + pageRequest.getQueryName() + "%"));
        }
        query.where(qMenuInfo.store.id.eq(id));
        pageList.setRecordsTotal(query.fetchCount());
        if (pageRequest.getCount() > Constants.COUNT_PAGING_MARK) {
            query.offset(pageRequest.getOffset()).limit(pageRequest.getCount());
        }
        pageList.setList(query.fetch());
        return pageList;
    }
}
