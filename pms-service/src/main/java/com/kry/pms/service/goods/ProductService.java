package com.kry.pms.service.goods;

import com.kry.pms.model.persistence.goods.Product;
import com.kry.pms.service.BaseService;

import java.util.List;
import java.util.Map;

public interface ProductService extends BaseService<Product>{

    Product getById(String id);

    Product findHalfRoomFee(String hotelCode);

    Product findFullRoomFee(String hotelCode);

    Product findNightTrial(String hotelCode);

    List<Map<String, Object>> getPaySetList(String hotelCode);

    List<Map<String, Object>> getPaySetListOtherStatus(String hotelCode);

    List<String> getDictCode(String hotelCode);

    Product findToArProduct(String hotelCode);
}