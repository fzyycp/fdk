/*##############################################################################
 # 基础类库：fdk-common
 #
 # @date 5/2018
 # @author faury
 #############################################################################*/
package cn.faury.fdk.common.anotation.serialize;

import cn.faury.fdk.common.utils.DateUtil;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * JSON序列化包含日期值
 */
@Documented
@Retention(RUNTIME)
@Target({ElementType.TYPE})
@SerializeTag
public @interface SerializeDate {
    String value() default DateUtil.FORMAT_DATE_TIME;
}
