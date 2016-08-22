package com.myee.tarot.apiold.service.impl;

import com.myee.tarot.apiold.dao.MaterialBusinessDao;
import com.myee.tarot.apiold.dao.SendRecordDao;
import com.myee.tarot.apiold.domain.MaterialBusiness;
import com.myee.tarot.apiold.domain.SendRecord;
import com.myee.tarot.apiold.service.MaterialBusinessService;
import com.myee.tarot.apiold.service.SendRecordService;
import com.myee.tarot.core.service.GenericEntityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by Chay on 2016/8/10.
 */
@Service
public class MaterialBusinessServiceImpl extends GenericEntityServiceImpl<Long, MaterialBusiness> implements MaterialBusinessService {

    protected MaterialBusinessDao materialBusinessDao;

    @Autowired
    public MaterialBusinessServiceImpl(MaterialBusinessDao materialBusinessDao) {
        super(materialBusinessDao);
        this.materialBusinessDao = materialBusinessDao;
    }

    public List<MaterialBusiness> listByStore(Long storeId,Date now){
        return materialBusinessDao.listByStore(storeId, now);
    }

    public List<MaterialBusiness> listByMaterialFileKind(String materialFileKind){
        return materialBusinessDao.listByMaterialFileKind(materialFileKind);
    }
}
