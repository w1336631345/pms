package com.kry.pms.base;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ParamSpecification<T> {
    public Specification<T> createSpecification(Map<String, Object> map) {
        Specification<T> sf = new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<>();
                for(Map.Entry<String, Object> entry : map.entrySet()){
                    String mapKey = entry.getKey();
                    Map<String, Object> mapValue = (Map<String, Object>) entry.getValue();
                    if("like".equals(mapKey)){
                        for(Map.Entry<String, Object> entry1 : mapValue.entrySet()){
                            String code = entry1.getKey();
                            String value = entry1.getValue().toString();
                            if (StringUtils.isNotBlank(code) && StringUtils.isNotBlank(value)) {
                                predicateList.add(cb.like(root.get(code).as(String.class), "%"+ value +"%"));
                            }
                        }
                    }
                    if("equals".equals(mapKey)){
                        for(Map.Entry<String, Object> entry1 : mapValue.entrySet()){
                            String code = entry1.getKey();
                            String value = entry1.getValue().toString();
                            if (StringUtils.isNotBlank(code) && StringUtils.isNotBlank(value)) {
                                predicateList.add(cb.equal(root.get(code).as(String.class), value));
                            }
                        }
                    }
                    if(">=".equals(mapKey)){
                        for(Map.Entry<String, Object> entry1 : mapValue.entrySet()){
                            String code = entry1.getKey();
                            String value = entry1.getValue().toString();
                            if (StringUtils.isNotBlank(code) && StringUtils.isNotBlank(value)) {
                                predicateList.add(cb.greaterThanOrEqualTo(root.get(code).as(String.class), value));
                            }
                        }
                    }
                    if("<=".equals(mapKey)){
                        for(Map.Entry<String, Object> entry1 : mapValue.entrySet()){
                            String code = entry1.getKey();
                            String value = entry1.getValue().toString();
                            if (StringUtils.isNotBlank(code) && StringUtils.isNotBlank(value)) {
                                predicateList.add(cb.lessThanOrEqualTo(root.get(code).as(String.class), value));
                            }
                        }
                    }
                    if("or".equals(mapKey)){
                        List<Predicate> predicateListOr = new ArrayList<>();
                        for(Map.Entry<String, Object> entry1 : mapValue.entrySet()){
                            String code = entry1.getKey();
                            String value = entry1.getValue().toString();
                            if (StringUtils.isNotBlank(code) && StringUtils.isNotBlank(value)) {
                                //如果是同一个key 如：test = 1 or test = 2
                                if("theSameKey".equals(code)){
                                    Map<String, String[]> theSameMap = (Map<String, String[]>) entry1.getValue();
                                    for(Map.Entry<String, String[]> entry2 : theSameMap.entrySet()){
                                        String key = entry2.getKey();
                                        String[] values = entry2.getValue();
                                        for(int i=0; i<values.length; i++){
                                            predicateListOr.add(cb.equal(root.get(key).as(String.class),  values[i]));
                                        }
                                    }
                                }else {
                                    predicateListOr.add(cb.equal(root.get(code).as(String.class),  value));
                                }
                            }
                        }
                        predicateList.add(cb.or(predicateListOr.toArray(new Predicate[predicateListOr.size()])));
                    }
                }
                return cb.and( predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
        return sf;
    }
}
