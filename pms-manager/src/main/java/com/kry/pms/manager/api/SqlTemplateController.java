package com.kry.pms.manager.api;

import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.persistence.sys.SqlTemplate;
import com.kry.pms.service.sys.SqlTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = "/api/v1/sys/sqlTemplate")
public class SqlTemplateController  {
    @Autowired
    SqlTemplateService sqlTemplateService;
    @PostMapping
    public HttpResponse<SqlTemplate> add(@RequestBody SqlTemplate sqlTemplate) {
        return new HttpResponse<SqlTemplate>().addData(sqlTemplateService.add(sqlTemplate));
    }

    @PutMapping
    public HttpResponse<SqlTemplate> modify(@RequestBody SqlTemplate sqlTemplate) {
        return new HttpResponse<SqlTemplate>().addData(sqlTemplateService.modify(sqlTemplate));
    }

    @DeleteMapping
    public HttpResponse<String> delete(String id) {
        HttpResponse<String> rep = new HttpResponse<>();
        sqlTemplateService.delete(id);
        return rep;
    }

    @GetMapping
    public HttpResponse<PageResponse<SqlTemplate>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
        HttpResponse<PageResponse<SqlTemplate>> rep = new HttpResponse<PageResponse<SqlTemplate>>();
        PageRequest<SqlTemplate> req =  new PageRequest<>();
        return rep.addData(sqlTemplateService.listPage(req));
    }

}
