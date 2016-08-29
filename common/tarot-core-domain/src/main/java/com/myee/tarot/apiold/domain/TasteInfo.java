package com.myee.tarot.apiold.domain;

import com.myee.tarot.core.GenericEntity;

import javax.persistence.*;

/**
 * Info: clever
 * User: Gary.zhang@clever-m.com
 * Date: 1/20/16
 * Time: 14:17
 * Version: 1.0
 * History: <p>如果有修改过程，请记录</P>
 */
@Entity
@javax.persistence.Table(name = "CA_TASTE")
public class TasteInfo extends GenericEntity<Long, MenuInfo> {

    @Id
    @Column(name = "ID", unique = true, nullable = true)
    @GeneratedValue(strategy = GenerationType.AUTO)//AUTO：主键由程序控制。IDENTITY：主键由数据库自动生成（主要是自动增长型） ； SEQUENCE：根据底层数据库的序列来生成主键，条件是数据库支持序列（类似oracle）；TABLE：使用一个特定的数据库表格来保存主键。
    private Long id;

    @Column(name = "TASTE_ID",length = 15)
    private String tasteId;     //口味编码

    @Column(name = "NAME",length = 200)
    private String name;       //名称

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTasteId() {
        return tasteId;
    }

    public void setTasteId(String tasteId) {
        this.tasteId = tasteId;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
}
