package com.luwak.gospring.annotation;

import java.lang.annotation.*;

/**
 * @author wanggang
 * @date 2018年4月19日 上午10:03:34
 * 
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {

	String value() default "";
	
}
