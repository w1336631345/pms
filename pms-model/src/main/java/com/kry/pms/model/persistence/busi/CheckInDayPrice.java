package com.kry.pms.model.persistence.busi;

import com.kry.pms.model.persistence.marketing.DiscountScheme;
import com.kry.pms.model.persistence.marketing.RoomPriceScheme;
import com.kry.pms.model.persistence.marketing.RoomPriceSchemeItem;
import com.kry.pms.model.persistence.room.GuestRoom;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

@Data
public class CheckInDayPrice {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(columnDefinition = "varchar(64)")
    protected String id;
    @Column(columnDefinition = "date COMMENT '入住当天时间'")
    private LocalDate checkInDay;
    @Column(columnDefinition = "double(8,2) COMMENT '房价'")
    private Double roomPrice;
    @Column(columnDefinition = "double(8,2) COMMENT '实付价/成交价'")
    private Double personalPrice;
    @Column(columnDefinition = "double(8,2) COMMENT '折扣'")
    private Double disCount;
    @Column(columnDefinition = "double(8,2) COMMENT '折扣%'")
    private String disCountStr;
    @OneToOne
    private DiscountScheme discountScheme;//优惠
    @OneToOne
    private RoomPriceSchemeItem priceSchemeItem;//包价
    @OneToOne
    private GuestRoom guestRoom;//房间
    @OneToOne
    private RoomPriceScheme roomPriceScheme;//房价方案
    @Column(columnDefinition = "varchar(64) COMMENT '酒店编号'")
    private String hotelCode;
    @ManyToOne
    private CheckInRecord checkInRecord;
}
