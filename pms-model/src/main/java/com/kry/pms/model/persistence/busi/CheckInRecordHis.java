package com.kry.pms.model.persistence.busi;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kry.pms.model.annotation.Log;
import com.kry.pms.model.annotation.PropertyMsg;
import com.kry.pms.model.persistence.PersistenceModel;
import com.kry.pms.model.persistence.goods.SetMeal;
import com.kry.pms.model.persistence.guest.Customer;
import com.kry.pms.model.persistence.marketing.*;
import com.kry.pms.model.persistence.org.Employee;
import com.kry.pms.model.persistence.room.GuestRoom;
import com.kry.pms.model.persistence.room.RoomType;
import com.kry.pms.model.persistence.sys.Account;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "t_checkin_record_his")
//@NamedEntityGraph(name = "CheckInRecord.roomPriceScheme", attributeNodes = {@NamedAttributeNode("roomPriceScheme")})
public class CheckInRecordHis extends PersistenceModel {
	@OneToOne(cascade = CascadeType.DETACH,fetch=FetchType.LAZY)
	private Customer customer;// 客户
	@ManyToOne
	@JoinColumn(name = "booking_record_id")
	private BookingRecord BookingRecord;// 预订记录id
	@OneToOne(fetch=FetchType.LAZY)
	private RoomPriceSchemeItem priceSchemeItem;// 房价方案
	@OneToOne(fetch=FetchType.LAZY)
	private GuestRoom guestRoom;// 房间
	@Column(name = "name_")
	private String name;// 姓名
	@OneToOne(fetch=FetchType.LAZY)
	@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
	private RoomPriceScheme roomPriceScheme;// 房价方案
	@OneToOne(fetch=FetchType.LAZY)
	private RoomType roomType;// 房间类型
	@Column
	private String orderType;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "main_record_id")
	private CheckInRecord mainRecord;// 主单id
	@ManyToOne
	private SetMeal setMeal;//包价，这个只有上面子单用到
	@Column
	private String orderNum;// 订单编号
	@Column
	private String orderNumOld;//原订单号
	@Column
	private Boolean linked;
	@Column
	private String linkNum;
	@Column
	private Double regularPrice;
	@Column
	private Double discount;// 打折
	@Column
	private Integer humanCount;// 总人数
	@Column
	private Integer adultCount;//成人数量
	@Column
	private Integer childCount;//小孩数量
	@Column
	private Integer roomCount;// 房间数
	@Column
	private Integer chrildrenCount;// 子单数
	@Column
	private LocalDateTime arriveTime;// 到达时间
	@Column
	@Log(operationName = "actual_time_of_arrive")
	private LocalDateTime actualTimeOfArrive;//实际到达时间
	@Column
	private LocalDateTime actualTimeOfLeave;//实际离开时间
	@Column
	private LocalDate startDate;// 开始时间
	@Column
	private Double originalPrice;//原价
	@Column
	private Double purchasePrice;// 成交价格
	@Column
	private Double personalPrice;// 个人承担价格
	@Column
	private Double personalPercentage;// 个人承担价格占比
	@Column
	private Integer checkInCount;// 已排房数
	@Column
	private Integer days;// 入住天数
	@Column
	private String holdTime;//保留时效
	@Column
	private Integer singleRoomCount;// 单房人数
	@Column
	private LocalDateTime leaveTime;// 离店时间
	@Column(columnDefinition = "varchar(64) default '0000' COMMENT '入住编号'")
	private String checkInSn;
	@Column(name = "type_")
	private String type;
	@Column
	private String groupType;// Y : 真是团队，N 散客
	@Column
	private String groupName;//团队名称
	@Column
	private String fitType;//散客类别 T：散客团， P 散客
	@ManyToOne
	private Group group;// 团队信息
	@OneToOne(fetch=FetchType.LAZY,cascade = CascadeType.PERSIST)
	private Account account;// 账号
	@Transient
	private String roomTypeId;// 房间类型id
	@Column
	private String contactName;// 预订人
	@Column
	private String contactMobile;// 预订人电话
	@OneToOne(fetch=FetchType.LAZY)
	private MarketingSources marketingSources;// 市场来源
	@ManyToOne
	private Customer corp;//协议单位
	@OneToOne(fetch=FetchType.LAZY)
	private Employee operationEmployee;// 操作人
	@OneToOne(fetch=FetchType.LAZY)
	private DistributionChannel distributionChannel;// 市场渠道
	@OneToOne(fetch=FetchType.LAZY)
	private SalesMen salesMen;
	@Column
	private String remark;
	@Transient
	private String mainRecordId;// 主单id
	@OneToOne(fetch=FetchType.LAZY)
	private DiscountScheme discountScheme;// 优惠方案
	// 预留记录id
	@Column
	private String reserveId;
	// 联房id
	@Column(columnDefinition = "varchar(64) COMMENT '联房id'")
	private String roomLinkId;
	// 联房主要房间，如果是主要房间记录M，主要是用于删除联房时，不能删除散客团原来的成员
	@Column(columnDefinition = "varchar(64) COMMENT '联房主要房间：M'")
	private String roomLinkIdM;
	// 同住
	@Column(columnDefinition = "varchar(64) COMMENT '同住编号'")
	private String togetherCode;
	@Column
	private Boolean isSecrecy;//房价是否保密
	@Column
	private String externalOrder;//外部订单号
	@Column
	private String vipCode;//主单vip
	@Transient
	private String isGOrU;//预订来至团队（G）还是多人（U）

	private LocalDateTime hisTime;//存入历史表时间

}
