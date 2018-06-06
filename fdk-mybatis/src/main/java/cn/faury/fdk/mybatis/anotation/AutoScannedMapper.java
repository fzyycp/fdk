package cn.faury.fdk.mybatis.anotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * 被Spring自动扫描注入到Mapper中注解标识
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface AutoScannedMapper {
}
