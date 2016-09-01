package com.myee.tarot.apiold.dao;

import com.myee.tarot.apiold.domain.MaterialBusiness;
import com.myee.tarot.core.dao.GenericEntityDao;

import java.util.Date;
import java.util.List;

/**
 * Created by Chay on 2016/8/10.
 */
public interface MaterialBusinessDao extends GenericEntityDao<Long, MaterialBusiness> {
    List<MaterialBusiness> listByTypeStoreTime(Long storeId, int type,Date now);

    List<MaterialBusiness> listByMaterialFileKind(String materialFileKind);
}
