package com.luwak.gospring.annotation;

import java.lang.annotation.*;

/**
 * @author wanggang
 * @date 2018年4月19日 上午10:04:58
 * 
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Service {
	
	String value() default "";
	
}
