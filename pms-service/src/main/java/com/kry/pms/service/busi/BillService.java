package com.kry.pms.service.busi;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.kry.pms.base.DtoResponse;
import com.kry.pms.model.http.request.busi.BillOperationBo;
import com.kry.pms.model.http.request.busi.BillQueryBo;
import com.kry.pms.model.http.request.busi.BillSettleBo;
import com.kry.pms.model.persistence.busi.Bill;
import com.kry.pms.model.persistence.busi.CheckInRecord;
import com.kry.pms.model.persistence.busi.RoomRecord;
import com.kry.pms.model.persistence.org.Employee;
import com.kry.pms.model.persistence.sys.Account;
import com.kry.pms.service.BaseService;

public interface BillService extends BaseService<Bill> {

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
	
	public List<Bill> findByIds(List<String> ids);

	//夜审手动入账
	void putAcount(List<RoomRecord> ids, LocalDate businessDate, Employee emp, String shiftCode);
	//夜审自动入账
	void putAcountAUTO(List<RoomRecord> ids, LocalDate businessDate);

	public List<Bill> checkAccountAllBill(Account account,double total,DtoResponse<Account> rep, String orderNum);

	public List<Bill> checkBillIds(List<String> billIds, double total, DtoResponse<Account> rep, String recordNum);

	public List<Bill> addFlatBills(List<Bill> bills, Employee employee,String shiftCode, String recordNum);

	public DtoResponse<Bill> offset(String id);

	/**
	 * 调整，入一笔帐，val为金额，其他信息与id对应的数据一致
	 * @param id
	 * @param val 如果为null 表示直接冲掉，其他未调整部分
	 * @return
	 */
	DtoResponse<Bill> adjust(String id, Double val,boolean shiftCheck,String shiftCode);
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

	public List queryByBo(BillQueryBo query);

}