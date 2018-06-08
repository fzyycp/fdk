package cn.faury.fdk.common.anotation.serialize;


import java.lang.annotation.*;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * JSON序列化标签
 */
@Documented
@Retention(RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface SerializeTag {
}
