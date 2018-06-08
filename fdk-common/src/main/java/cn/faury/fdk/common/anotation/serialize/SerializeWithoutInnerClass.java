/*##############################################################################
 # 基础类库：fdk-common
 #
 # @date 6/2018
 # @author faury
 #############################################################################*/

/*##############################################################################
 # 基础类库：fdk-common
 #
 # @date 5/2018
 # @author faury
 #############################################################################*/
package cn.faury.fdk.common.anotation.serialize;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * JSON序列化包含内部类值
 */
@Documented
@Retention(RUNTIME)
@Target({ElementType.TYPE})
@SerializeTag
public @interface SerializeWithoutInnerClass {
    boolean value() default true;
}
