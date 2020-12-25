package com.kry.pms.model.persistence.msg;

import com.kry.pms.model.persistence.PersistenceModel;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "t_msg_account")
public class MsgAccount extends PersistenceModel {

    private String organId;//机构id
    private String username;//用户名（短信用到的用户名是：organId:username）
    private String password;//密码
    private String content;//短信内容
}
