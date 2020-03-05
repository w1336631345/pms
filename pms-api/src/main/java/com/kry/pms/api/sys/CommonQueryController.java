package com.kry.pms.api.sys;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.model.http.response.report.CommomReportTableData;
import com.kry.pms.service.report.CommomReportService;
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
@RequestMapping(path = "/api/v1/report/commomQuery")
public class CommonQueryController extends BaseController {
    @Autowired
    CommonQueryService commonQueryService;

    @GetMapping("/{code}")
    public HttpResponse<List<Map<String,Object>>> fetchData(@PathVariable("code") String id, HttpServletRequest request) throws IOException, TemplateException {
        HttpResponse<List<Map<String,Object>>> rep = new HttpResponse<>();
        rep.setData(commonQueryService.commonQuery(getCurrentHotleCode(),id, parse2Map(request)));
        return rep;
    }

}