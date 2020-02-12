package com.kry.pms.api.dict;

import com.kry.pms.base.HttpResponse;
import com.kry.pms.util.HttpUtil;
import org.apache.commons.collections4.MapUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/v1/dict/weather")
public class WeatherController {

    @GetMapping
    public HttpResponse<Map<String, Object>> getWeather() throws IOException {
        HttpResponse hr = new HttpResponse();
        String dz = HttpUtil.doGet("http://pv.sohu.com/cityjson?ie=utf-8");//get请求
        String jsonStr = dz.split("=")[1].split(";")[0];
        Map<String, Object> map1 = HttpUtil.json2map(jsonStr);
        String cid = MapUtils.getString(map1, "cid");
        Map<String, Object> map2 = HttpUtil.getTodayWeather1(cid);
        hr.addData(map2);
        return hr;
    }

}
