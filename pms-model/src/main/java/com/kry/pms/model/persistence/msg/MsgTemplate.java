package com.kry.pms.model.persistence.msg;

import com.kry.pms.model.persistence.PersistenceModel;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "t_msg_template")
public class MsgTemplate extends PersistenceModel {

    private String msgType;
    private String typeCode;
    private String content;
    private String remark;
    private LocalDateTime sendTime;
    private String sentType;//发送方式：A自动发送，M手动发送

}
