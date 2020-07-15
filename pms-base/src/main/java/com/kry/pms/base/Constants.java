package com.kry.pms.base;

import java.time.LocalTime;

public class Constants {
	public static final String KEY_PAGE_SIZE = "pageSize";
	public static final String KEY_PAGE_NUM = "pageNum";
	public static final String KEY_ORDER = "order";
	public static final String KEY_SHORT_ASC = "asc";
	public static final String KEY_METHOD_SET_PRE = "set";
	public static final String KEY_METHOD_GET_PRE = "get";
	public static final String KEY_DEFAULT_SEPARATOR = ",";

	public interface Operation {
		public static final String BOOK_CANCEL = "cancel";
		public static final String BOOK_VERIFY_PASS = "verify_pass";
		public static final String BOOK_VERIFY_REFUSE = "verify_refuse";
	}

	public interface Status {
		public static final String DELETE = "DELETE";
		public static final String DRAFT = "DRAFT";
		public static final String NORMAL = "normal";
		public static final String DISABLE = "DISABLE";
		public static final String CLOSE = "CLOSE";
		public static final String BOOKING_VERIFY = "booking_verify";
		public static final String BOOKING_VERIFY_REFUSE = "booking_verify_refuse";
		public static final String BOOKING_SUCCESS = "booking_success";
		public static final String BOOKING_NO_SHOW = "no_show";
		public static final String BOOKING_CANCLE = "cancle";
		public static final String BOOKING_ALL_CHECK_IN = "all_check_in";
		public static final String BOOKING_PART_CHECK_IN = "part_check_in";
		public static final String BOOKING_PART_CHECK_OUT = "part_check_out";
		public static final String BOOKING_ALL_CHECK_OUT = "all_check_out";
		public static final String ROOM_STATUS_VACANT_CLEAN = "VC";
		public static final String ROOM_STATUS_VACANT_DIRTY = "VD";
		public static final String ROOM_STATUS_OCCUPY_CLEAN = "OC";
		public static final String ROOM_STATUS_OCCUPY_DIRTY = "OD";
		public static final String ROOM_STATUS_OUT_OF_ORDER = "OO";
		public static final String ROOM_STATUS_OUT_OF_SERVCIE = "OS";
		public static final String ROOM_STATUS_EXPECTE_DEPARTURE = "ED";
		public static final String ROOM_STATUS_FREE = "COM";
		public static final String ROOM_RECORD_STATUS_CHECK_IN = "check_in";
		public static final String ROOM_RECORD_STATUS_DAILY_VERIFY_PASS = "daily_verif_pass";
		public static final String ROOM_RECORD_STATUS_CHECK_OUT = "check_out";
		public static final String CHECKIN_RECORD_STATUS_RESERVATION = "R";// 预订
		public static final String CHECKIN_RECORD_STATUS_ASSIGN = "A";// 排房
		public static final String CHECKIN_RECORD_STATUS_CHECK_IN = "I";// 入住
		public static final String CHECKIN_RECORD_STATUS_CHECK_OUT = "O";// 退房
		public static final String CHECKIN_RECORD_STATUS_DEPARTURE_YESTERDAY = "D";// 历史订单
		public static final String CHECKIN_RECORD_STATUS_NO_SHOW = "N";// 未到
		public static final String CHECKIN_RECORD_STATUS_OUT_UNSETTLED = "S";// 退房未结账
		public static final String CHECKIN_RECORD_STATUS_CANCLE_BOOK = "X";// 取消
		public static final String BILL_SETTLED = "SETTLED";
		public static final String BILL_NEED_SETTLED = "NEED_SETTLED";
		public static final String BILL_CANCLE_SETTLED = "CANCLE_SETTLED";
		public static final String BILL_INVALID = "INVALID";
		public static final String BILL_TRANSFER = "TRANSFER";

		public static final String ROOM_USAGE_FREE = "F";  //空闲
		public static final String ROOM_USAGE_CHECK_IN = "I"; //入住
		public static final String ROOM_USAGE_CHECK_OUT = "O";//退房
		public static final String ROOM_USAGE_ASSIGN = "A";//分房
		public static final String ROOM_USAGE_LOCKED = "L";//锁定
		public static final String ROOM_USAGE_BOOK = "B";//预定
		public static final String ROOM_USAGE_RESERVATION = "R";//预留
		public static final String ROOM_USAGE_REPARIE = "P";//维修
		public static final String ROOM_USAGE_PREDICTABLE = "PR";//可预留
		public static final String ROOM_USAGE_AVAILABLE = "AVA";//可预留


