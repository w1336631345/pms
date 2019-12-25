package com.kry.pms.model.persistence.busi;

import com.kry.pms.model.persistence.dict.RoomChangeReason;
import com.kry.pms.model.persistence.guest.Customer;
import com.kry.pms.model.persistence.room.GuestRoom;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "t_room_change_record")
public class RoomChangeRecord {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(columnDefinition = "varchar(64)")
    protected String id;
    @Column(columnDefinition = "varchar(64) COMMENT '酒店编码'")
    protected String hotelCode;
    @OneToOne
    private GuestRoom oldGuestRoom;
    @Column(columnDefinition = "varchar(64) COMMENT '曾住房'")
    private String oldRoomNum;
    @OneToOne
    private GuestRoom newGuestRoom;
    @Column(columnDefinition = "varchar(64) COMMENT '新换房'")
    private String newRoomNum;
    @Column(columnDefinition = "varchar(64) COMMENT '处理方式：B补差价，F免费升级'")
    private String handleType;
    @Column(columnDefinition = "double(8,2) COMMENT '房价差'")
    private Double priceDifferential;
    @Column(columnDefinition = "varchar(10) COMMENT '状态：0结清，1未结清'")
    private String status;
    @OneToOne
    private Customer customer;
    @OneToOne
    private RoomChangeReason roomChangeReason;//换房原因
    @Column(columnDefinition = "varchar(500) COMMENT '备注'")
    private String remark;

    @Column(columnDefinition = "datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'")
    private LocalDateTime createDate;



}
