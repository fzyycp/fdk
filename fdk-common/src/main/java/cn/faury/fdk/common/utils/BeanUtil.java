/*##############################################################################
 # 基础类库：fdk-common
 #
 # @date 5/2018
 # @author faury
 #############################################################################*/
package cn.faury.fdk.common.utils;

import cn.faury.fdk.common.anotation.NonNull;
import cn.faury.fdk.common.anotation.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

/**
 * Bean对象操作工具
 */
public class BeanUtil {
    private static final Logger logger = LoggerFactory.getLogger(BeanUtil.class);

    /**
     * 通过properties文件初始化字段值
     *
     * @param bean       实例对象
     * @param properties 字段值
     */
    public static <T> void initFields(@NonNull T bean, @NonNull PropertiesUtil properties) {
        if (bean != null && properties != null) {
            Field[] fields = bean.getClass().getDeclaredFields();
            Arrays.stream(fields).forEach(field -> {
                Properties prop = field.getAnnotation(Properties.class);
                if (prop != null && StringUtil.isNotEmpty(prop.key())) {
                    try {
                        Method setMethod = bean.getClass().getMethod("set" + StringUtil.firstCharToUpperCase(field.getName()), field.getType());
                        if (setMethod != null && setMethod.getModifiers() == Modifier.PUBLIC) {
                            if (field.getType().isAssignableFrom(Integer.class) || field.getType().isAssignableFrom(int.class)) {
                                setMethod.invoke(bean, properties.getPropertyToInt(prop.key(), Integer.parseInt(prop.value())));
                            } else if (field.getType().isAssignableFrom(Long.class) || field.getType().isAssignableFrom(long.class)) {
                                setMethod.invoke(bean, properties.getPropertyToLong(prop.key(), Long.parseLong(prop.value())));
                            } else if (field.getType().isAssignableFrom(Float.class) || field.getType().isAssignableFrom(float.class)) {
                                setMethod.invoke(bean, properties.getPropertyToFloat(prop.key(), Float.parseFloat(prop.value())));
                            } else if (field.getType().isAssignableFrom(Double.class) || field.getType().isAssignableFrom(double.class)) {
                                setMethod.invoke(bean, properties.getPropertyToDouble(prop.key(), Double.parseDouble(prop.value())));
                            } else if (field.getType().isAssignableFrom(Boolean.class) || field.getType().isAssignableFrom(boolean.class)) {
                                setMethod.invoke(bean, properties.getPropertyToBoolean(prop.key(), StringUtil.isEmpty(prop.value()) ? null : Boolean.parseBoolean(prop.value())));
                            } else if (field.getType().isAssignableFrom(String.class)) {
                                setMethod.invoke(bean, properties.getProperty(prop.key(), prop.value()));
                            }
                        }
                    } catch (Exception e) {
                        if (logger.isTraceEnabled()) {
                            logger.trace(e.getMessage(), e);
                        }
                    }
                }
            });
        }
    }
}