		public static final String ACCOUNT_NEW ="N" ;
		public static final String ACCOUNT_IN = "I";
		public static final String ACCOUNT_HOLD = "H";
		public static final String ACCOUNT_SETTLE = "S";

		public static final String SETTLE_ACCOUNT_CANCLE = "C";

		public static final String SETTLEMENT_OK = "0";// 换房记录差价结清状态，结清
		public static final String SETTLEMENT_NO = "1";// 换房记录差价结清状态，未结清
	}

	public interface Key {
		public static final String BUSINESS_SEQ_KEY = "CHECKIN_SN";
		public static final String SESSION_ATTR_SHIFT_CODE = "SHIFT";
		public static final String SESSION_COMPUTER_IP = "COMPUTER_IP";
		public static final String SESSION_COMPUTER_MAC = "COMPUTER_MAC";
		public static final String BUSINESS_ORDER_NUM_SEQ_KEY = "ORDER_NUM";
		public static final String BUSINESS_BUSINESS_DATE_SEQ_KEY = "BUINESS_DATE";
		public static final String BUSINESS_PLAN_DATE_SEQ_KEY = "PLAN_DATE";
		public static final String BUSINESS_BUSINESS_SETTLE_SEQ_KEY = "SETTLE_NUM";
		public static final String BUSINESS_BUSINESS_TRANSFER_SEQ_KEY = "TRANSFER_NUM";
		public static final String BUSINESS_BUSINESS_CUSTOMER_ACCOUNT_SEQ_KEY = "CUSTOMER_ACCOUNT_NUM";
		public static final String BUSINESS_BUSINESS_GROUP_ACCOUNT_SEQ_KEY = "GROUP_ACCOUNT_NUM";//团队预订账号G开头
		public static final String BUSINESS_BUSINESS_GROUP_CUSTOMER_ACCOUNT_SEQ_KEY = "GROUP_CUSTOMER_ACCOUNT_NUM";//多人散客预订账号U开头
		public static final String BUSINESS_BUSINESS_ACCOUNT_SEQ_MEMBER = "ACCOUNT_SEQ_MEMBER";//会员账号
		public static final String BUSINESS_BUSINESS_ACCOUNT_SEQ="ACCOUNT_SEQ_";
		public static final String BUSINESS_BUSINESS_SALES_MEN_SEQ="SALES_MEN_SEQ";
		public static final String TOGETHER_NUM_KEY = "TOGETHER_NUM";
		public static final String CUSTOMER_NUM = "CUSTOMER_NUM";// 客户编码
		public static final String BUSINESS_CANCLE_SETTLE_SEQ_KEY = "CANCLE_SETTLE_NUM";
		public static final String EXECUTE_TYPE_AFTER_NIGHT_AUDT = "ANA";
    }

	public interface Type {


		public static final String REPORT_TABLE_DEF_TEMPLATE = "TEMPLATE";
		public static final String REPORT_TABLE_DEF_TABLE = "TABLE";
		public static final String REPORT_TABLE_DEF_PAGE = "PAGE";

		public static final String CONFIG_USER = "U";
		public static final String CONFIG_OPERATION = "O";
		public static final String CONFIG_SYSTEM = "S";

//		public static final String BILL_CHECK_TYPE_ALL = "ALL";
//		public static final String BILL_CHECK_TYPE_PART = "PART";

		public static final String BUSINESS_SEQ_PDS = "PDS";
		public static final String BUSINESS_SEQ_PS = "PS";
		public static final String BUSINESS_SEQ_DS = "DS";
		public static final String BUSINESS_SEQ_S = "S";

		public static final String PRODUCT_TYPE_COST = "cost";
		public static final String PRODUCT_TYPE_PAY = "pay";

		public static final String BILL_TYPE_COST = "cost";
		public static final String BILL_TYPE_PAY = "pay";
		public static final String BILL_TYPE_PACKAGE = "package";

		public static final String BOOK_GROUP = "GROUP";

		public static final String CHECK_IN_RECORD_GROUP_TYPE_NO = "N";
		public static final String CHECK_IN_RECORD_GROUP_TYPE_YES = "Y";

		public static final String CHECK_IN_RECORD_CUSTOMER = "C";
		public static final String CHECK_IN_RECORD_RESERVE = "R";
		public static final String CHECK_IN_RECORD_GROUP = "G";
		public static final String CHECK_IN_RECORD_LINK = "L";

