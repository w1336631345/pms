package com.kry.pms.model.persistence.guest;

import com.kry.pms.model.persistence.PersistenceModelTo;
import com.kry.pms.model.persistence.marketing.RoomPriceScheme;
import com.kry.pms.model.persistence.marketing.SalesMen;
import com.kry.pms.model.persistence.org.Hotel;
import com.kry.pms.model.persistence.sys.Account;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * 会员扣费模式
 */
@Data
@Entity
@Table(name = "t_member_deduction_type")
public class MemberDeductionType extends PersistenceModelTo {

    @Column(columnDefinition = "varchar(100) COMMENT '代码'")
    private String code;
    @Column(columnDefinition = "varchar(1000) COMMENT '描述1'")
    private String remark1;
    @Column(columnDefinition = "varchar(1000) COMMENT '描述2'")
    private String remark2;
    @Column
    private Double proportion;
    @Column
    private String proportionStr;
    @Column
    private String groupCode;

}
