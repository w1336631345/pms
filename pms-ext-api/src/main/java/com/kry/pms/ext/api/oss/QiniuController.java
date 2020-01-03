package com.kry.pms.ext.api.oss;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kry.pms.base.HttpResponse;
import com.kry.pms.ext.config.QiniuConfig;
@RestController
@RequestMapping(path = "api/v1/oss")
public class QiniuController {
	@Autowired
	QiniuConfig qiniuConfig;

	@GetMapping("/upload_token")
	public HttpResponse<String> uploadToken() {
		HttpResponse<String> response = new HttpResponse<>();
		response.addData(qiniuConfig.uploadToken());
		return response;
	}

	@GetMapping("/access_token")
	public HttpResponse<String> accessToken(String baseUrl) {
		HttpResponse<String> response = new HttpResponse<>();
		response.addData(qiniuConfig.accessToken(baseUrl));
		return response;
	}

}