		public static final String AUTO_DAILY_VERIFY = "auto_daily_verify";
		public static final String BILL_ITEM_ROOM_RECORD = "room";
		// AR 账户
		public static final String ACCOUNT_AR = "AR";
		// 客户账户
		public static final String ACCOUNT_CUSTOMER = "CUSTOMER";
		// 内部账户
		public static final String ACCOUNT_INNER = "INNER";
		// 团队账户
		public static final String ACCOUNT_GROUP = "GROUP";
		//多人散客主账户
		public static final String ACCOUNT_GROUP_CUSTOMER = "GROUP_CUSTOMER";
		// 会员账户
		public static final String ACCOUNT_MEMBER = "MEMBER";
		// 会员账户
		public static final String ACCOUNT_TEMP = "TEMP";
		// 自由定价
		public static final String PRODUCT_FREE_PRICE = "FREE_PRICE";
		// 定价商品
		public static final String PRODUCT_FIXED_PRICE = "FIXED_PRICE";
		// 实物商品
		public static final String PRODUCT_GOODS = "GOODS";
		// 服务（虚拟商品）
		public static final String PRODUCT_SERVICE = "SERVICE";

		public static final String CHANGE_ROOM_PAY_B = "B";// 换房记录差价处理方式，补差价
		public static final String CHANGE_ROOM_PAY_F = "F";// 换房记录差价处理方式，免费升级

		public static final String SETTLE_TYPE_GROUP = "GROUP";
		public static final String SETTLE_TYPE_ACCOUNT = "ACCOUNT";
		public static final String SETTLE_TYPE_PART = "PART";
		public static final String SETTLE_TYPE_LINK = "LINK";
		public static final String SETTLE_TYPE_IGROUP = "IG";
		public static final String SETTLE_TYPE_ROOM = "ROOM";
		public static final String SETTLE_TYPE_NONE = "NONE";
		
		public static final String BILL_CHECK_WAY_SETTLED = "S";
		public static final String BILL_CHECK_WAY_TRANSFER = "T";
		public static final String BILL_CHECK_WAY_SETTLED_AR= "TAR";

		public static final String CREDIT_GRANT_EMPLOYEE="E";

		public static final String EXT_FEE_NONE = "N";
		public static final String EXT_FEE_FULL = "F";
		public static final String EXT_FEE_HALF = "H";

		public static final String BILL_FLAG_EXT_ROOM_FEE = "EXT_ROOM_FEE";

		public static final String ROOM_LOCK_LOCK = "L";
		public static final String ROOM_LOCK_REPAIR = "P";
    }

	public static final int DELETED_TRUE = 1;
	public static final int DELETED_FALSE = 0;
	public static final int CODE_SHOW_LEVEL_ERROR = 2;
	public static final int CODE_SHOW_LEVEL_WARING = 1;
	public static final int CODE_SHOW_LEVEL_SUCCESS = 0;
	public static final String FLAG_YES = "Y";

	public interface ErrorCode {
		public static final int REQUIRED_PARAMETER_MISSING = 600;
		public static final int REQUIRED_PARAMETER_INVALID = 601;
	}

	public interface BusinessCode {
		public static final int CODE_RESOURCE_NOT_ENOUGH = 900;
		public static final int CODE_PARAMETER_INVALID = 901;
		public static final int CODE_ILLEGAL_OPERATION = 801;

		public static final String DEPT_MARKETING_DEFAULT_CODE = "MARKETING";
	}

	public interface SystemConfig {
		public static final String CODE_VARCANT_DIRTY_CHECK_IN_ABLE = "varcant_dirty_check_in_able";
		public static final String CODE_AUDIT_TIME = "audit_time";
		public static final String CODE_DEFAULT_ACCOUNT_PASSWORD = "default_account_password";
		public static final String CODE_CRITICAL_TIME = "critical_time";
		public static final LocalTime VALUE_DEFAULT_AUDIT_TIME = LocalTime.parse("01:00:00");
		public static final LocalTime VALUE_DEFAULT_CRITICAL_TIME = LocalTime.parse("06:00:00");
		public static final String CODE_CUSTOMER_GROUP_BOOKING_VERIFY = "customer_group_booking_verify";
		public static final String CODE_CUSTOMER_PERSONAL_BOOKING_VERIFY = "customer_personal_booking_verify";
	}

