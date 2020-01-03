package com.kry.pms.service.busi.impl;

import com.kry.pms.base.Constants;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.busi.RoomLinkDao;
import com.kry.pms.model.http.request.busi.RoomLinkBo;
import com.kry.pms.model.persistence.busi.CheckInRecord;
import com.kry.pms.model.persistence.busi.RoomLink;
import com.kry.pms.service.busi.CheckInRecordService;
import com.kry.pms.service.busi.RoomLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoomLinkServiceImpl implements RoomLinkService {

    @Autowired
    RoomLinkDao roomLinkDao;
    @Autowired
    CheckInRecordService checkInRecordService;

    @Override
    public RoomLink add(RoomLink entity) {
        RoomLink rl = roomLinkDao.save(entity);
        return rl;
    }

    @Override
    public void delete(String id) {
        roomLinkDao.deleteById(id);
    }

    @Override
    public RoomLink modify(RoomLink roomLink) {
        RoomLink rl = roomLinkDao.saveAndFlush(roomLink);
        return rl;
    }

    @Override
    public RoomLink findById(String id) {
        return null;
    }

    @Override
    public List<RoomLink> getAllByHotelCode(String code) {
        return null;
    }

    @Override
    public PageResponse<RoomLink> listPage(PageRequest<RoomLink> prq) {
        return null;
    }

    //添加联房
    @Override
    public HttpResponse addRoomLink(RoomLinkBo roomLinkBo){
        HttpResponse hr = new HttpResponse();
        String roomLinkId = null;
        if(roomLinkBo.getRoomLinkId() != null && roomLinkBo.getRoomLinkId() != ""){
            roomLinkId = roomLinkBo.getRoomLinkId();
            String ids[] = roomLinkBo.getIds();
            for(int i=0; i<ids.length; i++){
                CheckInRecord cir = checkInRecordService.findById(ids[i]);
                if(cir.getGuestRoom() != null){
                    String roomNum = cir.getGuestRoom().getRoomNum();
                    String hotelCode = cir.getHotelCode();
                    List<CheckInRecord> list = checkInRecordService.getByRoomNum(roomNum, hotelCode, Constants.Type.CHECK_IN_RECORD_GROUP_TYPE_NO);
                    for(int m=0; m<list.size(); m++){
                        CheckInRecord cirRoomNum = list.get(m);
                        cirRoomNum.setRoomLinkId(roomLinkBo.getRoomLinkId());
                        checkInRecordService.update(cirRoomNum);
                    }
                }
                cir.setRoomLinkId(roomLinkBo.getRoomLinkId());
                checkInRecordService.update(cir);
            }
        }else {
            RoomLink rl = new RoomLink();
            rl.setDeleted(Constants.DELETED_FALSE);
            add(rl);
            roomLinkId = rl.getId();
            String ids[] = roomLinkBo.getIds();
            for(int i=0; i<ids.length; i++){
                CheckInRecord cir = checkInRecordService.findById(ids[i]);
                if(cir.getGuestRoom() != null){
                    String roomNum = cir.getGuestRoom().getRoomNum();
                    String hotelCode = cir.getHotelCode();
                    List<CheckInRecord> list = checkInRecordService.getByRoomNum(roomNum, hotelCode, Constants.Type.CHECK_IN_RECORD_GROUP_TYPE_NO);
                    for(int m=0; m<list.size(); m++){
                        CheckInRecord cirRoomNum = list.get(m);
                        cirRoomNum.setRoomLinkId(rl.getId());
                        checkInRecordService.update(cirRoomNum);
                    }
                }
                cir.setRoomLinkId(rl.getId());
                checkInRecordService.update(cir);
            }
            String[] id = roomLinkBo.getId();
            for(int j=0; j<id.length; j++){
                CheckInRecord cir = checkInRecordService.findById(id[j]);
                cir.setRoomLinkId(rl.getId());
                checkInRecordService.update(cir);
            }
        }
        hr.setData(roomLinkId);
        return hr;
    }

    @Override
    public HttpResponse deleteRoomLink(String[] ids) {
        HttpResponse hr = new HttpResponse();
        String roomLinkId = null;
        for(int i=0; i<ids.length; i++){
            CheckInRecord cir = checkInRecordService.findById(ids[i]);
            if(roomLinkId == null){
                roomLinkId = cir.getRoomLinkId();
            }
            String roomNum = cir.getGuestRoom().getRoomNum();
            String hotelCode = cir.getHotelCode();
            List<CheckInRecord> list = checkInRecordService.getByRoomNum(roomNum, hotelCode, Constants.Type.CHECK_IN_RECORD_GROUP_TYPE_NO);
            for(int m=0; m<list.size(); m++){
                CheckInRecord cirRoomNum = list.get(m);
                cirRoomNum.setRoomLinkId(null);
                checkInRecordService.update(cirRoomNum);
            }
            cir.setRoomLinkId(null);
            checkInRecordService.update(cir);
        }
        List<CheckInRecord> list = checkInRecordService.getRoomLinkList(roomLinkId);
        String roomNum = null;
        boolean isOne = true;
        for(int j=0; j<list.size(); j++){
            CheckInRecord cir = list.get(j);
            if(cir.getGuestRoom() != null){
                if(j==0){
                    roomNum = cir.getGuestRoom().getRoomNum();
                }
                if(roomNum != null){
                    String eNum = cir.getGuestRoom().getRoomNum();
                    if(!roomNum.equals(eNum)){
                        isOne = false;
                    }
                }
            }
        }
        if(isOne){
            for(int i=0; i<list.size(); i++){
                CheckInRecord cir = list.get(i);
                cir.setRoomLinkId(null);
                checkInRecordService.update(cir);
            }
            if(roomLinkId != null){
                delete(roomLinkId);
            }
        }
        return hr.ok();
    }


}
