package com.myee.tarot.address.dao.impl;

import com.myee.tarot.address.dao.GeoZoneDao;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.reference.domain.GeoZone;
import com.myee.tarot.reference.domain.QGeoZone;
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

    public List<GeoZone> listProvince() {
        QGeoZone qGeoZone = QGeoZone.geoZone;
        JPQLQuery<GeoZone> query = new JPAQuery(getEntityManager());
        query.from(qGeoZone)
                .where(qGeoZone.level.eq(1));

        return query.fetch();
    }

    public List<GeoZone> listCityByProvince(Long provinceId) {
        QGeoZone qGeoZone = QGeoZone.geoZone;
        JPQLQuery<GeoZone> query = new JPAQuery(getEntityManager());
        query.from(qGeoZone)
                .where(qGeoZone.level.eq(2))
                .where(qGeoZone.parent.eq(provinceId));

        return query.fetch();
    }

    public List<GeoZone> listDistrictByCity(Long cityId) {
        QGeoZone qGeoZone = QGeoZone.geoZone;
        JPQLQuery<GeoZone> query = new JPAQuery(getEntityManager());
        query.from(qGeoZone)
                .where(qGeoZone.level.eq(3))
                .where(qGeoZone.parent.eq(cityId));

        return query.fetch();
    }
}
