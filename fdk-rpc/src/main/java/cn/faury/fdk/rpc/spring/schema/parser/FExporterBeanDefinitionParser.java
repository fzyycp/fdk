
package cn.faury.fdk.rpc.spring.schema.parser;

import cn.faury.fdk.common.utils.StringUtil;
import cn.faury.fdk.rpc.spring.schema.base.FBaseBeanDefinitionParser;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.remoting.caucho.HessianServiceExporter;
import org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter;
import org.w3c.dom.Element;

/**
 * 服务导出Bean解析器
 */
public class FExporterBeanDefinitionParser extends FBaseBeanDefinitionParser {

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.wassk.framework.spring.schema.base.FBaseBeanDefinitionParser#
	 * doGetBeanClass(org.w3c.dom.Element)
	 */
	@Override
	protected Class<?> doGetBeanClass(Element element) {
		// 发布方式：HttpInvoke
		if ("spring".equals(element.getAttribute("protocol"))) {
			return HttpInvokerServiceExporter.class;
		}
		// 默认采用Hessian方式
		return HessianServiceExporter.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cn.wassk.framework.spring.schema.base.FBaseBeanDefinitionParser#doSelfParse
	 * (org.w3c.dom.Element,
	 * org.springframework.beans.factory.support.BeanDefinitionBuilder)
	 */
	@Override
	protected void doSelfParse(Element element, BeanDefinitionBuilder bean) {
		String name = element.getAttribute("name");
		String provider = element.getAttribute("provider");
		String api = element.getAttribute("api");
		// 如果未定义name，则默认使用provider前补"/"
		if (StringUtil.isEmpty(name)) {
			name = "/" + provider;
		}
		if (StringUtil.isEmpty(name, provider, api)) {
			throw new IllegalArgumentException("Config attributes can't empty!");
		}
		element.setAttribute("name", name);
		bean.addPropertyReference("service", provider);
		bean.addPropertyValue("serviceInterface", api);
	}

}
