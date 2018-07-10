/*##############################################################################
 # 基础类库：fdk-common
 #
 # @date 5/2018
 # @author faury
 #############################################################################*/
package cn.faury.fdk.common.utils;

import cn.faury.fdk.common.anotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * 路径工具
 * <p>
 * new File("..\path\abc.txt") 中的三个方法获取路径的方法 1： getPath() 获取相对路径，例如
 * ..\path\abc.txt 2： getAbslutlyPath() 获取绝对路径，但可能包含 ".." 或 "." 字符，例如
 * D:\otherPath\..\path\abc.txt 3： getCanonicalPath() 获取绝对路径，但不包含 ".." 或 "."
 * 字符，例如 D:\path\abc.txt
 */
public class PathUtil {

    private static Logger logger = LoggerFactory.getLogger(PathUtil.class);

    // Web环境根目录（一个JVM不会改变，获取一次即可）
    private static String webRootPath = StringUtil.EMPTY_STR;
    // 类根路径（一个JVM不会改变，获取一次即可）
    private static String classRootPath = StringUtil.EMPTY_STR;

    /**
     * 获取class所在路径
     *
     * @param clazz class类
     * @return 路径
     */
    public static String getPath(@NonNull Class clazz) {
        if (clazz == null) {
            return StringUtil.EMPTY_STR;
        }
        String path = clazz.getResource(StringUtil.EMPTY_STR).getPath();
        return new File(path).getAbsolutePath();
    }

    /**
     * 获取对象所在路径
     *
     * @param object 对象
     * @return 路径
     */
    public static String getPath(@NonNull Object object) {
        if (object == null) {
            return StringUtil.EMPTY_STR;
        }
        return getPath(object.getClass());
    }

    /**
     * 获取类加重器所在根目录
     *
     * @return 类根目录
     */
    public static String getClassRootPath() {
        logger.debug("classRootPath in memory is {}",webRootPath);
        if (StringUtil.isEmpty(classRootPath)) {
            try {
                String path = PathUtil.class.getClassLoader()
                        .getResource(StringUtil.EMPTY_STR).toURI().getPath();
                logger.debug("the / of classRootPath path is {}",path);
                classRootPath = new File(path).getAbsolutePath();
            } catch (Exception e) {
                String path = PathUtil.class.getClassLoader()
                        .getResource(StringUtil.EMPTY_STR).getPath();
                classRootPath = new File(path).getAbsolutePath();
            }
        }
        logger.debug("the result of classRootPath is {}",classRootPath);
        return classRootPath;
    }

    /**
     * 获取包路径
     *
     * @param object 对象实体
     * @return 包路径
     */
    public static String getPackagePath(@NonNull Object object) {
        if (object == null) {
            return StringUtil.EMPTY_STR;
        }
        Package p = object.getClass().getPackage();
        return p != null ? p.getName().replaceAll("\\.", "/") : StringUtil.EMPTY_STR;
    }

    /**
     * 获取Web环境的根目录
     * @return web根目录
     */
    public static String getWebRootPath() {
        logger.debug("webRootPath in memory is {}",webRootPath);
        if (StringUtil.isEmpty(webRootPath)) {
            try {
                String path = PathUtil.class.getResource("/").toURI().getPath();
                logger.debug("the / of webRootPath path is {}",path);
                webRootPath = new File(path).getParentFile().getParentFile()
                        .getCanonicalPath();
            } catch (Exception ignored) {
            }
        }
        logger.debug("the result of webRootPath is {}",webRootPath);
        return webRootPath;
    }
}
