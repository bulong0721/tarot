package com.myee.tarot.menu.dao.impl;

import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.menu.dao.MenuItemDao;
import com.myee.tarot.menu.domain.MenuItem;
import org.springframework.stereotype.Repository;

/**
 * Created by Martin on 2016/4/21.
 */
@Repository
public class MenuItemDaoImpl extends GenericEntityDaoImpl<Long, MenuItem> implements MenuItemDao {

}
