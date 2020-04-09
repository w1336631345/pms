package com.kry.pms.model.persistence.guest;

import com.kry.pms.model.persistence.PersistenceModel;
import com.kry.pms.model.persistence.marketing.RoomPriceScheme;
import com.kry.pms.model.persistence.marketing.SalesMen;
import com.kry.pms.model.persistence.sys.Account;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * 会员
 */
@Data
@Entity
@Table(name = "t_member_type")
public class MemberType extends PersistenceModel {

    @Column(columnDefinition = "varchar(100) COMMENT '代码'")
    private String code_;
    @Column(columnDefinition = "varchar(100) COMMENT '序号'")
    private String nums;

    @Column
    private String cardNum;//会员卡号
    @Column
    private String macNum;//物理卡号
    @Column(columnDefinition = "varchar(200) COMMENT '名称'")
    private String name;
    @Column(columnDefinition = "varchar(100) COMMENT '会员类型'")
    private String type;
    @Column(columnDefinition = "varchar(100) COMMENT '会员状态是否启用'")
    private String isUsed;
    @Column
    private String level;//会员等级
    @ManyToOne
    private SalesMen salesMen;//销售员
    @ManyToOne
    private RoomPriceScheme roomPriceScheme;//房价方案
    @Column
    private LocalDate effectiveDate;//生效日期
    @Column
    private LocalDate limitationDate;//失效日期
    @Column
    private String documentType;//证件类型
    @Column
    private String documentNum;//证件号码
    @Column
    private String county;//国家
    @Column
    private String gender;//性别
    @Column
    private String nativePlace;//籍贯
    @Column
    private String city;//城市
    @Column
    private LocalDate birthday;//生日
    @Column
    private String email;//邮箱
    @Column
    private String mobile;//电话
    @Column
    private String address;//详细地址
    @OneToOne
    private Account account;//会员账号
    @Column
    private String roomLike;//客房喜好
    @Column
    private String foodLike;//餐饮喜好
    @Column(columnDefinition = "varchar(1000) COMMENT '描述'")
    private String remark;

    @Column
    private Double roomPay;//客房消费
    @Column
    private Double foodPay;//餐饮消费
    @Column
    private Double otherPay;//其它消费
    @Column
    private Double totalPay;//总消费
    @Column
    private Integer integral;//积分
    @Column
    private Integer hotelNum;//住店次数
    @Column
    private Integer hotelDays;//住店天数

}
