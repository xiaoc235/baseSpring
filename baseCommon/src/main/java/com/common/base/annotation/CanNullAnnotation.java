package com.common.base.annotation;

import java.lang.annotation.*;

/**
 * Created by jianghaoming on 2016/11/3.15:50
 *  true 可以为空
 *  false 不能为空
 *
 *  returnMessage 返回的信息， 默认可以为空
 *
 */
@Target(ElementType.FIELD)
@Inherited
@Retention(RetentionPolicy.RUNTIME )
public @interface CanNullAnnotation {

    public boolean isCanNull() default true;

    public String returnMessage() default "";
}
