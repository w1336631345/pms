package com.kry.pms.model.persistence.sys;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

/**
 * 功能描述: <br>国籍表
 * @Author: huanghaibin
 * @Date: 2020/1/19 15:36
 */
@Data
@Entity
@Table(name = "t_nationality")
public class Nationality {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(columnDefinition = "varchar(64)")
    protected String id;
    @Column
    private String name;
    @Column
    private String englishName;
    @Column
    private String code_;
    @OneToMany
    private List<Nation> nationList;


}
