package com.myee.tarot.reference.domain;

import com.myee.tarot.core.GenericEntity;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;

/**
 * Created by Martin on 2016/4/14.
 */
@Entity
@Table(name = "C_GEOZONE")
@Cacheable
public class GeoZone extends GenericEntity<Long, GeoZone> {

    @Id
    @Column(name = "ZONE_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "ZONE_SEQ_NEXT_VAL")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    protected Long id;

    @NotEmpty
    @Column(name = "ZONE_NAME")
    private String name;

    @NotEmpty
    @Column(name = "ZONE_CODE")
    private String code;//区号

    @Column(name = "PARENT")
    private Long parent;

//    @NotEmpty //对Integer会报错
    @Column(name = "ZONE_LEVEL")
    private int level;//区域级别0国家，1省/直辖市，2市，3区县

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
