package cn.faury.fdk.mobile.annotation;

import org.springframework.stereotype.Service;

import java.lang.annotation.*;

/**
 * 手机接口注解
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Service
public @interface IMobile {

	/**
	 * 方法名
	 * 
	 * @return 方法名
	 */
	String method();

	/**
	 * 是否需要权限验证，默认需要
	 * 
	 * @return 是否需要权限验证
	 */
	boolean isAuthc() default true;
}
