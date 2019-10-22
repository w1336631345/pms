package com.kry.pms.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
	public static LocalDateTime parse(String data) {
		return LocalDateTime.parse(data,DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	}
}
