package com.kry.pms.model.http.response.sys;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
@Data
public class UserOnlineVO {

    private String userId;
    private String hotelCode;
    private String hotelName;
    private String username;
    private String ipAddress;
    private String personInCharge;
    private String phone;
    private Date loginDate;
    private String nickename;
    private String status;
    private String isMyself;

    private String host;
    private String macAddress;
    private String sessionId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startAccessTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastAccessTime;

}
