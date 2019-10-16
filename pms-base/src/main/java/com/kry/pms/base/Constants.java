package com.kry.pms.base;

import java.time.LocalTime;

public class Constants {
	public static final String KEY_PAGE_SIZE = "pageSize";
	public static final String KEY_PAGE_NUM = "pageNum";
	public static final String KEY_METHOD_SET_PRE = "set";
	public static final String KEY_METHOD_GET_PRE = "get";
	public static final String KEY_DEFAULT_SEPARATOR = ",";
	public interface Operation{
		public static final String BOOK_CANCEL = "cancel";
		public static final String BOOK_VERIFY_PASS = "verify_pass";
		public static final String BOOK_VERIFY_REFUSE = "verify_refuse";
	}
	
	public interface Status {
		public static final String DELETE = "DELETE";
		public static final String DRAFT = "DRAFT";
		public static final String NORMAL = "normal";
		public static final String DISABLE = "DISABLE";
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
		public static final String CHECKIN_RECORD_STATUS_RESERVATION = "R";
		public static final String CHECKIN_RECORD_STATUS_CHECK_IN = "I";
		public static final String CHECKIN_RECORD_STATUS_CHECK_OUT = "O";
		public static final String CHECKIN_RECORD_STATUS_DEPARTURE_YESTERDAY = "D";
		public static final String CHECKIN_RECORD_STATUS_NO_SHOW = "N";
		public static final String CHECKIN_RECORD_STATUS_OUT_UNSETTLED = "S";
		public static final String CHECKIN_RECORD_STATUS_CANCLE_BOOK = "X";
		public static final String BILL_PAYMENTED = "PAYMENTED";
		public static final String BILL_TO_BE_PAID = "TO_BE_PAID";
		public static final String BILL_NO_PAYMENT_REQUIRED = "NO_PAYMENT_REQUIRED";

	}

	public interface Type {
		public static final String NO_BOOK_CHECK_IN = "nobook";
		public static final String BOOK_CHECK_IN = "booked";
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
		// 自由定价
		public static final String PRODUCT_FREE_PRICE = "FREE_PRICE";
		// 定价商品
		public static final String PRODUCT_FIXED_PRICE = "FIXED_PRICE";
		// 实物商品
		public static final String PRODUCT_GOODS = "GOODS";
		// 服务（虚拟商品）
		public static final String PRODUCT_SERVICE = "SERVICE";

	}

	public static final int DELETED_TRUE = 1;
	public static final int DELETED_FALSE = 0;
	public static final int CODE_SHOW_LEVEL_ERROR = 2;
	public static final int CODE_SHOW_LEVEL_WARING = 1;
	public static final int CODE_SHOW_LEVEL_SUCCESS = 0;

	public interface ErrorCode {
		public static final int REQUIRED_PARAMETER_MISSING = 600;
		public static final int REQUIRED_PARAMETER_INVALID = 601;
	}

	public interface BusinessCode {
		public static final int CODE_RESOURCE_NOT_ENOUGH = 900;
		public static final int CODE_PARAMETER_INVALID = 901;
		public static final int CODE_ILLEGAL_OPERATION = 801;
		
		
		public static final String DEPT_MARKETING_DEFAULT_CODE="MARKETING";
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
}