	public interface ReportSort {
		public static final String REPORT_ROOM_RATE = "0";// 营业日报表排序-房费账务统计
		public static final String REPORT_ROOM_CHECKIN_A = "1";// 营业日报表排序-a、客房总数统计
		public static final String REPORT_ROOM_CHECKIN_B = "2";// 营业日报表排序-房间入住（b、出租总数）统计
		public static final String REPORT_ROOM_CHECKIN_C = "3";// 营业日报表排序-房间入住（c、出售率）统计
		public static final String REPORT_ROOM_CHECKIN_D = "4";// 营业日报表排序-房间入住（d、房租收入）统计
		public static final String REPORT_ROOM_CHECKIN_E = "5";// 营业日报表排序-房间入住（e、平均房价）统计
		public static final String REPORT_ROOM_CHECKIN_F = "6";// 营业日报表排序-房间入住（f、人数统计）统计
		public static final String REPORT_ROOM_CHECKIN_G = "7";// 营业日报表排序-房间入住（g、客房其他收入）统计
	}

	public interface ReportType {
		public static final String REPORT_ROOM_RATE = "RATE";// 营业日报表项目类型-房费账务统计
		public static final String REPORT_ROOM_NUM = "NUM";// 营业日报表项目类型-房间数量统计
		public static final String REPORT_ROOM_CHECKIN = "CHECKIN";// 营业日报表项目类型-房间数量统计
	}

	public interface ReportProject {
		public static final String REPORT_ROOM_RATE = "客房房费";// 营业日报表项目-房费相关统计
		public static final String REPORT_ROOM_OTHER = "其他";// 营业日报表项目-房费其它统计
		public static final String REPORT_ROOM_TOTAL = "营业状况统计"; // 营业日报表项目-房费总和统计
		public static final String REPORT_ROOM_CHECKIN_A = "a、客房总数"; // 营业日报表项目-统计-a、客房总数
		public static final String REPORT_ROOM_CHECKIN_B = "b、出租总数"; // 营业日报表项目-统计-b、出租总数
		public static final String REPORT_ROOM_CHECKIN_C = "c、售卖率"; // 营业日报表项目-统计-c、售卖率
		public static final String REPORT_ROOM_CHECKIN_D = "d、房租收入"; // 营业日报表项目-统计-d、房租收入
		public static final String REPORT_ROOM_CHECKIN_E = "e、平均房价"; // 营业日报表项目-统计-e、平均房价
		public static final String REPORT_ROOM_CHECKIN_F = "f、人数统计"; // 营业日报表项目-统计-f、人数统计
		public static final String REPORT_ROOM_CHECKIN_G = "g、客房其他收入"; // 营业日报表项目-统计-g、客房其他收入
	}

	public interface ReportProjectType {
		public static final String REPORT_ROOM_RATE = "roomrate";// 营业日报表项目类型-房费统计
		public static final String REPORT_ROOM_NUM_A = "room_a";// 营业日报表项目-房间统计 a、客房总数
		public static final String REPORT_ROOM_NUM_B = "room_b"; // 营业日报表项目-房间统计 b、出租总数
		public static final String REPORT_ROOM_NUM_C = "room_c"; // 营业日报表项目-房间统计 c、出售率
		public static final String REPORT_ROOM_NUM_D = "room_d"; // 营业日报表项目-房间统计 d、房租收入
		public static final String REPORT_ROOM_NUM_E = "room_e"; // 营业日报表项目-房间统计 e、平均房价
		public static final String REPORT_ROOM_NUM_F = "room_f"; // 营业日报表项目-房间统计 f、人数统计
		public static final String REPORT_ROOM_NUM_G = "room_g"; // 营业日报表项目-房间统计 g、客房其他收入
	}

	public interface auditNightMode {
		public static final String NIGHT_AUDIT_AUTO = "AUTO";// 夜审入账方式-自动入账
		public static final String NIGHT_AUDIT_MANUAL = "MANUAL";// 夜审入账方式-手动入账
	}

	public interface Code {
		public static final String  HALF_DAY_ROOM_FEE = "1002";
		public static final String  FULL_DAY_ROOM_FEE = "1001";
		public static final String  NIGHT_TRIAL = "1000";
		public static final String  TO_AR = "9014";
	}

	public interface quartzType {
		public static final String NIGHT_AUDIT = "AUDIT";// 夜审入账方式-自动入账
		public static final String ALL = "ALL";// 夜审入账方式-手动入账
		public static final String NORMAL = "NORMAL";// 夜审入账方式-手动入账
	}

	public interface Flag {
		public static final String FEE_FTA = "FTA";
		public static final String FEE_RES = "RES";
		public static final String FEE_TRANSFER = "TRA";
		public static final String FEE_OLD_RES = "ORES";
		public static final String FEE_OFFSET_TO_TRANSFER = "OTRA";
	}
}
