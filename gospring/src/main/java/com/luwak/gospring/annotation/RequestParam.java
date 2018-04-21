package com.luwak.gospring.annotation;

import java.lang.annotation.*;

/**
 * @author wanggang
 * @date 2018年4月19日 上午10:06:31
 * 
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestParam {
	
	String value() default "";

}
