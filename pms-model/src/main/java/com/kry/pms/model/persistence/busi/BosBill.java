package com.kry.pms.model.persistence.busi;

import com.kry.pms.model.persistence.PersistenceModel;
import com.kry.pms.model.persistence.goods.Product;
import com.kry.pms.model.persistence.org.Employee;
import com.kry.pms.model.persistence.room.GuestRoom;
import com.kry.pms.model.persistence.sys.Account;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "t_bos_bill")
public class BosBill extends PersistenceModel {
	@Column
	private String shiftCode;
	@OneToOne
	private Account account;
	@Column
	private Double total;
	@OneToOne
	private Account targetAccount;
	@Column
	private Integer quantity;
	@OneToOne
	private GuestRoom guestRoom;
	@Column
	private String showName;
	@Column
	private String roomNum;
	@Column
	private String roomId;
	@OneToOne
	private Product product;
	@Column
	private String type;
	@Column
	private Double cost;
	@Column
	private Double pay;
	@OneToOne
	private Employee operationEmployee;
	@Column(columnDefinition = "varchar(255) COMMENT '操作员备注'")
	private String operationRemark;
	@Column
	private Integer currentItemSeq;
	@Column
	private String paymentStatus;
	@Column
	private String tranferRemark;
	@Column
	private Integer billSeq;
	@Column
	private String settlementStatus;
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "bos_bill_id")
	private List<BosBillItem> items;
	@Column
	private LocalDate businessDate;
	@Column
	private String receiptNum;//原始凭据号
	@Column
	private String remark;
	@Column
	private String transferFlag;
	@Column
	private String transferType;
	@Column
	private String sid;
	@Column
	private String tid;
	@Column
	private String itemType;
	@ManyToOne
	private BosBill sourceBosBill;
	@Column
	private String currentSettleAccountRecordNum;//当前结账单号
	@Column
	private String roomRecordId;
	@Column
	private String feeFlag;
	@Column
	private String transactionId;
	@Column
	private String paymentPlatform;
	@Column
	private String packageNum;
	@Column
	private String serialNumber;//流水单号

	public static BosBill billToBosBill(Bill bill){
		BosBill bosBill = new BosBill();
		bosBill.setShiftCode(bill.getShiftCode());
		bosBill.setAccount(bill.getAccount());
		bosBill.setTotal(bill.getTotal());
		bosBill.setTargetAccount(bill.getTargetAccount());
		bosBill.setQuantity(bill.getQuantity());
		bosBill.setGuestRoom(bill.getGuestRoom());
		bosBill.setShowName(bill.getShowName());
		bosBill.setRoomNum(bill.getRoomNum());
		bosBill.setRoomId(bill.getRoomId());
		bosBill.setProduct(bill.getProduct());
		bosBill.setType(bill.getType());
		bosBill.setCost(bill.getCost());
		bosBill.setPay(bill.getPay());
		bosBill.setOperationEmployee(bill.getOperationEmployee());
		bosBill.setOperationRemark(bill.getOperationRemark());
		bosBill.setCurrentItemSeq(bill.getCurrentItemSeq());
		bosBill.setPaymentStatus(bill.getPaymentStatus());
		bosBill.setTranferRemark(bill.getTranferRemark());
		bosBill.setBillSeq(bill.getBillSeq());
		bosBill.setSettlementStatus(bill.getSettlementStatus());
		List<BosBillItem> items = new ArrayList<>();
		for(int i=0; i<bill.getItems().size(); i++){
			BosBillItem bosBillItem = new BosBillItem();
			BillItem billItem = bill.getItems().get(i);
			bosBillItem.setType(billItem.getType());
			bosBillItem.setRoomRecord(billItem.getRoomRecord());
			bosBillItem.setBillDate(billItem.getBillDate());
			bosBillItem.setGoodsCode(billItem.getGoodsCode());
			bosBillItem.setGoodsName(billItem.getGoodsName());
			bosBillItem.setBosGoodsInfoId(billItem.getBosGoodsInfoId());
			bosBillItem.setDiscountScheme(billItem.getDiscountScheme());
			bosBillItem.setTotal(billItem.getTotal());
			bosBillItem.setQuantity(billItem.getQuantity());
			bosBillItem.setPrice(billItem.getPrice());
			bosBillItem.setItemSeq(billItem.getItemSeq());
			bosBillItem.setStatusPayment(billItem.getStatusPayment());
			items.add(bosBillItem);
		}
		bosBill.setItems(items);
		bosBill.setBusinessDate(bill.getBusinessDate());
		bosBill.setReceiptNum(bill.getReceiptNum());
		bosBill.setRemark(bill.getRemark());
		bosBill.setTransferFlag(bill.getTransferFlag());
		bosBill.setTransferType(bill.getTransferType());
		bosBill.setSid(bill.getSid());
		bosBill.setTid(bill.getTid());
		bosBill.setItemType(bill.getItemType());
//		bosBill.setSourceBosBill(null);
		bosBill.setCurrentSettleAccountRecordNum(bill.getCurrentSettleAccountRecordNum());
		bosBill.setRoomRecordId(bill.getRoomRecordId());
		bosBill.setFeeFlag(bill.getFeeFlag());
		bosBill.setTransactionId(bill.getTransactionId());
		bosBill.setPaymentPlatform(bill.getPaymentPlatform());
		bosBill.setPackageNum(bill.getPackageNum());
//		bosBill.setSerialNumber(null);
		return bosBill;
	}

}
