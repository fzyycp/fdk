/*##############################################################################
 # 基础类库：fdk-common
 #
 # @date 5/2018
 # @author faury
 #############################################################################*/
package cn.faury.fdk.common.utils;

import cn.faury.fdk.common.anotation.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.*;

/**
 * JSON工具
 */
public class JsonUtil {

    /**
     * 私有构造函数
     */
    protected JsonUtil() {
    }

    /**
     * 对象转换为JSON字符串
     *
     * @param object 被转换对象
     * @return json字符串
     */
    public static String objectToJson(@Nullable Object object) {
        GsonBuilder builder = new GsonBuilder();
        // 序列化null值
        SerializeNulls sn = object.getClass().getAnnotation(SerializeNulls.class);
        if (sn != null && sn.value()) {
            builder.serializeNulls();
        }
        // 日期格式化
        SerializeDate sd = object.getClass().getAnnotation(SerializeDate.class);
        if (sd != null && StringUtil.isNotEmpty(sd.value())) {
            builder.setDateFormat(sd.value());
        } else {
            builder.setDateFormat(DateUtil.FORMAT_DATE_TIME);
        }
        // 启用只注解有Expose的字段
        SerializeExpose se = object.getClass().getAnnotation(SerializeExpose.class);
        if (se != null && se.value()) {
            builder.excludeFieldsWithoutExposeAnnotation();
        }
        // 转义HTML
        SerializeHtmlEscaping she = object.getClass().getAnnotation(SerializeHtmlEscaping.class);
        if (she == null || !she.value()) {
            builder.disableHtmlEscaping();
        }
        // 禁止序列化内部类
        SerializeWithoutInnerClass sic = object.getClass().getAnnotation(SerializeWithoutInnerClass.class);
        if (sic != null && sic.value()) {
            builder.disableInnerClassSerialization();
        }
        return builder.create().toJson(object);
    }

    /**
     * json字符串转对象
     *
     * @param json json字符串
     * @param type 对象类型
     * @return 对象实体
     */
    public static <T> T jsonToObject(String json, Type type) {
        Gson gson = new Gson();
        return gson.fromJson(json, type);
    }

    /**
     * json字符串转对象
     *
     * @param json     json字符串
     * @param classOfT 对象类
     * @param <T>      类型定义
     * @return 对象实体
     */
    public static <T> T jsonToObject(String json, Class<T> classOfT) {
        Gson gson = new Gson();
        return gson.fromJson(json, classOfT);
    }

    /**
     * json字符串转Bean对象
     *
     * @param json json字符串
     * @return 对象实体
     */
    public static Map<String, Object> jsonToMap(String json) {
        Type type = new TypeToken<Map<String, Object>>() {
        }.getType();
        return jsonToObject(json, type);
    }

    /**
     * 根据Map对象获取对象
     *
     * @param map      map数据
     * @param classOfT 目标对象参数
     * @param <T>      对象类型
     * @return 对象实体
     */
    public static <T> T mapToObject(Map<String, Object> map, Class<T> classOfT) {
        String json = objectToJson(map);
        return jsonToObject(json, classOfT);
    }
}
