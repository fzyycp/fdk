/*##############################################################################
 # 基础类库：fdk-common
 #
 # @date 5/2018
 # @author faury
 #############################################################################*/
package cn.faury.fdk.common.utils;

import cn.faury.fdk.common.anotation.Nullable;
import cn.faury.fdk.common.anotation.serialize.*;
import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Map;

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
     * @param object                     被转换对象
     * @param serializeWithoutNulls      不序列化null对象
     * @param serializeDateFormat        日期格式
     * @param serializeExpose            只序列化有Expose注解的字段
     * @param serializeHtmlEscaping      转义HTML
     * @param serializeWithoutInnerClass 不序列化内部类
     * @return json字符串
     */
    public static String objectToJson(@Nullable Object object, boolean serializeWithoutNulls, String serializeDateFormat
            , boolean serializeExpose, boolean serializeHtmlEscaping, boolean serializeWithoutInnerClass) {
        GsonBuilder builder = new GsonBuilder();
        // 序列化null值
        if (!serializeWithoutNulls) {
            builder.serializeNulls();
        }
        // 日期格式化
        builder.setDateFormat(StringUtil.emptyDefault(serializeDateFormat, DateUtil.FORMAT_DATE_TIME));
        // 启用只注解有Expose的字段
        if (serializeExpose) {
            builder.excludeFieldsWithoutExposeAnnotation();
        }
        // 转义HTML
        if (!serializeHtmlEscaping) {
            builder.disableHtmlEscaping();
        }
        // 禁止序列化内部类
        if (serializeWithoutInnerClass) {
            builder.disableInnerClassSerialization();
        }

        return builder.create().toJson(object);
    }

    /**
     * 对象转换为JSON字符串
     *
     * @param object  被转换对象
     * @param configs 序列化配置
     * @return json字符串
     */
    public static String objectToJson(@Nullable Object object, @Nullable Annotation[] configs) {
        GsonBuilder builder = new GsonBuilder();
        builder.setDateFormat(DateUtil.FORMAT_DATE_TIME);
        final boolean[] withoutNulls = {false};
        Arrays.stream(configs).forEach(annotation -> {
            if (annotation instanceof SerializeWithoutNulls && ((SerializeWithoutNulls) annotation).value()) {// 序列化null值
                withoutNulls[0] = true;
            } else if (annotation instanceof SerializeDate) {// 日期格式化
                builder.setDateFormat(StringUtil.emptyDefault(((SerializeDate) annotation).value(), DateUtil.FORMAT_DATE_TIME));
            } else if (annotation instanceof SerializeExpose && ((SerializeExpose) annotation).value()) {// 启用只注解有Expose的字段
                builder.excludeFieldsWithoutExposeAnnotation();
            } else if (annotation instanceof SerializeHtmlEscaping && !((SerializeHtmlEscaping) annotation).value()) {// 转义HTML
                builder.disableHtmlEscaping();
            } else if (annotation instanceof SerializeWithoutInnerClass && ((SerializeWithoutInnerClass) annotation).value()) {// 禁止序列化内部类
                builder.disableInnerClassSerialization();
            }
        });

        if (!withoutNulls[0]) {
            builder.serializeNulls();
        }

        return builder.create().toJson(object);
    }

    /**
     * 对象转换为JSON字符串
     *
     * @param object 被转换对象
     * @return json字符串
     */
    public static String objectToJson(@Nullable Object object) {
        return objectToJson(object, object.getClass().getAnnotations());
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
        return JSON.parseObject(json);
//        Type type = new TypeToken<TreeMap<String, Object>>() {
//        }.getType();
////        Gson gson = new Gson();
//        Gson gson = new GsonBuilder()
//                .registerTypeAdapter(Double.class, new JsonDeserializer<Double>() {
//                    @Override
//                    public Double deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
//                        return null;
//                    }
//                })
//                .registerTypeAdapter(Number.class, new JsonDeserializer<Number>() {
//                    @Override
//                    public Number deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
//                        return null;
//                    }
//                })
////                .registerTypeAdapterFactory(new MapTypeAdapterFactory())
////                .registerTypeAdapter(type, (JsonDeserializer<TreeMap<String, Object>>) (jsonElement, type1, jsonDeserializationContext) -> {
////                    TreeMap<String, Object> treeMap = new TreeMap<>();
////                    JsonObject jsonObject = jsonElement.getAsJsonObject();
////                    Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
////                    for (Map.Entry<String, JsonElement> entry : entrySet) {
////                        JsonElement value = entry.getValue();
////                        if (value.isJsonPrimitive()) {
////                            if (((JsonPrimitive) value).isBoolean()) {
////                                treeMap.put(entry.getKey(), value.getAsBoolean());
////                            } else if (((JsonPrimitive) value).isNumber()) {
////                                Number number = value.getAsNumber();
////                                if (number.longValue() == number.doubleValue()) {
////                                    treeMap.put(entry.getKey(), value.getAsLong());
////                                } else {
////                                    treeMap.put(entry.getKey(), value.getAsNumber());
////                                }
////                            } else if (((JsonPrimitive) value).isString()) {
////                                treeMap.put(entry.getKey(), value.getAsString());
////                            } else {
////                                treeMap.put(entry.getKey(), value.getAsString());
////                            }
////                        } else {
////                            treeMap.put(entry.getKey(), value);
////                        }
////                    }
////                    return treeMap;
////                })
//                .create();
//        return gson.fromJson(json, type);
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
