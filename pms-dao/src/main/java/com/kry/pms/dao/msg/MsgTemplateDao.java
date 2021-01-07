package com.kry.pms.dao.msg;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.msg.MsgTemplate;

import java.util.List;

public interface MsgTemplateDao extends BaseDao<MsgTemplate>{

    List<MsgTemplate> findByHotelCode(String hotelCode);

    MsgTemplate findByHotelCodeAndStatusAndSentTypeAndTypeCode(String hotelCode, String status, String sentType, String typeCode);
}
