package com.myee.tarot.core.domain;

/**
 * Created by Martin on 2016/4/28.
 */

import com.myee.tarot.core.audit.Auditable;
import com.myee.tarot.core.audit.AuditableListener;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
@EntityListeners(value = AuditableListener.class)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Description implements Serializable {
    private static final long serialVersionUID = -4335863941736710046L;

    @Id
    @Column(name = "DESCRIPTION_ID")
    @TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "DESCRIPTION_SEQ_NEXT_VAL",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    private Long id;

    @Embedded
    private Auditable auditable = new Auditable();

    @NotEmpty
    @Column(name = "NAME", nullable = false, length = 120)
    private String name;

    @Column(name = "TITLE", length = 100)
    private String title;

    @Column(name = "DESCRIPTION")
    @Type(type = "org.hibernate.type.StringClobType")
    private String description;

    public Description() {
    }

    public Auditable getAuditable() {
        return auditable;
    }

    public void setAuditable(Auditable auditable) {
        this.auditable = auditable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

