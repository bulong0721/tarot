package com.myee.tarot.catalog.domain;

import com.myee.tarot.core.GenericEntity;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Ray.Fu on 2016/8/10.
 */
@Entity
@Table(name = "C_VOICE_LOG")
public class VoiceLog extends GenericEntity<Long, VoiceLog> {

    @Id
    @Column(name = "VOICE_LOG_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "DEVICE_SEQ_NEXT_VAL",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    protected Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TIME")
    protected Date time;

    @Column(name = "COOKIE_LISTEN")
    private String cookieListen;

    @Column(name = "COOKIE_SPEAK")
    private String cookieSpeak;

    @Column(name = "TYPE")
    private String type;

    @Override
    public Long getId() {
        return null;
    }

    @Override
    public void setId(Long id) {

    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getCookieListen() {
        return cookieListen;
    }

    public void setCookieListen(String cookieListen) {
        this.cookieListen = cookieListen;
    }

    public String getCookieSpeak() {
        return cookieSpeak;
    }

    public void setCookieSpeak(String cookieSpeak) {
        this.cookieSpeak = cookieSpeak;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
