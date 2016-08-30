package com.myee.tarot.apiold.dao;

import com.myee.tarot.apiold.domain.MenuInfo;
import com.myee.tarot.core.dao.GenericEntityDao;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;

import java.util.List;

/**
 * Created by Chay on 2016/8/10.
 */
public interface MenuDao extends GenericEntityDao<Long, MenuInfo> {
    List<MenuInfo> listByStoreId(long id);

    PageResult<MenuInfo> pageByStore(Long id, PageRequest pageRequest);
}
