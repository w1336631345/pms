package com.kry.pms.service.busi;

import com.kry.pms.base.HttpResponse;
import com.kry.pms.model.http.request.busi.RoomLinkBo;
import com.kry.pms.model.persistence.busi.CheckInRecord;
import com.kry.pms.model.persistence.busi.RoomLink;
import com.kry.pms.service.BaseService;
import org.springframework.data.domain.Page;

public interface RoomLinkService  extends BaseService<RoomLink> {

    HttpResponse addRoomLink(RoomLinkBo roomLinkBo);

    HttpResponse deleteRoomLink(String[] ids);
}
