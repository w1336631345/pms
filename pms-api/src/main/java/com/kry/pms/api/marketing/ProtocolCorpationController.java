package com.kry.pms.api.marketing;

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
import com.kry.pms.model.persistence.marketing.ProtocolCorpation;
import com.kry.pms.service.marketing.ProtocolCorpationService;

@RestController
@RequestMapping(path = "/api/v1/marketing/protocolCorpation")
public class ProtocolCorpationController extends BaseController<ProtocolCorpation> {
	@Autowired
	ProtocolCorpationService protocolCorpationService;
	@PostMapping
	public HttpResponse<ProtocolCorpation> add(@RequestBody ProtocolCorpation protocolCorpation) {
		return getDefaultResponse().addData(protocolCorpationService.add(protocolCorpation));
	}

	@PutMapping
	public HttpResponse<ProtocolCorpation> modify(@RequestBody ProtocolCorpation protocolCorpation) {
		return getDefaultResponse().addData(protocolCorpationService.modify(protocolCorpation));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		protocolCorpationService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<ProtocolCorpation>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<ProtocolCorpation>> rep = new HttpResponse<PageResponse<ProtocolCorpation>>();
		PageRequest<ProtocolCorpation> req = parse2PageRequest(request);
		return rep.addData(protocolCorpationService.listPage(req));
	}

}
