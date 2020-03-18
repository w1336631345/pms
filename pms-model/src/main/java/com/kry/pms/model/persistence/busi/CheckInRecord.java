package com.kry.pms.model.persistence.busi;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kry.pms.model.annotation.Log;
import com.kry.pms.model.persistence.PersistenceModel;
import com.kry.pms.model.persistence.goods.SetMeal;
import com.kry.pms.model.persistence.guest.Customer;
import com.kry.pms.model.persistence.marketing.*;
import com.kry.pms.model.persistence.org.Employee;
import com.kry.pms.model.persistence.room.GuestRoom;
import com.kry.pms.model.persistence.room.RoomTag;
import com.kry.pms.model.persistence.room.RoomType;
import com.kry.pms.model.persistence.sys.Account;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "t_checkin_record")
//@NamedEntityGraph(name = "CheckInRecord.roomPriceScheme", attributeNodes = {@NamedAttributeNode("roomPriceScheme")})
public class CheckInRecord extends PersistenceModel {
	@OneToOne(cascade = CascadeType.DETACH)
	private Customer customer;// 客户
	@ManyToOne
	@JoinColumn(name = "booking_record_id")
	private BookingRecord BookingRecord;// 预订记录id

	@OneToOne
	private RoomPriceSchemeItem priceSchemeItem;// 房价方案
	@OneToOne
	private GuestRoom guestRoom;// 房间
	@Column(name = "name_")
	private String name;// 姓名
	@OneToOne
	@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
	private RoomPriceScheme roomPriceScheme;// 房价方案
	@OneToOne
	private RoomType roomType;// 房间类型
	@Column
	private String orderType;
	@ManyToOne
	@JoinColumn(name = "main_record_id")
	private CheckInRecord mainRecord;// 主单id
	@Transient
	private List<CheckInRecord> subRecords;// 子单
	@ManyToOne
	private SetMeal setMeal;//包价，这个只有上面子单用到
	@Column
	private String orderNum;// 订单编号
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
	private String holdTime;
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
	@OneToOne(cascade = CascadeType.PERSIST)
	private Account account;// 账号
	@Transient
	private String roomTypeId;// 房间类型id
	@Column
	private String contactName;// 预订人
	@Column
	private String contactMobile;// 预订人电话
	@OneToOne
	private MarketingSources marketingSources;// 市场来源
//	@OneToOne
//	private ProtocolCorpation protocolCorpation;// 协议单位
	@ManyToOne
	private Customer corp;//协议单位
	@OneToOne
	private Employee operationEmployee;// 操作人
	@OneToOne
	private DistributionChannel distributionChannel;// 市场渠道
//	@OneToOne
//	private Employee marketEmployee;// 销售员
	@OneToOne
	private SalesMen salesMen;
	@Column
	private String remark;
	@Transient
	private String mainRecordId;// 主单id
	@OneToOne
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
	@Transient
	private Boolean isUpdateTime = false;// 用作判断主单是否修改了到店离店时间
//	@ManyToMany
//	private List<Arrangement> arrangements;//房间布置（特殊要求）
//	@ManyToMany(cascade = CascadeType.PERSIST)
//	private List<RoomTag> demands;// 房间标签(房间布置)
	@ElementCollection
	@CollectionTable(name="t_room_layout")
	private List<String> roomLayout;//客房布置
	@ElementCollection
	@CollectionTable(name="t_room_requirement")
	private List<String> roomRequirement;//客房要求
	@Column
	private Boolean isSecrecy;//房价是否保密

	@Transient
	private String isGOrU;//预订来至团队（G）还是多人（U）

