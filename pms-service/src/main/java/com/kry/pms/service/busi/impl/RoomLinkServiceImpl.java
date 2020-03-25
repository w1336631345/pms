package com.kry.pms.service.busi.impl;

import com.kry.pms.base.Constants;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.busi.CheckInRecordDao;
import com.kry.pms.dao.busi.RoomLinkDao;
import com.kry.pms.model.http.request.busi.RoomLinkBo;
import com.kry.pms.model.persistence.busi.CheckInRecord;
import com.kry.pms.model.persistence.busi.RoomLink;
import com.kry.pms.model.persistence.room.GuestRoom;
import com.kry.pms.service.busi.CheckInRecordService;
import com.kry.pms.service.busi.RoomLinkService;
import com.kry.pms.service.sys.BusinessSeqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.awt.peer.CanvasPeer;
import java.util.ArrayList;
import java.util.List;

@Service
public class RoomLinkServiceImpl implements RoomLinkService {

    @Autowired
    RoomLinkDao roomLinkDao;
    @Autowired
    CheckInRecordService checkInRecordService;
    @Autowired
    CheckInRecordDao checkInRecordDao;
    @Autowired
    BusinessSeqService businessSeqService;

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
    @Transactional
    public HttpResponse addRoomLink(RoomLinkBo roomLinkBo){
        HttpResponse hr = new HttpResponse();
        String ids[] = roomLinkBo.getIds();
        String roomLinkId = null;
        int hCount = 0;
        List<String> roomList = new ArrayList<>();
        if(roomLinkBo.getRoomLinkId() != null && !"".equals(roomLinkBo.getRoomLinkId())){
            roomLinkId = roomLinkBo.getRoomLinkId();
        }else {
            RoomLink rl = new RoomLink();
            rl.setDeleted(Constants.DELETED_FALSE);
            add(rl);//没有roomLinkId说明之前没有联过房
            roomLinkId = rl.getId();
            String[] id = roomLinkBo.getId();
            for(int j=0; j<id.length; j++){
                CheckInRecord cir = checkInRecordService.findById(id[j]);
                cir.setRoomLinkId(roomLinkId);
                cir.setRoomLinkIdM("M");//因为初次联房，设置联房原数据是主数据
                checkInRecordService.update(cir);
            }
        }
        for(int i=0; i<ids.length; i++){
            CheckInRecord cir = checkInRecordService.findById(ids[i]);
            if(cir.getGuestRoom() != null){
                if(!roomList.contains(cir.getGuestRoom().getRoomNum())){
                    roomList.add(cir.getGuestRoom().getRoomNum());
                }
                List<CheckInRecord> list = checkInRecordService.findByOrderNumAndGuestRoomAndDeleted(cir.getOrderNum(), cir.getGuestRoom(), Constants.DELETED_FALSE);
                hCount = hCount + list.size();
                for(int m=0; m<list.size(); m++){
                    CheckInRecord cirRoomNum = list.get(m);
                    cirRoomNum.setOrderNumOld(cirRoomNum.getOrderNum());
                    cirRoomNum.setRoomLinkId(roomLinkId);
                    cirRoomNum.setOrderNum(roomLinkBo.getOrderNum());
                    checkInRecordService.update(cirRoomNum);
                }
            }
        }
        List<CheckInRecord> listMain = checkInRecordDao.findByOrderNumAndTypeAndDeleted(roomLinkBo.getOrderNum(), Constants.Type.CHECK_IN_RECORD_GROUP, Constants.DELETED_FALSE);
        if(listMain != null && !listMain.isEmpty()){
            CheckInRecord main = listMain.get(0);
            main.setRoomCount(main.getRoomCount() + roomList.size());
            main.setHumanCount(main.getHumanCount() + ids.length);
            checkInRecordService.update(main);
        }
        hr.setData(roomLinkId);
        return hr;
    }

    @Override
    public HttpResponse deleteRoomLink(String[] ids) {
        HttpResponse hr = new HttpResponse();
        String orderNum = null;
        List<String> roomList = new ArrayList<>();
        int hCount = 0;
        List<CheckInRecord> main = null;
        for(int i=0; i<ids.length; i++){
            CheckInRecord cir = checkInRecordService.findById(ids[i]);
            if(main == null){
                main = checkInRecordDao.findByOrderNumAndTypeAndDeleted(cir.getOrderNum(), Constants.Type.CHECK_IN_RECORD_GROUP, Constants.DELETED_FALSE);
            }
            if(cir.getOrderNumOld() != null){
                orderNum = cir.getOrderNumOld();
            }else {
                orderNum = businessSeqService.fetchNextSeqNum(cir.getHotelCode(),
                        Constants.Key.BUSINESS_ORDER_NUM_SEQ_KEY);
            }
            List<CheckInRecord> list = checkInRecordService.findByOrderNumAndGuestRoomAndDeleted(cir.getOrderNum(), cir.getGuestRoom(), Constants.DELETED_FALSE);
            for(int m=0; m<list.size(); m++){
                CheckInRecord cirRoomNum = list.get(m);
                if(!"M".equals(cirRoomNum.getRoomLinkIdM())){
                    if(!roomList.contains(cirRoomNum.getGuestRoom().getRoomNum())){
                        roomList.add(cirRoomNum.getGuestRoom().getRoomNum());
                    }
                    hCount = hCount + 1;
                    cirRoomNum.setRoomLinkId(null);
                    cirRoomNum.setOrderNum(orderNum);
                    checkInRecordService.update(cirRoomNum);
                }
            }
        }
        if(main != null && !main.isEmpty()){
            CheckInRecord cm = main.get(0);
            cm.setHumanCount(cm.getHumanCount() - hCount);
            cm.setRoomCount(cm.getRoomCount() - roomList.size());
            checkInRecordService.update(cm);
        }
        return hr.ok();
    }


}
