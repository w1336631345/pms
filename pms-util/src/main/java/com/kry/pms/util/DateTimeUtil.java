package com.kry.pms.util;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtil {
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");
	public static LocalDateTime parse(String data) {
		return LocalDateTime.parse(data,DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	}

	public static String toDayStartStr(){
		Calendar calendar1 = Calendar.getInstance();
		calendar1.set(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH),
				0, 0, 0);
		Date beginOfDate = calendar1.getTime();
		return sdf.format(beginOfDate);
	}
	public static Date toDayStartDate(){
		Calendar calendar1 = Calendar.getInstance();
		calendar1.set(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH),
				0, 0, 0);
		Date beginOfDate = calendar1.getTime();
		return beginOfDate;
	}
	public static String toDayEndStr(){
		Calendar calendar2 = Calendar.getInstance();
		calendar2.set(calendar2.get(Calendar.YEAR), calendar2.get(Calendar.MONTH), calendar2.get(Calendar.DAY_OF_MONTH),
				23, 59, 59);
		Date endOfDate = calendar2.getTime();
		return sdf.format(endOfDate);
	}
	public static Date toDayEndDate(){
		Calendar calendar2 = Calendar.getInstance();
		calendar2.set(calendar2.get(Calendar.YEAR), calendar2.get(Calendar.MONTH), calendar2.get(Calendar.DAY_OF_MONTH),
				23, 59, 59);
		Date endOfDate = calendar2.getTime();
		return endOfDate;
	}
}
