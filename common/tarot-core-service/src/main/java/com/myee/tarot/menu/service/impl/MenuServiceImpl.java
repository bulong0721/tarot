package com.myee.tarot.menu.service.impl;

import com.myee.tarot.admin.dao.AdminUserDao;
import com.myee.tarot.admin.domain.AdminUser;
import com.myee.tarot.admin.service.AdminUserService;
import com.myee.tarot.core.dao.GenericEntityDao;
import com.myee.tarot.core.service.GenericEntityServiceImpl;
import com.myee.tarot.core.service.GenericResponse;
import com.myee.tarot.menu.dao.MenuDao;
import com.myee.tarot.menu.dao.MenuItemDao;
import com.myee.tarot.menu.domain.Menu;
import com.myee.tarot.menu.domain.MenuItem;
import com.myee.tarot.menu.dto.MenuItemDTO;
import com.myee.tarot.menu.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Martin on 2016/4/21.
 */
@Service
public class MenuServiceImpl extends GenericEntityServiceImpl<Long, Menu> implements MenuService {

    protected  MenuDao menuDao;

    @Autowired
    protected MenuItemDao menuItemDao;

    @Autowired
    public MenuServiceImpl(MenuDao menuDao) {
        super(menuDao);
        this.menuDao = menuDao;
    }

    @Override
    public MenuItem findMenuItemById(Long menuItemId) {
        return menuItemDao.getById(menuItemId);
    }

    @Override
    public List<MenuItemDTO> buildMenuItemDTOs4Menu(Menu menu) {
        return null;
    }
}
