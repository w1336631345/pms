package com.kry.pms.model.persistence.sys;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kry.pms.model.persistence.PersistenceModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "t_manager_user")
public class ManagerUser extends PersistenceModel {
    @Column
    private String nickname;
    @Column
    private String username;
    @Column
    private String mobile;
    @Column
    private String openId;
    @Column
    private String unionId;//同一主体下的程序的唯一标识符（和openid比较）
    @Column
    @JsonIgnore
    private String password;
    @Column(name = "type_")
    private String type;
    @OneToOne
    private Role role;
    @Column
    private String avatar;
    @Column(columnDefinition = "varchar(32) default 'normal' COMMENT '是否允许登录（normal：正常可登录状态，audit：夜审不可登录）'")
    private String allowLogin;
}
