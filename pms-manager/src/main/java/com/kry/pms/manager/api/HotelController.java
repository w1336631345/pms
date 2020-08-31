package com.kry.pms.manager.api;

import com.kry.pms.base.Constants;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.manager.entity.NewHotelVo;
import com.kry.pms.model.persistence.org.Hotel;
import com.kry.pms.service.org.HotelService;
import com.kry.pms.service.sys.SqlTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/hotel")
public class HotelController {
    @Autowired
    SqlTemplateService sqlTemplateService;

    @Autowired
    HotelService hotelService;

    /**
     * 创建新酒店
     *
     * @return
     */
    @PostMapping("/new")
    public HttpResponse<String> createNewHotel(NewHotelVo newHotelVo) {
        HttpResponse<String> response = new HttpResponse<>();
        Hotel hotel = hotelService.getByHotelCode(newHotelVo.getSrcCode());
        if (hotel == null) {
            return response.error(Constants.BusinessCode.CODE_PARAMETER_INVALID, "找不到对应的源");
        }
        sqlTemplateService.callProcedure("initNewHotel", newHotelVo.getCorpCode(), newHotelVo.getCode(), newHotelVo.getName(), newHotelVo.getSrcCode());
        return response;
    }

    /**
     * 清空酒店业务数据
     *
     * @param hotelCode
     * @return
     */
    @GetMapping("/clean")
    public HttpResponse<String> cleanBusinessData(String hotelCode) {
        HttpResponse<String> response = new HttpResponse<>();
        Hotel hotel = hotelService.getByHotelCode(hotelCode);
        if(hotel!=null&&hotel.getType().startsWith("TEST")){
            sqlTemplateService.callProcedure("cleanBusinessData", hotelCode);
        }else{
            return response.error(Constants.BusinessCode.CODE_PARAMETER_INVALID, "找不到对应的酒店或非测试酒店无法清空数据");
        }
        return response;
    }
}
