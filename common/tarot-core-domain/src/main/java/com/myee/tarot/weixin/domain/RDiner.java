package com.myee.tarot.weixin.domain;

//import com.myee.djinn.dto.Diner;
import com.myee.tarot.core.GenericEntity;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Martin on 2016/1/18.
 */
@Entity
@Table(name = "CA_DINER")
public class RDiner extends GenericEntity<Long, RDiner> {
    @Id
    @Column(name = "CA_DINER_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", pkColumnValue = "CA_DINER_SEQ_NEXT_VAL", valueColumnName = "SEQ_COUNT", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    private Long   id;

    @Column(name = "MOBILE")
    private String mobile;

    @Column(name = "WEIXIN")
    private String weixin;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "NICK_NAME")
    private String nickName;

    @Column(name = "TIME_FIRST_REPAST")
    private Date   timeFirstRepast;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Date getTimeFirstRepast() {
        return timeFirstRepast;
    }

    public void setTimeFirstRepast(Date timeFirstRepast) {
        this.timeFirstRepast = timeFirstRepast;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    /*public Diner toDto() {
        Diner diner = new Diner();
        diner.setDinerId(id);
        diner.setFirstName(firstName);
        diner.setLastName(lastName);
        diner.setNickName(nickName);
        diner.setWeixin(weixin);
        diner.setMobile(mobile);
        return diner;
    }*/
}
