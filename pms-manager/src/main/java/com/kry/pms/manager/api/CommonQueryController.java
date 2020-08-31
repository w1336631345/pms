package com.kry.pms.manager.api;

import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageResponse;
import com.kry.pms.service.sys.CommonQueryService;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/v1/common/commomQuery")
public class CommonQueryController extends BaseController {
    @Autowired
    CommonQueryService commonQueryService;

    @GetMapping("/{code}")
    public HttpResponse<List<Map<String, Object>>> fetchData(@PathVariable("code") String id, String hotelCode, HttpServletRequest request) throws IOException, TemplateException {
        HttpResponse<List<Map<String, Object>>> rep = new HttpResponse<>();
        rep.setData(commonQueryService.commonQuery(hotelCode, id, parse2Map(request)));
        return rep;
    }

    @GetMapping("/page/{code}")
    public HttpResponse<PageResponse<Map<String, Object>>> fetchPageData(@PathVariable("code") String id, String hotelCode, HttpServletRequest request) throws IOException, TemplateException {
        HttpResponse<PageResponse<Map<String, Object>>> rep = new HttpResponse<>();
        rep.setData(commonQueryService.commonPageQuery(hotelCode, id, parse2CommonPageRequest(request)));
        return rep;
    }

}