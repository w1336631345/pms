package com.kry.pms.model.http.request.busi;

import javax.validation.constraints.NotBlank;

import com.kry.pms.model.http.request.room.BaseBo;

import lombok.Data;

@Data
public class GuestInfoBo extends BaseBo {
	@NotBlank
	private String name;
	@NotBlank
	private String mobile;
	
	private String certificateType;
	@NotBlank
	private String idCardNum;
	
	private String idcardAddress;
	
	private String dateOfBirth;
	
	private String licensingAuthority;
	
	private String countryOrRegionCode;
}
