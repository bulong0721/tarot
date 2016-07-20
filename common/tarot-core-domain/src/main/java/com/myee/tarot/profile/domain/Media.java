package com.myee.tarot.profile.domain;

import com.myee.tarot.core.GenericEntity;

import javax.persistence.*;

/**
 * Created by Martin on 2016/4/14.
 */
@Entity
@Table(name = "C_MEDIA")
@Cacheable
public class Media extends GenericEntity<Long, Media> {

    @Id
    @Column(name = "MEDIA_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "MEDIA_SEQ_NEXT_VAL",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    protected Long id;

    @Column(name = "URL", nullable = false)
    @org.hibernate.annotations.Index(name = "MEDIA_URL_INDEX", columnNames = {"URL"})
    protected String url;

    @Column(name = "TITLE")
    @org.hibernate.annotations.Index(name = "MEDIA_TITLE_INDEX", columnNames = {"TITLE"})
    protected String title;

    @Column(name = "ALT_TEXT")
    protected String altText;

    @Column(name = "TAGS")
    protected String tags;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAltText() {
        return altText;
    }

    public void setAltText(String altText) {
        this.altText = altText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Media media = (Media) o;

        if (url != null ? !url.equals(media.url) : media.url != null) return false;
        if (title != null ? !title.equals(media.title) : media.title != null) return false;
        if (altText != null ? !altText.equals(media.altText) : media.altText != null) return false;
        return !(tags != null ? !tags.equals(media.tags) : media.tags != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (altText != null ? altText.hashCode() : 0);
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        return result;
    }
}
