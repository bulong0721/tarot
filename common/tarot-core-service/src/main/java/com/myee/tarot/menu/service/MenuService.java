package com.myee.tarot.menu.service;

import com.myee.tarot.core.service.GenericEntityService;
import com.myee.tarot.menu.domain.Menu;
import com.myee.tarot.menu.domain.MenuItem;
import com.myee.tarot.menu.dto.MenuItemDTO;

import java.util.List;

/**
 * Created by Martin on 2016/4/20.
 */
public interface MenuService extends GenericEntityService<Long, Menu> {

    MenuItem findMenuItemById(Long menuItemId);

    List<MenuItemDTO> buildMenuItemDTOs4Menu(Menu menu);
}
