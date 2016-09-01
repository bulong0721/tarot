package com.myee.tarot.apiold.dao.impl;

import com.myee.tarot.apiold.dao.AdvertisementDao;
import com.myee.tarot.apiold.domain.Advertisement;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import org.springframework.stereotype.Repository;

/**
 * Created by Chay on 2016/8/10.
 */
@Repository
public class AdvertisementDaoImpl extends GenericEntityDaoImpl<Long, Advertisement> implements AdvertisementDao {
}