	public String getRoomLinkIdM() { return roomLinkIdM; }
	public void setRoomLinkIdM(String roomLinkIdM) { this.roomLinkIdM = roomLinkIdM; }
	public Customer getCorp() { return corp; }
	public void setCorp(Customer corp) { this.corp = corp; }
	public List<String> getRoomRequirement() { return roomRequirement; }
	public void setRoomRequirement(List<String> roomRequirement) { this.roomRequirement = roomRequirement; }
	public List<String> getRoomLayout() { return roomLayout; }
	public void setRoomLayout(List<String> roomLayout) { this.roomLayout = roomLayout; }
	public String getGroupName() { return groupName; }
	public void setGroupName(String groupName) { this.groupName = groupName; }
	public Double getOriginalPrice() { return originalPrice; }
	public void setOriginalPrice(Double originalPrice) { this.originalPrice = originalPrice; }
	public String getFitType() { return fitType; }
	public void setFitType(String fitType) { this.fitType = fitType; }
	public String getIsGOrU() { return isGOrU; }
	public void setIsGOrU(String isGOrU) { this.isGOrU = isGOrU; }
	public Boolean getIsSecrecy() { return isSecrecy; }
	public void setIsSecrecy(Boolean isSecrecy) { this.isSecrecy = isSecrecy; }
	public LocalDateTime getActualTimeOfArrive() { return actualTimeOfArrive; }
	public void setActualTimeOfArrive(LocalDateTime actualTimeOfArrive) { this.actualTimeOfArrive = actualTimeOfArrive; }
	public LocalDateTime getActualTimeOfLeave() { return actualTimeOfLeave; }
	public void setActualTimeOfLeave(LocalDateTime actualTimeOfLeave) { this.actualTimeOfLeave = actualTimeOfLeave; }
	public Integer getAdultCount() { return adultCount; }
	public void setAdultCount(Integer adultCount) { this.adultCount = adultCount; }
	public Integer getChildCount() { return childCount; }
	public void setChildCount(Integer childCount) { this.childCount = childCount; }

	public Boolean getIsUpdateTime() {
		return isUpdateTime;
	}

	public void setIsUpdateTime(Boolean isUpdateTime) {
		this.isUpdateTime = isUpdateTime;
	}

	public String getReserveId() {
		return reserveId;
	}

	public void setReserveId(String reserveId) {
		this.reserveId = reserveId;
	}

	public String getRoomLinkId() {
		return roomLinkId;
	}

	public void setRoomLinkId(String roomLinkId) {
		this.roomLinkId = roomLinkId;
	}

	public Integer getSingleRoomCount() {
		return singleRoomCount;
	}

	public void setSingleRoomCount(Integer singleRoomCount) {
		this.singleRoomCount = singleRoomCount;
	}

	public DiscountScheme getDiscountScheme() {
		return discountScheme;
	}

	public void setDiscountScheme(DiscountScheme discountScheme) {
		this.discountScheme = discountScheme;
	}

	public Double getRegularPrice() {
		return regularPrice;
	}

