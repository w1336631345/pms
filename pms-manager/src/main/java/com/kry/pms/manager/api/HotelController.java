package com.kry.pms.manager.api;

import com.kry.pms.base.HttpResponse;
import com.kry.pms.manager.entity.NewHotelVo;
import com.kry.pms.service.sys.SqlTemplateService;
import org.springframework.beans.factory.annotation.Autowired;

public class HotelController {
    @Autowired
    SqlTemplateService sqlTemplateService;

    /**
     * 创建新酒店
     *
     * @return
     */
    public HttpResponse<String> createNewHotel(NewHotelVo newHotelVo) {
        HttpResponse<String> response = new HttpResponse<>();
        sqlTemplateService.callProcedure("initNewHotel", newHotelVo.getCorpCode(), newHotelVo.getCode(), newHotelVo.getName(), newHotelVo.getSrcCode());
        return response;
    }

    /**
     * 清空酒店业务数据
     *
     * @param hotelCode
     * @return
     */
    public HttpResponse<String> cleanBusinessData(String hotelCode) {
        HttpResponse<String> response = new HttpResponse<>();
        return response;
    }
}
