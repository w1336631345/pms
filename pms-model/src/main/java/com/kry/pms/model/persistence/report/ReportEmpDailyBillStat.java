package com.kry.pms.model.persistence.report;

import lombok.Data;
import org.hibernate.annotations.GeneratorType;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
@Entity
@Table(name = "t_report_emp_daily_bill_stat")
public class ReportEmpDailyBillStat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column
    private String hotelCode;
    @Column
    private LocalDate quantityDate;
    @Column
    private LocalDateTime createTime;
    @Column
    private String employeeId;
    @Column
    private String shift;
    @Column
    private String code;
    @Column
    private String name;
    @Column
    private String keyOne;
    @Column
    private String keyTwo;
    @Column
    private String keyThree;
    @Column
    private String keyFour;
    @Column
    private String key;
    @Column(name = "value_")
    private String value;
}
