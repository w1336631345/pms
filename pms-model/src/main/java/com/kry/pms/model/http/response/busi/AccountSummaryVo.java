package com.kry.pms.model.http.response.busi;

import java.time.LocalDateTime;
import java.util.Collection;

import com.kry.pms.base.Constants;
import com.kry.pms.model.persistence.busi.CheckInRecord;
import com.kry.pms.model.persistence.sys.Account;

import lombok.Data;

@Data
public class AccountSummaryVo {
    private String orderNum;
    private String id;
    private String name;
    private Double total;
    private String roomNum;
    private String type;
    private Double pay;
    private Double cost;
    private Double creditLimit;
    private Double availableCreditLimit;
    private String settleType;
    private String roomStatus;
    private String accountCode;
    private LocalDateTime arriveTime;
    private LocalDateTime leaveTime;
    private String guestRoomId;
    private Collection<AccountSummaryVo> children;

    public AccountSummaryVo() {

    }

    public AccountSummaryVo(String orderNum, String id, String name, Double total, String roomNum, String type, Double pay, Double cost, Double creditLimit, Double availableCreditLimit, String roomStatus, String accountCode, LocalDateTime arriveTime, LocalDateTime leaveTime, String guestRoomId) {
        this.orderNum = orderNum;
        this.id = id;
        this.name = name;
        this.total = total;
        this.roomNum = roomNum;
        this.type = type;
        this.pay = pay;
        this.cost = cost;
        this.creditLimit = creditLimit;
        this.availableCreditLimit = availableCreditLimit;
        this.roomStatus = roomStatus;
        this.accountCode = accountCode;
        this.arriveTime = arriveTime;
        this.leaveTime = leaveTime;
        this.guestRoomId = guestRoomId;
    }

    public AccountSummaryVo(Account acc) {
        this(acc, null);
    }

    public AccountSummaryVo(Account acc, String roomStatus) {
        this.id = acc.getId();
        this.name = acc.getName();
        this.total = acc.getTotal();
        this.roomNum = acc.getRoomNum();
        if (acc.getType().equals(Constants.Type.ACCOUNT_CUSTOMER)) {
            this.settleType = Constants.Type.SETTLE_TYPE_ACCOUNT;
        }
        this.accountCode = acc.getCode();
        this.type = acc.getType();
        this.cost = acc.getCost();
        this.pay = acc.getPay();
        this.creditLimit = acc.getCreditLimit();
        this.availableCreditLimit = acc.getAvailableCreditLimit();
        this.roomStatus = roomStatus;
    }

    public AccountSummaryVo(CheckInRecord cir) {
        this(cir.getAccount(), null);
        this.roomStatus = cir.getStatus();
        this.orderNum = cir.getOrderNum();
        this.arriveTime = cir.getActualTimeOfArrive() != null ? cir.getActualTimeOfArrive() : cir.getArriveTime();
        this.leaveTime = cir.getActualTimeOfLeave() != null ? cir.getActualTimeOfLeave() : cir.getLeaveTime();
    }
}
