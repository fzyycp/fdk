/*##############################################################################
 # 基础类库：fdk-common
 #
 # @date 5/2018
 # @author faury
 #############################################################################*/
package cn.faury.fdk.common.utils;

import cn.faury.fdk.common.anotation.NonNull;

import java.io.*;

/**
 * 序列化工具
 */
public class SerializeUtil {
    /**
     * 对象序列化
     *
     * @param object 对象
     * @return 序列化结果
     */
    public static byte[] serialize(@NonNull Object object) {
        byte[] retByte = null;
        if (object != null) {
            ByteArrayOutputStream baos = null;
            ObjectOutputStream oos = null;
            try {
                // 序列化
                baos = new ByteArrayOutputStream();
                oos = new ObjectOutputStream(baos);
                oos.writeObject(object);
                retByte = baos.toByteArray();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (baos != null) {
                        baos.flush();
                    }
                    if (oos != null) {
                        oos.flush();
                        oos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return retByte;
    }

    /**
     * 反序列化
     *
     * @param bytes 字节
     * @param clazz 类对象
     * @param <T>   模板类
     * @return 对象实例
     */
    public static <T> T deserialize(byte[] bytes, Class<T> clazz) {
        T retT = null;
        if (bytes != null && 0 < bytes.length) {
            ByteArrayInputStream bais = null;
            ObjectInputStream ois = null;
            try {
                // 反序列化
                bais = new ByteArrayInputStream(bytes);
                ois = new ObjectInputStream(bais);
                retT = (T) ois.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bais != null) {
                        bais.close();
                    }
                    if (ois != null) {
                        ois.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return retT;
    }
}