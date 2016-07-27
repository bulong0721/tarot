package com.myee.tarot.campaign.dao.impl;

import com.myee.tarot.campaign.dao.PriceInfoDao;
import com.myee.tarot.catalog.domain.Device;
import com.myee.tarot.catalog.domain.QDevice;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.campaign.domain.PriceInfo;
import com.myee.tarot.campaign.domain.QMerchantPrice;
import com.myee.tarot.campaign.domain.QPriceInfo;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2016/7/14.
 */
@Repository
public class PriceInfoDaoImpl extends GenericEntityDaoImpl<Long, PriceInfo> implements PriceInfoDao {
    @Override
    public List<PriceInfo> findByStatusAndKeyId(String keyId, int status) {
        QPriceInfo qPriceInfo = QPriceInfo.priceInfo;
        JPQLQuery<PriceInfo> query = new JPAQuery(getEntityManager());
        List<PriceInfo> result = query.from(qPriceInfo).where(qPriceInfo.status.eq(status).and(qPriceInfo.keyId.eq(keyId))).fetch();
        return result;
    }

    @Override
    public PageResult<PriceInfo> pageList(Long storeId,PageRequest pageRequest) {
        PageResult<PriceInfo> pageList = new PageResult<PriceInfo>();
        QPriceInfo qPriceInfo = QPriceInfo.priceInfo;
        JPQLQuery<PriceInfo> query = new JPAQuery(getEntityManager());
        if(StringUtils.isNotBlank(pageRequest.getQueryName())){
            query.where(qPriceInfo.checkCode.like("%" + pageRequest.getQueryName() + "%"));
        }
        query.from(qPriceInfo).where(qPriceInfo.status.eq(Constants.PRICEINFO_USED).and(qPriceInfo.price.storeId.eq(storeId))).orderBy(qPriceInfo.checkDate.desc());
        pageList.setRecordsTotal(query.fetchCount());
        if( pageRequest.getCount() > 0){
            query.offset(pageRequest.getOffset()).limit(pageRequest.getCount());
        }
        pageList.setList(query.fetch());
        return pageList;
    }

    @Override
    public PriceInfo priceCheckCode(Long storeId, String checkCode) {
        QPriceInfo qPriceInfo = QPriceInfo.priceInfo;
        JPQLQuery<PriceInfo> query = new JPAQuery(getEntityManager());
        List<PriceInfo> result = query.from(qPriceInfo).where(qPriceInfo.checkCode.eq(checkCode).and(qPriceInfo.price.storeId.eq(storeId))).fetch();
        if(result!=null&&result.size()>0){
            return result.get(0);
        }else{
            return null;
        }
    }

    @Override
    public List<PriceInfo> findByStoreIdAndKeyId(Long storeId, String keyId) {
        QPriceInfo qPriceInfo = QPriceInfo.priceInfo;
        JPQLQuery<PriceInfo> query = new JPAQuery(getEntityManager());
        List<PriceInfo> result = query.from(qPriceInfo).where(qPriceInfo.keyId.eq(keyId).and(qPriceInfo.price.storeId.eq(storeId))).fetch();
        return result;
    }


}