	public void setRegularPrice(Double regularPrice) {
		this.regularPrice = regularPrice;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	public String getMainRecordId() {
		return mainRecordId;
	}

	public void setMainRecordId(String mainRecordId) {
		this.mainRecordId = mainRecordId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactMobile() {
		return contactMobile;
	}

	public void setContactMobile(String contactMobile) {
		this.contactMobile = contactMobile;
	}

	public MarketingSources getMarketingSources() {
		return marketingSources;
	}

	public void setMarketingSources(MarketingSources marketingSources) {
		this.marketingSources = marketingSources;
	}

	public Employee getOperationEmployee() {
		return operationEmployee;
	}

	public void setOperationEmployee(Employee operationEmployee) {
		this.operationEmployee = operationEmployee;
	}

	public DistributionChannel getDistributionChannel() {
		return distributionChannel;
	}

	public void setDistributionChannel(DistributionChannel distributionChannel) {
		this.distributionChannel = distributionChannel;
	}
	public RoomPriceScheme getRoomPriceScheme() {
		return roomPriceScheme;
	}

	public SalesMen getSalesMen() {
		return salesMen;
	}
	public void setSalesMen(SalesMen salesMen) {
		this.salesMen = salesMen;
	}
	public void setRoomPriceScheme(RoomPriceScheme roomPriceScheme) {
		this.roomPriceScheme = roomPriceScheme;
	}

	public String getRoomTypeId() {
		return roomTypeId;
	}

	public void setRoomTypeId(String roomTypeId) {
		this.roomTypeId = roomTypeId;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@JsonIgnore
	public BookingRecord getBookingRecord() {
		return BookingRecord;
	}

	public void setBookingRecord(BookingRecord bookingRecord) {
		BookingRecord = bookingRecord;
	}

	public RoomPriceSchemeItem getPriceSchemeItem() {
		return priceSchemeItem;
	}

	public void setPriceSchemeItem(RoomPriceSchemeItem priceSchemeItem) {
		this.priceSchemeItem = priceSchemeItem;
	}

	public GuestRoom getGuestRoom() {
		return guestRoom;
	}

	public void setGuestRoom(GuestRoom guestRoom) {
		this.guestRoom = guestRoom;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public RoomType getRoomType() {
		return roomType;
	}

	public void setRoomType(RoomType roomType) {
		this.roomType = roomType;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	@JsonIgnore
	public CheckInRecord getMainRecord() {
		return mainRecord;
	}

	@JsonIgnore
	public void setMainRecord(CheckInRecord mainRecord) {
		this.mainRecord = mainRecord;
	}

	public List<CheckInRecord> getSubRecords() {
		return subRecords;
	}

	public void setSubRecords(List<CheckInRecord> subRecords) {
		this.subRecords = subRecords;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public Integer getHumanCount() {
		return humanCount;
	}

	public void setHumanCount(Integer humanCount) {
		this.humanCount = humanCount;
	}

	public Integer getRoomCount() {
		return roomCount;
	}

	public void setRoomCount(Integer roomCount) {
		this.roomCount = roomCount;
	}

	public Integer getChrildrenCount() {
		return chrildrenCount;
	}

	public void setChrildrenCount(Integer chrildrenCount) {
		this.chrildrenCount = chrildrenCount;
	}

	public LocalDateTime getArriveTime() {
		return arriveTime;
	}

	public void setArriveTime(LocalDateTime arriveTime) {
		this.arriveTime = arriveTime;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public Double getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(Double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public Integer getCheckInCount() {
		return checkInCount;
	}

	public void setCheckInCount(Integer checkInCount) {
		this.checkInCount = checkInCount;
	}

	public Integer getDays() {
		return days;
	}

	public void setDays(Integer days) {
		this.days = days;
	}

	public LocalDateTime getLeaveTime() {
		return leaveTime;
	}

	public void setLeaveTime(LocalDateTime leaveTime) {
		this.leaveTime = leaveTime;
	}

	public String getCheckInSn() {
		return checkInSn;
	}

	public void setCheckInSn(String checkInSn) {
		this.checkInSn = checkInSn;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public String getHoldTime() {
		return holdTime;
	}

	public void setHoldTime(String holdTime) {
		this.holdTime = holdTime;
	}

	public String getTogetherCode() {
		return togetherCode;
	}

	public void setTogetherCode(String togetherCode) {
		this.togetherCode = togetherCode;
	}

	public Double getPersonalPrice() {
		return personalPrice;
	}

	public void setPersonalPrice(Double personalPrice) {
		this.personalPrice = personalPrice;
	}

	public Double getPersonalPercentage() {
		return personalPercentage;
	}

	public void setPersonalPercentage(Double personalPercentage) {
		this.personalPercentage = personalPercentage;
	}

	public Boolean getLinked() {
		return linked;
	}

	public void setLinked(Boolean linked) {
		this.linked = linked;
	}

	public String getLinkNum() {
		return linkNum;
	}

	public void setLinkNum(String linkNum) {
		this.linkNum = linkNum;
	}

	public SetMeal getSetMeal() { return setMeal; }

	public void setSetMeal(SetMeal setMeal) { this.setMeal = setMeal; }

}
