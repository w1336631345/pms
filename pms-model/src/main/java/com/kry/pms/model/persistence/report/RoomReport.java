package com.kry.pms.model.persistence.report;

import com.kry.pms.model.persistence.room.GuestRoom;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "t_report_room")
public class RoomReport {

    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Id
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    private GuestRoom guestRoom;
    @Column(columnDefinition = "varchar(100) COMMENT '房间类型'")
    private String roomTypeName;
    //VC空的干净房  VD空的脏房 OC住的干净房 OD住的脏房 OO维修房 OS锁房 EA本日将到 ED本日将离 R1无资源空房 R2本日到为排房
    @Column(columnDefinition = "varchar(100) COMMENT '房间状态'")
    private String roomStatus;
    @Column(columnDefinition = "varchar(100) COMMENT '房间编号'")
    private String roomNum;
    @Column(columnDefinition = "varchar(100) COMMENT '是否删除'")
    private String deleted;

    @Column(columnDefinition = "varchar(100) COMMENT '酒店代码'")
    private String hotelCode;
    @Column(columnDefinition = "date COMMENT '记录的营业日期'")
    private LocalDate businessDate;
    @Column(columnDefinition = "varchar(50) COMMENT '排序'")
    private String sort;
}
