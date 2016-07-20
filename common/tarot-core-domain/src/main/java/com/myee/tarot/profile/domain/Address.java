package com.myee.tarot.profile.domain;

import com.myee.tarot.core.GenericEntity;

import javax.persistence.*;

/**
 * Created by Martin on 2016/4/14.
 */
@Entity
@Table(name = "C_ADDRESS")
public class Address extends GenericEntity<Long, Address> {
    @Id
    @Column(name = "ADDRESS_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "ADDRESS_SEQ_NEXT_VAL", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    protected Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, targetEntity = GeoZone.class)
    @JoinColumn(name = "PROVINCE_ZONE")
    private GeoZone province;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, targetEntity = GeoZone.class)
    @JoinColumn(name = "CITY_ZONE")
    private GeoZone city;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, targetEntity = GeoZone.class)
    @JoinColumn(name = "COUNTY_ZONE")
    private GeoZone county;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, targetEntity = GeoZone.class)
    @JoinColumn(name = "CIRCLE_ZONE")
    private GeoZone circle;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, targetEntity = GeoZone.class)
    @JoinColumn(name = "MALL_ZONE")
    private GeoZone mall;

    @Column(name = "ADDRESS", length = 255)
    private String address;

    @Column(name = "POSTAL_CODE")
    private String postalCode;

    @Column(name = "LONGITUDE")
    private Double longitude;

    @Column(name = "LATITUDE")
    private Double latitude;

    @Column(name = "TRAVEL_INFO", length = 255)
    private String travelInfo;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public GeoZone getCity() {
        return city;
    }

    public void setCity(GeoZone city) {
        this.city = city;
    }

    public GeoZone getCounty() {
        return county;
    }

    public void setCounty(GeoZone county) {
        this.county = county;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public GeoZone getMall() {
        return mall;
    }

    public void setMall(GeoZone mall) {
        this.mall = mall;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public GeoZone getProvince() {
        return province;
    }

    public void setProvince(GeoZone province) {
        this.province = province;
    }

    public String getTravelInfo() {
        return travelInfo;
    }

    public void setTravelInfo(String travelInfo) {
        this.travelInfo = travelInfo;
    }

    public GeoZone getCircle() {
        return circle;
    }

    public void setCircle(GeoZone circle) {
        this.circle = circle;
    }
}
