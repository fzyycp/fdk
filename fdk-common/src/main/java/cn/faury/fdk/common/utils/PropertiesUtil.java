/*##############################################################################
 # 基础类库：fdk-common
 #
 # @date 5/2018
 # @author faury
 #############################################################################*/
package cn.faury.fdk.common.utils;

import cn.faury.fdk.common.anotation.NonNull;
import cn.faury.fdk.common.anotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * properties工具
 */
public class PropertiesUtil {

    // 日志记录器
    private static final Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);
    // 保存配置属性
    private ConcurrentMap<String, Object> properties = new ConcurrentHashMap<>();

    private static PropertiesUtil _ins;

    /**
     * 私有构造函数
     */
    private PropertiesUtil() {
    }

    /**
     * 根据property文件创建配置对象
     *
     * @param file 文件
     * @return 配置对象
     */
    public static PropertiesUtil createPropertyInstance(@NonNull File file) {
        if (logger.isTraceEnabled()) {
            logger.trace("createPropertyInstance from file=" + file);
        }
        PropertiesUtil property = new PropertiesUtil();
        if (file==null || !file.exists()) {
            throw new IllegalArgumentException(
                    "Parameter of file can not be empty");
        }
        property.loadPropertyFile(file);
        return property;
    }

    /**
     * 获取默认实例
     * @return 单一实例
     */
    public static PropertiesUtil instance() {
        AssertUtil.assertNotNull(_ins,"Properties未初始化");
        return _ins;
    }

    /**
     * 初始化
     *
     * @param file 文件名
     */
    public static void init(@NonNull File file){
        _ins = new PropertiesUtil();
        _ins.loadPropertyFile(file);
    }

    /**
     * 加载配置文件
     *
     * @param file 文件路径
     */
    private void loadPropertyFile(@NonNull File file) {
        Properties property = new Properties();

        if (file == null) {
            throw new IllegalArgumentException(
                    "Parameter of file can not be empty");
        }
        if (!file.exists()) {
            throw new IllegalArgumentException(
                    "Parameter of file is not exists:" + file.getAbsolutePath());
        }
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            property.load(inputStream);
        } catch (Exception eOne) {

            try {
                ClassLoader loader = Thread.currentThread()
                        .getContextClassLoader();
                property.load(loader.getResourceAsStream(file.getAbsolutePath()));
            } catch (IOException eTwo) {
                throw new IllegalArgumentException(
                        "Properties file loading failed: " + eTwo.getMessage());
            }
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
            } catch (IOException ignored) {
            }
        }

        for (Entry<Object, Object> entry : property.entrySet()) {
            this.properties.put(entry.getKey().toString(), entry.getValue());
        }
    }

    /**
     * 获取属性值
     *
     * @param key 属性key
     * @return 属性value
     */
    public String getProperty(@NonNull String key) {
        checkPropertyLoading();
        if (this.properties.containsKey(key)) {
            return properties.get(key).toString();
        }
        return null;
    }

    /**
     * 获取属性值
     *
     * @param key          属性key
     * @param defaultValue 默认值
     * @return 属性value
     */
    public String getProperty(@NonNull String key, @Nullable String defaultValue) {
        String resultStr = this.getProperty(key);
        return resultStr != null ? resultStr : defaultValue;

    }

    /**
     * 获取属性值并转化为Integer型
     *
     * @param key 属性key
     * @return 属性value
     */
    public Integer getPropertyToInt(@NonNull String key) {
        checkPropertyLoading();
        Integer resultInt = null;
        String resultStr = this.getProperty(key);
        if (StringUtil.isNotEmpty(resultStr)) {
            try {
                resultInt = Integer.parseInt(resultStr);
            } catch (Exception e) {
                if (logger.isTraceEnabled()) {
                    logger.trace(e.getMessage(), e);
                }
            }
        }
        return resultInt;
    }

    /**
     * 获取属性值并转化为Integer型
     *
     * @param key          属性key
     * @param defaultValue 默认值
     * @return 属性value
     */
    public Integer getPropertyToInt(@NonNull String key, @Nullable Integer defaultValue) {
        Integer result = getPropertyToInt(key);
        return result != null ? result : defaultValue;
    }

    /**
     * 获取属性值并转化为Long型
     *
     * @param key 属性key
     * @return 属性value
     */
    public Long getPropertyToLong(@NonNull String key) {
        checkPropertyLoading();
        Long resultInt = null;
        String resultStr = this.getProperty(key);
        if (StringUtil.isNotEmpty(resultStr)) {
            try {
                resultInt = Long.parseLong(resultStr);
            } catch (Exception e) {
                if (logger.isTraceEnabled()) {
                    logger.trace(e.getMessage(), e);
                }
            }
        }
        return resultInt;
    }

    /**
     * 获取属性值并转化为Long型
     *
     * @param key          属性key
     * @param defaultValue 默认值
     * @return 属性value
     */
    public Long getPropertyToLong(@NonNull String key, @Nullable Long defaultValue) {
        Long result = getPropertyToLong(key);
        return result != null ? result : defaultValue;
    }

    /**
     * 获取属性值并转化为Boolean型
     *
     * @param key 属性key
     * @return 属性value
     */
    public Boolean getPropertyToBoolean(@NonNull String key) {
        String resultStr = this.getProperty(key);
        Boolean resultBool = null;
        if (StringUtil.isNotEmpty(resultStr)) {
            resultStr = resultStr.trim();
            if (resultStr.equalsIgnoreCase("true"))
                resultBool = Boolean.TRUE;
            else if (resultStr.equalsIgnoreCase("false"))
                resultBool = Boolean.FALSE;
        }
        return resultBool;

    }

    /**
     * 获取属性值并转化为Boolean型
     *
     * @param key          属性key
     * @param defaultValue 默认值
     * @return 属性value
     */
    public Boolean getPropertyToBoolean(@NonNull String key, @Nullable Boolean defaultValue) {
        Boolean result = getPropertyToBoolean(key);
        return result != null ? result : defaultValue;
    }

    /**
     * 获取属性值并转化为Float型
     *
     * @param key 属性key
     * @return 属性value
     */
    public Float getPropertyToFloat(@NonNull String key) {
        String resultStr = this.getProperty(key);
        Float result = null;
        if (StringUtil.isNotEmpty(resultStr)) {
            try {
                result = Float.parseFloat(resultStr);
            } catch (Exception e) {
                if (logger.isTraceEnabled()) {
                    logger.trace(e.getMessage(), e);
                }
            }
        }
        return result;

    }

    /**
     * 获取属性值并转化为Float型
     *
     * @param key          属性key
     * @param defaultValue 默认值
     * @return 属性value
     */
    public Float getPropertyToFloat(@NonNull String key, @Nullable Float defaultValue) {
        Float result = getPropertyToFloat(key);
        return result != null ? result : defaultValue;
    }

    /**
     * 获取属性值并转化为Double型
     *
     * @param key 属性key
     * @return 属性value
     */
    public Double getPropertyToDouble(@NonNull String key) {
        String resultStr = this.getProperty(key);
        Double result = null;
        if (StringUtil.isNotEmpty(resultStr)) {
            try {
                result = Double.parseDouble(resultStr);
            } catch (Exception e) {
                if (logger.isTraceEnabled()) {
                    logger.trace(e.getMessage(), e);
                }
            }
        }
        return result;

    }

    /**
     * 获取属性值并转化为Double型
     *
     * @param key          属性key
     * @param defaultValue 默认值
     * @return 属性value
     */
    public Double getPropertyToDouble(@NonNull String key, @Nullable Double defaultValue) {
        Double result = getPropertyToDouble(key);
        return result != null ? result : defaultValue;
    }

    /**
     * 获取所有Key集合
     *
     * @return Key集合
     */
    public Set<String> getPropertyKeySet() {
        checkPropertyLoading();
        return properties.keySet();
    }

    /**
     * 检查property是否加载
     */
    private void checkPropertyLoading() {
        if (properties == null) {
            throw new IllegalArgumentException(
                    "You must load properties file by invoking loadPropertyFile(String) method in configConstant(Constants) method before.");
        }
    }

    @Override
    public String toString() {
        return JsonUtil.objectToJson(this);
    }
}
