package com.myee.tarot.apiold.service.impl;

import com.myee.tarot.apiold.dao.MaterialPublishDao;
import com.myee.tarot.apiold.domain.MaterialPublish;
import com.myee.tarot.apiold.service.MaterialPublishService;
import com.myee.tarot.core.service.GenericEntityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by Chay on 2016/8/10.
 */
@Service
public class MaterialPublishServiceImpl extends GenericEntityServiceImpl<Long, MaterialPublish> implements MaterialPublishService {

    protected MaterialPublishDao materialPublishDao;

    @Autowired
    public MaterialPublishServiceImpl(MaterialPublishDao materialPublishDao) {
        super(materialPublishDao);
        this.materialPublishDao = materialPublishDao;
    }

    public List<MaterialPublish> listByStore(Long storeId,Date now){
        return materialPublishDao.listByStore(storeId,now);
    }
}
