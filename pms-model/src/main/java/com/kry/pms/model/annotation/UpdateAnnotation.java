package com.kry.pms.model.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UpdateAnnotation {
    //传入参数只是为了给人方便查询，知道是哪条数据作出的修改举动
    String name() default "";//日志中需要插入的列名称
    String value() default "";//列的值
    String type() default "";//类型（RS：房间房态，GO：主单）
}
