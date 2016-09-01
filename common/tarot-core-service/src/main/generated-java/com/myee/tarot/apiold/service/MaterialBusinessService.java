package com.myee.tarot.apiold.service;

import com.myee.tarot.apiold.domain.MaterialBusiness;
import com.myee.tarot.core.service.GenericEntityService;

import java.util.Date;
import java.util.List;

/**
 * Created by Chay on 2016/8/10.
 */
public interface MaterialBusinessService extends GenericEntityService<Long, MaterialBusiness> {
    List<MaterialBusiness> listByTypeStoreTime(Long storeId,int type, Date now);

    List<MaterialBusiness> listByMaterialFileKind(String materialFileKind);
}
