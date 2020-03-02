package com.kry.pms.model.persistence.log;

import com.kry.pms.model.func.UseInfoAble;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "t_room_source_use_log")
public class RoomSourceUseLog {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(columnDefinition = "varchar(64)")
    protected String id;
    private LocalDateTime logTime;
    private Integer roomCount;
    private String roomType;
    private String operationName;
    private String roomNum;
    private String summaryInfo;
    private String businessKey;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    RoomSourceUseLog() {

    }

    public RoomSourceUseLog(UseInfoAble info,String operationName) {
        this.operationName = operationName;
        this.logTime = LocalDateTime.now();
        this.roomCount = info.getRoomCount();
        if(info.roomType()!=null){
            this.roomType = info.roomType().getName();
        }
        if(info.guestRoom()!=null){
            this.roomNum = info.guestRoom().getRoomNum();
        }
        this.summaryInfo = info.getSummaryInfo();
        this.businessKey = info.getBusinessKey();
        this.startTime = info.getStartTime();
        this.endTime = info.getEndTime();
    }
}
