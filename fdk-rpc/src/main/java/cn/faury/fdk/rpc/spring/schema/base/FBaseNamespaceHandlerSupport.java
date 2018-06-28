package cn.faury.fdk.rpc.spring.schema.base;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * 基础命名空间处理器
 */
public abstract class FBaseNamespaceHandlerSupport extends NamespaceHandlerSupport {

    /**
     * 执行初始化
     */
    public abstract void doInit();

    /* (non-Javadoc)
     * @see org.springframework.beans.factory.xml.NamespaceHandler#init()
     */
    @Override
    public void init() {
        doInit();
    }

}
