package com.myee.tarot.menu.domain;

import com.myee.tarot.cms.domain.Page;
import com.myee.tarot.core.GenericEntity;
import com.myee.tarot.reference.domain.Media;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by Martin on 2016/4/18.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "C_CMS_MENU_ITEM")
public class MenuItem extends GenericEntity<Long, MenuItem> {

    @Id
    @Column(name = "MENU_ITEM_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "MENU_ITEM_SEQ_NEXT_VAL")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    protected Long id;

    @Column(name = "LABEL")
    protected String label;

    @Column(name = "MENU_ITEM_TYPE")
    protected String type;

    @Column(name = "SEQUENCE", precision = 10, scale = 6)
    protected BigDecimal sequence;

    @ManyToOne(optional = true, targetEntity = Menu.class, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "PARENT_MENU_ID")
    protected Menu parentMenu;

    @Column(name = "ACTION_URL")
    protected String actionUrl;

    @ManyToOne(targetEntity = Media.class)
    @JoinColumn(name = "MEDIA_ID")
    protected Media image;

    @Column(name = "ALT_TEXT")
    protected String altText;

    @ManyToOne(targetEntity = Menu.class)
    @JoinColumn(name = "LINKED_MENU_ID")
    protected Menu linkedMenu;

    @ManyToOne(targetEntity = Page.class)
    @JoinColumn(name = "LINKED_PAGE_ID")
    protected Page linkedPage;

    @Lob
    @Type(type = "org.hibernate.type.StringClobType")
    @Column(name = "CUSTOM_HTML", length = Integer.MAX_VALUE - 1)
    protected String customHtml;

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }

    public String getAltText() {
        return altText;
    }

    public void setAltText(String altText) {
        this.altText = altText;
    }

    public String getCustomHtml() {
        return customHtml;
    }

    public void setCustomHtml(String customHtml) {
        this.customHtml = customHtml;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Media getImage() {
        return image;
    }

    public void setImage(Media image) {
        this.image = image;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Menu getLinkedMenu() {
        return linkedMenu;
    }

    public void setLinkedMenu(Menu linkedMenu) {
        this.linkedMenu = linkedMenu;
    }

    public Menu getParentMenu() {
        return parentMenu;
    }

    public void setParentMenu(Menu parentMenu) {
        this.parentMenu = parentMenu;
    }

    public BigDecimal getSequence() {
        return sequence;
    }

    public void setSequence(BigDecimal sequence) {
        this.sequence = sequence;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Page getLinkedPage() {
        return linkedPage;
    }

    public void setLinkedPage(Page linkedPage) {
        this.linkedPage = linkedPage;
    }
}
