package com.kry.pms.manager.api;

import com.kry.pms.base.HttpResponse;
import com.kry.pms.model.persistence.sys.BusinessSeq;
import com.kry.pms.service.sys.BusinessSeqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/common/businessSeq")
public class BusinessSeqController extends BaseController<BusinessSeq> {
    @Autowired
    BusinessSeqService businessSeqService;

    @GetMapping(path = "/list/{hotelCode}")
    public HttpResponse<List<BusinessSeq>> list(@PathVariable("hotelCode") String hotelCode) {
        HttpResponse<List<BusinessSeq>> rep = new HttpResponse<List<BusinessSeq>>();
        List<BusinessSeq> list = businessSeqService.getAllByHotelCode(hotelCode);
        return rep.addData(list);
    }

    @PostMapping
    public HttpResponse<BusinessSeq> add(@RequestBody BusinessSeq businessSeq) {
        return getDefaultResponse().addData(businessSeqService.add(businessSeq));
    }

    @PostMapping("/batch")
    public HttpResponse<List<BusinessSeq>> batch(@RequestBody List<BusinessSeq> businessSeqs) {
        HttpResponse<List<BusinessSeq>> rep = new HttpResponse<List<BusinessSeq>>();
        rep.setData(businessSeqService.batchAdd(businessSeqs));
        return rep;
    }
    @PutMapping
    public HttpResponse<BusinessSeq> modify(@RequestBody BusinessSeq businessSeq) {
        return getDefaultResponse().addData(businessSeqService.modify(businessSeq));
    }

    @DeleteMapping
    public HttpResponse<String> delete(String id) {
        HttpResponse<String> rep = new HttpResponse<>();
        businessSeqService.delete(id);
        return rep;
    }
}
