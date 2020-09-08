package com.kry.pms.dao.busi;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.busi.BosBillItem;

import java.util.List;

public interface BosBillItemDao extends BaseDao<BosBillItem>{

    List<BosBillItem> findByBosBillId(String billId);
}
