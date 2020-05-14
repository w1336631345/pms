package com.kry.pms.api.report;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.model.http.response.report.CommomReportTableData;
import com.kry.pms.model.persistence.sys.BusinessSeq;
import com.kry.pms.service.report.CommomReportService;
import com.kry.pms.service.sys.BusinessSeqService;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/v1/report/commomReport")
public class CommomReportController extends BaseController {
    @Autowired
    CommomReportService commomReportService;
    @Autowired
    BusinessSeqService businessSeqService;

    @GetMapping("/{id}")
    public HttpResponse<CommomReportTableData> fetchData(@PathVariable("id") String id, HttpServletRequest request) throws IOException, TemplateException {
        HttpResponse<CommomReportTableData> rep = new HttpResponse<>();
        Map<String, Object> parmrs = parse2Map(request);
        baseParmrsCheck(parmrs);
        rep.setData(commomReportService.fetchCommonReport(id, parmrs));
        return rep;
    }

    private void baseParmrsCheck(Map<String, Object> parmrs) {
        String hotelCode = getCurrentHotleCode();
        parmrs.put("hotel_code", hotelCode);
        if (!parmrs.containsKey("business_date")) {
            parmrs.put("business_date", businessSeqService.getBuinessDate(hotelCode));
        }
        if (!parmrs.containsKey("shift")) {
            parmrs.put("shift", getShiftCode());
        }
        if (!parmrs.containsKey("employee_id")) {
            parmrs.put("employee_id", getCurrentEmployee().getId());
        }
    }

}
