/*##############################################################################
 # 基础类库：fdk-common
 #
 # @date 6/2018
 # @author faury
 #############################################################################*/

package cn.faury.fdk.common.anotation.permission;

import java.lang.annotation.*;


/**
 * 写权限
 */
@Target(value = {ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Write {

    /**
     * 对象值
     * @return 对象值
     */
    public String value() default "";

    /**
     * 描述
     * @return
     */
    public String description() default "";

}
