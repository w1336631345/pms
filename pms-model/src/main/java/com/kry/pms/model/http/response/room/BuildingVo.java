package com.kry.pms.model.http.response.room;

import java.io.Serializable;
import java.util.ArrayList;

import lombok.Data;

@Data
public class BuildingVo implements Serializable{
	String id;
	String name;
	ArrayList<FloorVo> floors;
}
