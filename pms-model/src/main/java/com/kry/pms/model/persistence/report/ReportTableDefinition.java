package com.kry.pms.model.persistence.report;

import com.kry.pms.model.persistence.PersistenceModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Data
@Table(name = "t_report_table_definition")
public class ReportTableDefinition extends PersistenceModel {
    @Column
    private String code;//编码
    @Column
    private String name;//名称
    @Column(name = "type_")
    private String type;//类型
    @Column
    private String groupKey;//分组
    @Column
    private String headerType;//头部类型
    @Column
    private String headerValue;//表头数据
    @Column
    private String dataType;//数据类型
    @Column
    private String dataValue;//数据值
    @Column(columnDefinition = "varchar(6000)")
    private String webTemplateType;
    @Column
    private String webTemplate;
    @Column
    private String exportTemplete;
    @Column
    private String exportTempleteType;
    @Column(columnDefinition = "varchar(2000)")
    private String baseTempleteType;
    @Column
    private String baseTemplete;
    @ManyToMany
    private List<ReportRowDefinition> rows;
    @Column
    private String params;//参数 &
}
