package com.myee.tarot.apiold.dao;

import com.myee.tarot.apiold.domain.Picture;
import com.myee.tarot.core.dao.GenericEntityDao;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;

/**
 * Created by Chay on 2016/8/10.
 */
public interface PictureDao extends GenericEntityDao<Long, Picture> {
    PageResult<Picture> pageByTypeStore(Integer userType, Long storeId, PageRequest pageRequest);
}
