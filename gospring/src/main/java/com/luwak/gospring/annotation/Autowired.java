package com.luwak.gospring.annotation;

import java.lang.annotation.*;

/**
 * @author wanggang
 * @date 2018年4月19日 上午10:00:28
 * 
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {
	
	String value() default "";
	
}
