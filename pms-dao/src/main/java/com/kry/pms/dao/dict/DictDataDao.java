package com.kry.pms.dao.dict;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.dict.DictData;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DictDataDao extends BaseDao<DictData>{
    @Query(value = "select d from DictData d ,DictType t where d.dictTypeCode = t.code and d.hotelCode=?1 and t.safeLevel = ?2 order by d.sortNum asc")
     List<DictData> findByHotelCodeAndSafeLevel(String hotelCode,int safeLevel);

    DictData findByHotelCodeAndCode(String hotelCode, String code);
    DictData findByHotelCodeAndCodeAndDeleted(String hotelCode, String code,int deleted);

    @Query(nativeQuery = true, value = " select \n" +
            " td.code \n" +
            "from t_dict_data td \n" +
            "where td.hotel_code = ?1 \n" +
            "and td.dict_type_code = ?2 ")
    List<String> getArchivesType(String hotelCode, String dictTypeCode);

    DictData findByHotelCodeAndCodeAndDictTypeCodeAndDeleted(String hotelCode, String code, String dictTypeCode, int deletedFalse);
}
