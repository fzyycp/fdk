/*##############################################################################
 # 基础类库：fdk-common
 #
 # @date 6/2018
 # @author faury
 #############################################################################*/

package cn.faury.fdk.common.anotation.service;

import java.lang.annotation.*;


/**
 * 提供读、写服务
 */
@Target(value = { ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@CanReadOnly
@CanWriteOnly
public @interface CanReadWrite {

}
