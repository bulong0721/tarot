package com.myee.tarot.profile.dao.impl;

import com.myee.tarot.profile.dao.GeoZoneDao;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.profile.domain.GeoZone;
import com.myee.tarot.profile.domain.QGeoZone;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Chay on 2016/5/25.
 */
@Repository
public class GeoZoneDaoImpl extends GenericEntityDaoImpl<Long, GeoZone> implements GeoZoneDao {

    public static Log log = LogFactory.getLog(GeoZoneDaoImpl.class);
    private static final int Level_Province = 1;
    private static final int Level_City     = 2;
    private static final int Level_District = 3;

    public List<GeoZone> listProvince() {
        QGeoZone qGeoZone = QGeoZone.geoZone;
        JPQLQuery<GeoZone> query = new JPAQuery(getEntityManager());
        query.from(qGeoZone)
                .where(qGeoZone.level.eq(Level_Province));

        return query.fetch();
    }

    public List<GeoZone> listCityByProvince(Long provinceId) {
        QGeoZone qGeoZone = QGeoZone.geoZone;
        JPQLQuery<GeoZone> query = new JPAQuery(getEntityManager());
        query.from(qGeoZone)
                .where(qGeoZone.level.eq(Level_City))
                .where(qGeoZone.parent.eq(provinceId));

        return query.fetch();
    }

    public List<GeoZone> listDistrictByCity(Long cityId) {
        QGeoZone qGeoZone = QGeoZone.geoZone;
        JPQLQuery<GeoZone> query = new JPAQuery(getEntityManager());
        query.from(qGeoZone)
                .where(qGeoZone.level.eq(Level_District))
                .where(qGeoZone.parent.eq(cityId));

        return query.fetch();
    }
}
