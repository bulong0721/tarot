package com.myee.tarot.menu.domain;

import com.myee.tarot.core.GenericEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Martin on 2016/4/18.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "C_CMS_MENU")
public class Menu extends GenericEntity<Long, Menu> {

    @Id
    @Column(name = "MENU_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "MENU_SEQ_NEXT_VAL",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    protected Long id;

    @Column(name = "NAME", nullable = false)
    protected String name;

    @OneToMany(mappedBy = "parentMenu", targetEntity = MenuItem.class, cascade = {CascadeType.ALL}, orphanRemoval = true)
    @OrderBy(value = "sequence")
    protected List<MenuItem> menuItems = new ArrayList<MenuItem>(20);

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
