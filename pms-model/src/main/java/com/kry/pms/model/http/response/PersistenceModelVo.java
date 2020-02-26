package com.kry.pms.model.http.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PersistenceModelVo {

    private String id;

    private LocalDateTime createDate;

    private String createUser;

    private LocalDateTime updateDate;

    private String updateUser;

    private Integer deleted;

    private String status;

    private String corporationCode;

    private String hotelCode;
}
