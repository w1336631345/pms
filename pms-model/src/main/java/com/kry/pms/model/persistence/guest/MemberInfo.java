package com.kry.pms.model.persistence.guest;

import com.kry.pms.model.persistence.PersistenceModel;
import com.kry.pms.model.persistence.marketing.RoomPriceScheme;
import com.kry.pms.model.persistence.marketing.SalesMen;
import com.kry.pms.model.persistence.org.Hotel;
import com.kry.pms.model.persistence.sys.Account;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * 会员
 */
@Data
@Entity
@Table(name = "t_member_info")
public class MemberInfo extends PersistenceModel {

    @Column
    private String cardNum;//会员卡号
    @Column
    private String macNum;//物理卡号
    @Column(columnDefinition = "varchar(100) COMMENT '会员状态是否启用'")
    private String isUsed;
    @ManyToOne
    private Customer customer;//会员账号
    @Column(columnDefinition = "varchar(100) COMMENT '会员类型'")
    private String type;
    @Column
    private String level;//会员等级
    @Column(columnDefinition = "varchar(100) COMMENT '代码'")
    private String code_;
    @Column(columnDefinition = "varchar(100) COMMENT '序号'")
    private String nums;
    @Column(columnDefinition = "varchar(200) COMMENT '名称'")
    private String name;
    @ManyToOne
    private SalesMen salesMen;//销售员
    @ManyToOne
    private RoomPriceScheme roomPriceScheme;//房价方案
    @Column
    private LocalDate effectiveDate;//生效日期
    @Column
    private LocalDate limitationDate;//失效日期
    @Column(columnDefinition = "varchar(1000) COMMENT '描述'")
    private String remark;
    @Column
    private Integer integral;//积分
    @Column
    private String integralType;//积分模式
    @Column
    private String deductionType;//扣费模式
    @ManyToOne
    private Customer recommender;//推荐人
    @OneToOne
    private Account account;//储值账户
//    @OneToOne
//    private Account account;//会员账号（积分账号）
    @Column
    private Double balance;//账户余额
    @Column
    private Double givePrice;//赠送金额
    @ManyToOne
    private Hotel hotel;//发卡酒店
    @Column
    private String quota;//消费限额
    @Column
    private String password;//密码
    @Column
    private String isUsedPassword;//校验密码

}
