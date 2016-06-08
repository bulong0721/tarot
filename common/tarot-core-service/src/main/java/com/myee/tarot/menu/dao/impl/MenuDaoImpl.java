package com.myee.tarot.menu.dao.impl;

import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.menu.dao.MenuDao;
import com.myee.tarot.menu.domain.Menu;
import org.springframework.stereotype.Repository;

/**
 * Created by Martin on 2016/4/21.
 */
@Repository
public class MenuDaoImpl extends GenericEntityDaoImpl<Long, Menu> implements MenuDao {

}
