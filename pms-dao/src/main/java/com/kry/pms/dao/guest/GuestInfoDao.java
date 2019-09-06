package com.kry.pms.dao.guest;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.guest.GuestInfo;

public interface GuestInfoDao extends BaseDao<GuestInfo>{

	GuestInfo findByIdCardNum(String idCardNum);

}
