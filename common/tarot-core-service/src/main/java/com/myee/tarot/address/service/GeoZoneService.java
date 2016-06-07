package com.myee.tarot.address.service;

import com.myee.tarot.core.service.GenericEntityService;
import com.myee.tarot.reference.domain.GeoZone;

import java.util.List;

/**
 * Created by Chay on 2016/6/3.
 */
public interface GeoZoneService  extends GenericEntityService<Long, GeoZone> {
    List<GeoZone> listProvince();
    List<GeoZone> listCityByProvince(Long provinceId);
    List<GeoZone> listDistrictByCity(Long cityId);

}
