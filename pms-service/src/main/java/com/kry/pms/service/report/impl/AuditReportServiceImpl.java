package com.kry.pms.service.report.impl;

import com.kry.pms.dao.report.AuditReportDao;
import com.kry.pms.model.persistence.sys.User;
import com.kry.pms.service.report.AuditReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuditReportServiceImpl implements AuditReportService {

    @Autowired
    AuditReportDao auditReportDao;

    @Override
    public List<Map<String, Object>> auditNight(User user, String businessDate){
        List<Map<String, Object>> list = auditReportDao.auditNights();
        List<Map<String, Object>> totalType = auditReportDao.totalCostType(user.getHotelCode(), businessDate);
        List<Map<String, Object>> relist = new ArrayList<>();
        //MC主营，SE服务费，RE Rebate，OT其它，TR税收
        for(int i=0; i<totalType.size(); i++){
            List<Map<String, Object>> child = new ArrayList<>();
            Map<String, Object> map = new HashMap<>();
            map.putAll(totalType.get(i));
            String code = totalType.get(i).get("cateGory_id").toString();
            for(int j=0; j<list.size(); j++){
                String childCode = list.get(j).get("cateGory_id").toString();
                if(childCode.equals(code)){
                    child.add(list.get(j));
                }
            }
            if(("N/A").equals(code)){
                map.put("code_name", "借方合计");
            }
            map.put("children", child);
            relist.add(map);
        }
        return relist;
    }

    @Override
    public List<Map<String, Object>> receivables(User user, String businessDate) {
        List<Map<String, Object>> list = auditReportDao.receivables(user.getHotelCode(), businessDate);
        List<Map<String, Object>> totalType = auditReportDao.totalPayType(user.getHotelCode(), businessDate);
        List<Map<String, Object>> relist = new ArrayList<>();
        //AL前厅，FB餐饮，BOS BOS，AR记账回收，OH其它
        for(int i=0; i<totalType.size(); i++){
            List<Map<String, Object>> child = new ArrayList<>();
            Map<String, Object> map = new HashMap<>();
            map.putAll(totalType.get(i));
            String code = totalType.get(i).get("cateGory_id").toString();
            for(int j=0; j<list.size(); j++){
                String childCode = list.get(j).get("cateGory_id").toString();
                if(childCode.equals(code)){
                    child.add(list.get(j));
                }
            }
            if(("N/A").equals(code)){
                map.put("code_name", "贷方合计");
            }
            map.put("children", child);
            relist.add(map);
        }
        return relist;
    }
}
