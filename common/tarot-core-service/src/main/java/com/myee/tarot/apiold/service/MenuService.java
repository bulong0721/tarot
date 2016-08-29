package com.myee.tarot.apiold.service;

import com.myee.tarot.apiold.domain.MenuInfo;
import com.myee.tarot.apiold.view.MenuInfoView;
import com.myee.tarot.core.service.GenericEntityService;

import java.util.List;

/**
 * Created by Chay on 2016/8/10.
 */
public interface MenuService extends GenericEntityService<Long, MenuInfo> {
    List<MenuInfo> listByStoreId(long id);
}
