package com.myee.tarot.weixin.domain;

import com.myee.tarot.core.GenericEntity;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Ray.Fu on 2016/5/10.
 */
@Entity
@Table(name = "CA_FEED_BACK")
@DynamicUpdate
public class WFeedBack extends GenericEntity<Long, WFeedBack> {
    @Id
    @Column(name = "FEEDBACK_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", pkColumnValue = "CA_FEED_BACK_SEQ_NEXT_VAL", valueColumnName = "SEQ_COUNT", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    private Long   id;

    @Column(name = "FEEDBACK_OPENID")
    private String feedBackOpenId;

    @Column(name = "FEEDBACK_CONTEXT")
    private String feedBackContext;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFeedBackOpenId() {
        return feedBackOpenId;
    }

    public void setFeedBackOpenId(String feedBackOpenId) {
        this.feedBackOpenId = feedBackOpenId;
    }

    public String getFeedBackContext() {
        return feedBackContext;
    }

    public void setFeedBackContext(String feedBackContext) {
        this.feedBackContext = feedBackContext;
    }

}
