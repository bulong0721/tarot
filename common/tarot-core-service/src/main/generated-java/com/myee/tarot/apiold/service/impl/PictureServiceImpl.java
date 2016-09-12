package com.myee.tarot.apiold.service.impl;

import com.myee.tarot.apiold.dao.PictureDao;
import com.myee.tarot.apiold.domain.Picture;
import com.myee.tarot.apiold.service.PictureService;
import com.myee.tarot.core.service.GenericEntityServiceImpl;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Chay on 2016/8/10.
 */
@Service
public class PictureServiceImpl extends GenericEntityServiceImpl<Long, Picture> implements PictureService {

    protected PictureDao pictureDao;

    @Autowired
    public PictureServiceImpl(PictureDao pictureDao) {
        super(pictureDao);
        this.pictureDao = pictureDao;
    }

    @Override
    public PageResult<Picture> pageByTypeStore(Integer userType, Long storeId, PageRequest pageRequest){
        return pictureDao.pageByTypeStore( userType, storeId, pageRequest);
    }
}
