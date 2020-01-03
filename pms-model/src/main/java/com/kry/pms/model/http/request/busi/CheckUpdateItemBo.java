package com.kry.pms.model.http.request.busi;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CheckUpdateItemBo {

    private String[] ids;
    private LocalDateTime arriveTime;//入住时间
    private LocalDateTime leaveTime;//离开时间
    private String days;//天数
    private String roomType; //房类
    private String roomPriceScheme;//房价方案
    private String purchasePrice;//房价
    private String contactName;//预订人
    private String distributionChannel;//渠道
    private String marketEmployee;//销售员
    private String discountScheme;//优惠
    private String holdTime;//保留时间
    private String demands;//要求
    private String customerLevel;//vip
    private String protocolCorpation;//公司单位
    private String remark;//备注

}
