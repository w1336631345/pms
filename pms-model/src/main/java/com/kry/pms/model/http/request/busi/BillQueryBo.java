package com.kry.pms.model.http.request.busi;

import lombok.Data;

@Data
public class BillQueryBo {
	private String hotelCode;
	private String billDateType;// T Y H 今日 昨日 历史
	private String key;
	private String shift;// 班次
	private String operationEmployeeId;// 操作员id
	private String type;// billType
	private String productId;// 项目
	private String businessDateStart;// 营业日开始
	private String businessDateEnd;// 营业日结束
	private String roomId;// 房间id
	private String accountName;// 账户名
	private String accountNum;// 账号
	private String settleNum;// 结帐单号
	private Double min;// 最小金额
	private Double max;// 最大金额
}
