package com.kry.pms.manager.api;

import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.http.response.report.ReportTableDefinitionListVo;
import com.kry.pms.model.persistence.report.ReportTableDefinition;
import com.kry.pms.service.report.ReportTableDefinitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/report/reportTableDefinition")
public class ReportTableDefinitionController extends  BaseController<ReportTableDefinition>{
    @Autowired
    ReportTableDefinitionService reportTableDefinitionService;

    @PostMapping
    public HttpResponse<ReportTableDefinition> add(@RequestBody ReportTableDefinition reportTableDefinition) {
        return new HttpResponse<ReportTableDefinition>().addData(reportTableDefinitionService.add(reportTableDefinition));
    }

    @PutMapping
    public HttpResponse<ReportTableDefinition> modify(@RequestBody ReportTableDefinition reportTableDefinition) {
        return new HttpResponse<ReportTableDefinition>().addData(reportTableDefinitionService.modify(reportTableDefinition));
    }

    @DeleteMapping
    public HttpResponse<String> delete(String id) {
        HttpResponse<String> rep = new HttpResponse<>();
        reportTableDefinitionService.delete(id);
        return rep;
    }


    @GetMapping("/build")
    public HttpResponse<String> build(String id) {
        HttpResponse<String> rep = new HttpResponse<>();
        reportTableDefinitionService.buildTemplate(id);
        return rep;
    }

    @GetMapping
    public HttpResponse<PageResponse<ReportTableDefinition>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException {
        HttpResponse<PageResponse<ReportTableDefinition>> rep = new HttpResponse<PageResponse<ReportTableDefinition>>();
        PageRequest<ReportTableDefinition> req = parse2PageRequest(request);
        return rep.addData(reportTableDefinitionService.listPage(req));
    }

    @GetMapping
    @RequestMapping("/group/{groupKey}")
    public HttpResponse<List<ReportTableDefinitionListVo>> query(@PathVariable("groupKey") String groupKey) throws InstantiationException, IllegalAccessException {
        HttpResponse<List<ReportTableDefinitionListVo>> rep = new HttpResponse<List<ReportTableDefinitionListVo>>();
        return rep.addData(reportTableDefinitionService.groupKey(groupKey));
    }
}
