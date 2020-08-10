package com.kry.pms.service.sys;

import java.util.Collection;
import java.util.List;

import com.kry.pms.base.DtoResponse;
import com.kry.pms.model.http.request.busi.BillCheckBo;
import com.kry.pms.model.http.response.busi.AccountSummaryVo;
import com.kry.pms.model.http.response.busi.SettleInfoVo;
import com.kry.pms.model.persistence.busi.Bill;
import com.kry.pms.model.persistence.busi.CheckInRecord;
import com.kry.pms.model.persistence.busi.RoomRecord;
import com.kry.pms.model.persistence.guest.Customer;
import com.kry.pms.model.persistence.org.Employee;
import com.kry.pms.model.persistence.sys.Account;
import com.kry.pms.service.BaseService;

public interface AccountService extends BaseService<Account> {
    void deleteTrue(String id);

    /**
     * 如客房帐
     *
     * @param rr
     * @return
     */
    Account billEntry(RoomRecord rr);
    public Bill enterExtFee(CheckInRecord cir, String type);
    public boolean enterExtFee(List<CheckInRecord> cirs, String type);
    Account billEntry(Bill bill);

    Collection<AccountSummaryVo> getAccountSummaryByOrderNum(String orderNum, String checkInType);

    Collection<Account> getAccountByOrderNumAndStatusAndCheckInType(String orderNum, String checkInType, String status);

    DtoResponse<Account> checkCustomerBill(BillCheckBo billCheckBo);

    Account createAccount(Customer customer, String roomNum);

    Account createMemberAccount(Customer customer, String hotelCode);

    DtoResponse<Double> queryRoomPrice(String id);

    /**
     * 帐务是否为空
     * @param id
     * @return
     */
    boolean accountCheck(String id);

    List<Account> findByHotelCodeAndType(String hotelCode);

    DtoResponse<List<Bill>> transferBill(List<Bill> bills, Double total, Account account, Account targetAccount,
                                         String shiftCode, Employee operationEmployee, String recordNum);

    DtoResponse<List<Bill>> cancleTransfer(List<Bill> flatBills, Double total, Account account, Account targetAccount, String shiftCode, Employee operationEmployee, String cancleNum);

    boolean accountCheckAndSettledZeroBill(Account account);

    SettleInfoVo getSettleInfo(String type, String id, String extFee, String hotelCode, String orderNum);
}