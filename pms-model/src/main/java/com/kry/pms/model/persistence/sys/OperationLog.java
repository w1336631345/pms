package com.kry.pms.model.persistence.sys;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
@Getter
@Setter
@Entity
@Table(name = "t_operation_log")
public class OperationLog {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(columnDefinition = "varchar(64)")
    protected String id;
    private String opType;
    private String opName;
    private String oldValue;
    private String newValue;
    private LocalDateTime opDate;
    private String opUser;
}
