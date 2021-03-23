package com.kry.pms.service.busi;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.kry.pms.base.DtoResponse;
import com.kry.pms.model.dto.BillStatistics;
import com.kry.pms.model.http.request.busi.BillOperationBo;
import com.kry.pms.model.http.request.busi.BillSettleBo;
import com.kry.pms.model.persistence.busi.Bill;
import com.kry.pms.model.persistence.busi.CheckInRecord;
import com.kry.pms.model.persistence.busi.RoomRecord;
import com.kry.pms.model.persistence.org.Employee;
import com.kry.pms.model.persistence.sys.Account;
import com.kry.pms.service.BaseService;

public interface BillService extends BaseService<Bill> {

    List<Bill> addAll(List<Bill> bills);

    public boolean billEntry(RoomRecord rr, LocalDate recordDate);

	/**
	 * 检查并支付账单
	 * @param crs
	 * @param total
	 * @return
	 */
	public boolean checkAndPayBill(List<CheckInRecord> crs, double total);

	public boolean checkAndPayBill(BillSettleBo bsb);

	public List<Bill> findByAccountId(String id);
	public List<Bill> findByAccountAndStatus(Account account,String status);
	
	public List<Bill> findByIds(List<String> ids);

	//夜审手动入账
	void putAcount(List<RoomRecord> ids, LocalDate businessDate, Employee emp, String shiftCode);

    // 夜审手动入账(优化)
    void putAcountMap(List<Map<String, Object>> list, LocalDate businessDate, Employee emp, String shiftCode, String hotelCode);

    //夜审自动入账
	void putAcountAUTO(List<RoomRecord> ids, LocalDate businessDate);

	Bill addFlatBill(Bill bill, Employee employee, String shiftCode, String orderNum);

	List<Bill> addToArFlatBill(Bill bill, Employee employee, String shiftCode, String orderNum);

	public List<Bill> checkAccountAllBill(Account account, double total, DtoResponse<Account> rep, String orderNum);

	public List<Bill> checkBillIds(List<String> billIds, double total, DtoResponse<Account> rep, String recordNum);

    @org.springframework.transaction.annotation.Transactional
    List<Bill> checkBills(List<Bill> bills, double total, DtoResponse<Account> rep, String recordNum);

    public List<Bill> addFlatBills(List<Bill> bills, Employee employee, String shiftCode, String recordNum);
	public List<Bill> addToMemberFlatBill(Bill bill, Employee employee, String shiftCode, String orderNum);
	public DtoResponse<Bill> offset(String id,Employee employee,String shiftCode);
	/**
	 * 拆账   冲掉原来的帐，重新入两笔一样的 金额分别是val1，val2
	 * @param id
	 * @param val1
	 * @param val2
	 * @return
	 */
	DtoResponse<Bill> split(String id, Double val1,Double val2);
	
	

	public DtoResponse<Bill> operation(BillOperationBo bob);

	DtoResponse<List<Bill>> transfer(String bid, Account targetAccount, String shiftCode, Employee employee, String recordNum);

	public List<Bill> checkBillIds(List<String> billIds, DtoResponse<Account> rep, String recordNum);

	public DtoResponse<List<Bill>> transfer(Bill bill, Account targetAccount, String shiftCode,
			Employee operationEmployee, String recordNum);

//	public List queryByBo(BillQueryBo query);

    int countUnSettleBill(String accountId);

    int countUnSellteNotZeroBill(String accountId);

	BillStatistics sumNeedSettle(Account account);

	Bill createArSettleBill(Account targetAccount, double total, double cost, double pay, Employee operationEmployee, String shiftCode,String recordNum);

	Bill createToArBill(Account account, double processTotal, double pay, Employee operationEmployee, String shiftCode,String recordNum,String remark);

    List<Map<String, Object>> getStatusTotal(String hotelCode, String accountId);

	int autoSettleZeroBill(String id);
}