package cn.faury.fdk.rpc.spring.schema.base;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;

/**
 * 基础Bean解析器
 */
public abstract class FBaseBeanDefinitionParser extends
		AbstractSingleBeanDefinitionParser {

	/**
	 * 获取当前Bean的类
	 * 
	 * @param element
	 *            xml配置节点
	 * @return 类类型
	 */
	protected abstract Class<?> doGetBeanClass(Element element);
	
	/**
	 * 加载业务的转换
	 * @param element xml配置对象
	 * @param bean bean构造器
	 */
	protected abstract void doSelfParse(Element element, BeanDefinitionBuilder bean);
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser
	 * #getBeanClass(org.w3c.dom.Element)
	 */
	@Override
	protected Class<?> getBeanClass(Element element) {
		return doGetBeanClass(element);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser
	 * #getBeanClassName(org.w3c.dom.Element)
	 */
	@Override
	protected String getBeanClassName(Element element) {
		return getBeanClass(element).getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser
	 * #doParse(org.w3c.dom.Element,
	 * org.springframework.beans.factory.support.BeanDefinitionBuilder)
	 */
	@Override
	protected void doParse(Element element, BeanDefinitionBuilder bean) {
		doSelfParse(element, bean);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.xml.AbstractBeanDefinitionParser#
	 * shouldGenerateId()
	 */
	@Override
	protected boolean shouldGenerateId() {
		return true;
	}

}
