package com.myee.tarot.resource.dao;

import com.myee.tarot.core.dao.GenericEntityDao;
import com.myee.tarot.resource.domain.UpdateConfigProductUsedXREF;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Project Name : tarot
 * User: Jelynn
 * Date: 2016/12/19
 * Time: 16:04
 * Describe:
 * Version:1.0
 */
public interface UpdateConfigProductUsedXREFDao extends GenericEntityDao<Long, UpdateConfigProductUsedXREF> {

	UpdateConfigProductUsedXREF getByTypeAndDeviceGroupNO(String type, String deviceGroupNO);

	List<UpdateConfigProductUsedXREF> listByConfigId(Long configId);
}
