package com.kry.pms.model.persistence.busi;

import lombok.Data;

import javax.persistence.Column;

@Data
public class RoomLinkGroup {
    @Column
    private String id;
    @Column
    private String linkNum;
    @Column
    private String hotelCode;
    @Column
    private String deleted;
}
