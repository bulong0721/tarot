package com.myee.tarot.resource.dao.impl;

import com.myee.tarot.catalog.dao.ProductUsedDao;
import com.myee.tarot.catalog.domain.ProductUsed;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.core.util.StringUtil;
import com.myee.tarot.resource.dao.UpdateConfigProductUsedXREFDao;
import com.myee.tarot.resource.domain.QUpdateConfigProductUsedXREF;
import com.myee.tarot.resource.domain.UpdateConfigProductUsedXREF;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Project Name : tarot
 * User: Jelynn
 * Date: 2016/12/19
 * Time: 16:05
 * Describe:
 * Version:1.0
 */
@Repository
public class UpdateConfigProductUsedXREFDaoImpl extends GenericEntityDaoImpl<Long, UpdateConfigProductUsedXREF> implements UpdateConfigProductUsedXREFDao {

	@Autowired
	private ProductUsedDao productUsedDao;

	@Override
	public UpdateConfigProductUsedXREF getByTypeAndDeviceGroupNO(String type, String deviceGroupNO) {
		QUpdateConfigProductUsedXREF qUpdateConfig = QUpdateConfigProductUsedXREF.updateConfigProductUsedXREF;
		JPQLQuery<UpdateConfigProductUsedXREF> query = new JPAQuery(getEntityManager());
		query.from(qUpdateConfig);
		if (StringUtil.isNullOrEmpty(type) || StringUtil.isNullOrEmpty(deviceGroupNO)) {
			throw new IllegalArgumentException("type and deviceGroupNO must be not null");
		}
		query.where(qUpdateConfig.type.eq(type));
		ProductUsed productUsed = productUsedDao.getByCode(deviceGroupNO);
		if(null == productUsed){
			return null;
		}
		query.where(qUpdateConfig.productUsed.eq(productUsed));
		List<UpdateConfigProductUsedXREF> list = query.fetch();
		if(null == list || list.isEmpty() || list.size() > 1){
			return null; //TODO
		}
		return list.get(0);
	}
}
