package com.kry.pms.service.dict.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.kry.pms.model.persistence.sys.SystemConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.Constants;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.dict.DictDataDao;
import com.kry.pms.model.persistence.dict.DictData;
import com.kry.pms.service.dict.DictDataService;

@Service
public class DictDataServiceImpl implements DictDataService {
    @Autowired
    DictDataDao dictDataDao;

    @Override
    public DictData add(DictData dictData) {
        DictData exist = dictDataDao.findByHotelCodeAndCodeAndDictTypeCodeAndDeleted(dictData.getHotelCode(),dictData.getCode(),dictData.getDictTypeCode(),Constants.DELETED_FALSE);
        if(exist!=null){
            return null;
        }
        return dictDataDao.saveAndFlush(dictData);
    }

    @Override
    public void delete(String id) {
        DictData dictData = dictDataDao.findById(id).get();
        if (dictData != null) {
            dictData.setDeleted(Constants.DELETED_TRUE);
        }
        modify(dictData);
    }

    @Override
    public DictData modify(DictData dictData) {
        return dictDataDao.saveAndFlush(dictData);
    }

    @Override
    public DictData findById(String id) {
        return dictDataDao.getOne(id);
    }

    @Override
    public List<DictData> getAllByHotelCode(String code) {
        return null;//默认不实现
        //return dictDataDao.findByHotelCode(code);
    }

    @Override
    public PageResponse<DictData> listPage(PageRequest<DictData> prq) {
        Example<DictData> ex = Example.of(prq.getExb());
        org.springframework.data.domain.PageRequest req;
        if (prq.getOrderBy() != null) {
            Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
            req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
        } else {
            req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
        }
        return convent(dictDataDao.findAll(ex, req));
    }
    @Override
    public Map<String, Map<String, String>> getWebDict(String hotelCode) {
        List<DictData> list = dictDataDao.findByHotelCodeAndSafeLevel(hotelCode, 0);
        Map<String, Map<String, String>> data = new HashMap<>();
        Map<String, String> item = null;
        for (DictData d : list) {      //因为在Map里面过滤了一次，所以说虽然查询有序，但是展示还是乱序了
            if (!data.containsKey(d.getDictTypeCode())) {  //如果最外层的key不存在（类型，例如ARType），则直接插入
//                item = new HashMap<>();
                if ("ARType".equals(d.getDictTypeCode())){    //AR账务类型的需要有序，初始化成LinkedHashMap，其余的还是声明成HashMap
                    item = new LinkedHashMap<>();
                }else{
                    item = new HashMap<>();
                }
                item.put(d.getCode(), d.getDescription());
                data.put(d.getDictTypeCode(), item);
            } else {
                data.get(d.getDictTypeCode()).put(d.getCode(), d.getDescription());  //否则指定key后插入
            }
        }
        return data;
    }


}
