package cn.faury.fdk.mybatis;

import org.apache.ibatis.annotations.Mapper;

import java.lang.annotation.*;

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Mapper
public @interface AutoScannedMapper {
}
