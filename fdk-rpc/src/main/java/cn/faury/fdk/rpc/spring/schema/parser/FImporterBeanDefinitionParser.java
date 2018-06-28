package cn.faury.fdk.rpc.spring.schema.parser;

import cn.faury.fdk.common.utils.StringUtil;
import cn.faury.fdk.rpc.spring.schema.base.FBaseBeanDefinitionParser;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.remoting.caucho.HessianProxyFactoryBean;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;
import org.w3c.dom.Element;


/**
 * 服务引入解析器
 */
public class FImporterBeanDefinitionParser extends FBaseBeanDefinitionParser {

	/* (non-Javadoc)
	 * @see cn.wassk.framework.spring.schema.base.FBaseBeanDefinitionParser#doGetBeanClass(org.w3c.dom.Element)
	 */
	@Override
	protected Class<?> doGetBeanClass(Element element) {
		// 发布方式：HttpInvoke
		if ("spring".equals(element.getAttribute("protocol"))) {
			return HttpInvokerProxyFactoryBean.class;
		}
		// 默认采用Hessian方式
		return HessianProxyFactoryBean.class;
	}

	/* (non-Javadoc)
	 * @see cn.wassk.framework.spring.schema.base.FBaseBeanDefinitionParser#doSelfParse(org.w3c.dom.Element, org.springframework.beans.factory.support.BeanDefinitionBuilder)
	 */
	@Override
	protected void doSelfParse(Element element, BeanDefinitionBuilder bean) {
		String name = element.getAttribute("name");
		String url = element.getAttribute("url");
		String api = element.getAttribute("api");
		String overloadEnabled = element.getAttribute("overloadEnabled");
		// 如果未定义name，则默认使用url最后一级参数
		if (StringUtil.isEmpty(name) && StringUtil.isNotEmpty(url)) {
			String[] k = url.split("/");
			name = k[k.length - 1];
		}
		if (StringUtil.isEmpty(name, url, api)) {
			throw new IllegalArgumentException("Config attributes can't empty!");
		}
		element.setAttribute("name", name);
		bean.addPropertyValue("serviceUrl", url);
		bean.addPropertyValue("serviceInterface", api);
		if ("false".equalsIgnoreCase(overloadEnabled)) {
			bean.addPropertyValue("overloadEnabled", overloadEnabled);
		} else {
			bean.addPropertyValue("overloadEnabled", true);
		}
	}

}
