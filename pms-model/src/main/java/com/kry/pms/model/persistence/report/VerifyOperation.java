package com.kry.pms.model.persistence.report;

import com.kry.pms.model.persistence.PersistenceModel;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "t_verify_operation")
public class VerifyOperation extends PersistenceModel {
    private String name;
    private String type;
    private String requestUrl;
}
