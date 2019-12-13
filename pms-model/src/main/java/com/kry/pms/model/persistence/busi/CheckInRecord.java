package com.kry.pms.model.persistence.busi;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kry.pms.model.persistence.PersistenceModel;
import com.kry.pms.model.persistence.guest.Customer;
import com.kry.pms.model.persistence.marketing.*;
import com.kry.pms.model.persistence.org.Employee;
import com.kry.pms.model.persistence.room.GuestRoom;
import com.kry.pms.model.persistence.room.RoomTag;
import com.kry.pms.model.persistence.room.RoomType;
import com.kry.pms.model.persistence.sys.Account;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "t_checkin_record")
public class CheckInRecord extends PersistenceModel {
	@OneToOne(cascade = CascadeType.DETACH)
	private Customer customer;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="booking_record_id")
	private BookingRecord BookingRecord;
	@OneToOne
	private RoomPriceSchemeItem priceSchemeItem;
	@OneToOne
	private GuestRoom guestRoom;
	@Column(name = "name_")
	private String name;
	@OneToOne
	private RoomPriceScheme roomPriceScheme;
	@OneToOne
	private RoomType roomType;
	@Column
	private String orderType;
	@ManyToOne
	@JoinColumn(name="main_record_id")
	private CheckInRecord mainRecord;
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="main_record_id")
	private List<CheckInRecord> subRecords;
	@Column
	private String orderNum;
	@Column
	private Boolean linked;
	@Column
	private String linkNum;
	@Column
	private Double regularPrice;
	@Column
	private Double discount;
	@Column
	private Integer humanCount;
	@Column
	private Integer roomCount;
	@ManyToMany(cascade = CascadeType.PERSIST)
	private List<RoomTag> demands;
	@Column
	private Integer chrildrenCount;
	@Column
	private LocalDateTime arriveTime;
	@Column
	private LocalDate startDate;
	@Column
	private Double purchasePrice;
	@Column
	private Integer checkInCount;
	@Column
	private Integer days;
	@Column
	private String holdTime;
	@Column
	private LocalDateTime leaveTime;
	@Column(columnDefinition = "varchar(64) default '0000' COMMENT '入住编号'")
	private String checkInSn;
	@Column(name = "type_")
	private String type;
	@Column
	private String groupType;
	@ManyToOne
	private Group group;
	@OneToOne(cascade = CascadeType.PERSIST)
	private Account account;
	@Transient
	private String roomTypeId;
	@Column
	private String contactName;
	@Column
	private String contactMobile;
	@OneToOne
	private MarketingSources marketingSources;
	@OneToOne
	private ProtocolCorpation protocolCorpation;
	@OneToOne
	private Employee operationEmployee;
	@OneToOne
	private DistributionChannel distributionChannel;
	@OneToOne
	private Employee marketEmployee;
	@Column
	private String remark;
	@Transient
	private String mainRecordId;
	@OneToOne
	private DiscountScheme discountScheme;
	
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
	public ProtocolCorpation getProtocolCorpation() {
		return protocolCorpation;
	}
	public void setProtocolCorpation(ProtocolCorpation protocolCorpation) {
		this.protocolCorpation = protocolCorpation;
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
	public Employee getMarketEmployee() {
		return marketEmployee;
	}
	public void setMarketEmployee(Employee marketEmployee) {
		this.marketEmployee = marketEmployee;
	}
	public RoomPriceScheme getRoomPriceScheme() {
		return roomPriceScheme;
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
	public List<RoomTag> getDemands() {
		return demands;
	}
	public void setDemands(List<RoomTag> demands) {
		this.demands = demands;
	}
	
}
