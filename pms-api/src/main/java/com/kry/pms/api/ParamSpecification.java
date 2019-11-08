package com.kry.pms.api;

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
    private Specification<T> createSpecification(Map<String, Object> map) {
        Specification<T> sf = new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<>();
                for(Map.Entry<String, Object> entry : map.entrySet()){
                    String mapKey = entry.getKey();
                    Map<String, String> mapValue = (Map<String, String>) entry.getValue();
                    if("like".equals(mapKey)){
                        for(Map.Entry<String, String> entry1 : mapValue.entrySet()){
                            String code = entry1.getKey();
                            String value = entry1.getValue();
                            if (StringUtils.isNotBlank(code) && StringUtils.isNotBlank(value)) {
                                predicateList.add(cb.like(root.get(code).as(String.class), "%"+ value +"%"));
                            }
                        }
                    }
                    if("equals".equals(mapKey)){
                        for(Map.Entry<String, String> entry1 : mapValue.entrySet()){
                            String code = entry1.getKey();
                            String value = entry1.getValue();
                            if (StringUtils.isNotBlank(code) && StringUtils.isNotBlank(value)) {
                                predicateList.add(cb.equal(root.get(code).as(String.class), value));
                            }
                        }
                    }
                    if("or".equals(mapKey)){
                        List<Predicate> predicateListOr = new ArrayList<>();
                        for(Map.Entry<String, String> entry1 : mapValue.entrySet()){
                            String code = entry1.getKey();
                            String value = entry1.getValue();
                            if (StringUtils.isNotBlank(code) && StringUtils.isNotBlank(value)) {
                                predicateListOr.add(cb.equal(root.get(code).as(String.class),  value));
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
