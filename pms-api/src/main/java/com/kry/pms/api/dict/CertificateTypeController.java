package com.kry.pms.api.dict;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.persistence.dict.CertificateType;
import com.kry.pms.service.dict.CertificateTypeService;

@RestController
@RequestMapping(path = "/api/v1/dict/certificateType")
public class CertificateTypeController extends BaseController<CertificateType> {
	@Autowired
	CertificateTypeService certificateTypeService;
	@PostMapping
	public HttpResponse<CertificateType> add(@RequestBody CertificateType certificateType) {
		return getDefaultResponse().addData(certificateTypeService.add(certificateType));
	}

	@PutMapping
	public HttpResponse<CertificateType> modify(@RequestBody CertificateType certificateType) {
		return getDefaultResponse().addData(certificateTypeService.modify(certificateType));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		certificateTypeService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<CertificateType>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<CertificateType>> rep = new HttpResponse<PageResponse<CertificateType>>();
		PageRequest<CertificateType> req = parse2PageRequest(request);
		return rep.addData(certificateTypeService.listPage(req));
	}

}
