package com.myee.tarot.menu.dao.impl;

import com.myee.tarot.admin.domain.AdminUser;
import com.myee.tarot.admin.domain.QAdminUser;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.menu.dao.MenuDao;
import com.myee.tarot.menu.domain.Menu;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * Created by Martin on 2016/4/21.
 */
@Repository
public class MenuDaoImpl extends GenericEntityDaoImpl<Long, Menu> implements MenuDao {

}
