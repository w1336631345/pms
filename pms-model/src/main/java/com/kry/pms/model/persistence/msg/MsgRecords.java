package com.kry.pms.model.persistence.msg;

import com.kry.pms.model.persistence.PersistenceModel;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "t_msg_records")
public class MsgRecords extends PersistenceModel {

    private String custName;
    private String mobile;
    private String content;
    private Integer total;
    private LocalDateTime sendTime;
    private String result;
}
