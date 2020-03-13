package com.kry.pms.model.annotation;

public @interface Log {
    public String operationName() default "";
}
