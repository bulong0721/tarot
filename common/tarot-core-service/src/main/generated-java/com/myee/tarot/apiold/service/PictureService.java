package com.myee.tarot.apiold.service;

import com.myee.tarot.apiold.domain.Picture;
import com.myee.tarot.core.service.GenericEntityService;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;

/**
 * Created by Chay on 2016/8/10.
 */
public interface PictureService extends GenericEntityService<Long, Picture> {
    PageResult<Picture> pageByTypeStore(Integer userType, Long storeId, PageRequest pageRequest);
}
