package com.kry.pms.model.http.response.busi;

import com.kry.pms.model.http.response.marketing.CustomerVo;
import com.kry.pms.model.persistence.busi.CheckInRecord;
import com.kry.pms.model.persistence.sys.Account;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class CheckInRecordVo {
    private String id;
    private String name;
    private String orderNum;
    private String groupType;
    private LocalDateTime arriveTime;
    private LocalDateTime leaveTime;
    private String protocolCorpationName;
    private String protocolCorpationId;
    private Double purchasePrice;
    private Double personalPrice;
    private CustomerVo customer;
    private String accountId;
    private AccountSummaryVo accountSummaryVo;
    private Double total;

    private String remark;

   public CheckInRecordVo() {

    }

    public static CheckInRecordVo convert(CheckInRecord cir) {
        if (cir == null) {
            return null;
        }
        CheckInRecordVo cirv = new CheckInRecordVo();
        BeanUtils.copyProperties(cir, cirv);
        cirv.setAccountId(cir.getAccount().getId());
        if (cir.getCustomer() != null) {
            cirv.setCustomer(CustomerVo.convert(cir.getCustomer()));
        }
		if (cir.getCorp() != null) {
			cirv.setProtocolCorpationId(cir.getCorp().getId());
			cirv.setProtocolCorpationName(cir.getCorp().getName());
		}
        if (cir.getAccount() != null) {
            cirv.setAccountSummaryVo(new AccountSummaryVo(cir.getAccount()));
        }
        if (cir.getCorp() != null) {
            cirv.setProtocolCorpationId(cir.getCorp().getId());
            cirv.setProtocolCorpationName(cir.getCorp().getName());
        }
        if (cir.getActualTimeOfLeave() != null) {
            cirv.setLeaveTime(cir.getActualTimeOfLeave());
        }
        if (cir.getActualTimeOfArrive() != null) {
            cirv.setArriveTime(cir.getActualTimeOfArrive());
        }
        if (cir.getRemark() != null) {
            cirv.setRemark(cir.getRemark());
        }
        return cirv;

    }

    public static List<CheckInRecordVo> convert(List<CheckInRecord> currentCheckInRecords) {
        if (currentCheckInRecords == null || currentCheckInRecords.isEmpty()) {
            return null;
        }
        List<CheckInRecordVo> data = new ArrayList<CheckInRecordVo>();
        for (CheckInRecord checkInRecord : currentCheckInRecords) {
            data.add(convert(checkInRecord));
        }
        return data;
    }
}
