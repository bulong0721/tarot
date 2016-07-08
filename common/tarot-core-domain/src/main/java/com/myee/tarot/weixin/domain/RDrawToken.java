package com.myee.tarot.weixin.domain;

//import com.myee.djinn.dto.DrawToken;
import com.myee.tarot.core.GenericEntity;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Martin on 2016/1/18.
 */
@Entity
@Table(name = "CA_DRAW_TOKEN")
public class RDrawToken extends GenericEntity<Long, RDrawToken> {
    @Id
    @Column(name = "CA_DRAW_TOKEN_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", pkColumnValue = "CA_DRAW_TOKEN_SEQ_NEXT_VAL", valueColumnName = "SEQ_COUNT", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    private Long   id;

    @Column(name = "WAIT_TOKEN_ID")
    private Long   waitTokenId;

    @Column(name = "TOKEN")
    private String token;

    @Column(name = "TIME_GENATED")
    private Date   timeGenated;

    @Column(name = "TIME_DRAWED")
    private Date   timeDrawed;

    @Column(name = "TIME_DEADLINE")
    private Date   timeDeadline;

    @Column(name = "TIME_EXCHANGED")
    private Date   timeExchanged;

    public Date getTimeExchanged() {
        return timeExchanged;
    }

    public void setTimeExchanged(Date timeExchanged) {
        this.timeExchanged = timeExchanged;
    }

    public Date getTimeDeadline() {
        return timeDeadline;
    }

    public void setTimeDeadline(Date timeDeadline) {
        this.timeDeadline = timeDeadline;
    }

    public Date getTimeDrawed() {
        return timeDrawed;
    }

    public void setTimeDrawed(Date timeDrawed) {
        this.timeDrawed = timeDrawed;
    }

    public Date getTimeGenated() {
        return timeGenated;
    }

    public void setTimeGenated(Date timeGenated) {
        this.timeGenated = timeGenated;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getWaitTokenId() {
        return waitTokenId;
    }

    public void setWaitTokenId(Long waitTokenId) {
        this.waitTokenId = waitTokenId;
    }

    /*public DrawToken toDto() {
        DrawToken drawToken = new DrawToken();
        return drawToken;
    }*/

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
}
