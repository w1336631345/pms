package com.kry.pms.model.persistence.log;

import com.kry.pms.model.persistence.PersistenceModel;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.transaction.Transactional;

@Data
@Entity
@Table(name = "t_log_interface_use")
public class InterfaceUseLog extends PersistenceModel {

    private String url;

    private String classPath;

    private String methedName;

    private String methedType;

    private String remark;

    private String params;

    private String resultCode;

    private String resultMsg;

}
