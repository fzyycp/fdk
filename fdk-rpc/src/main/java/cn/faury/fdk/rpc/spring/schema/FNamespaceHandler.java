package cn.faury.fdk.rpc.spring.schema;

import cn.faury.fdk.rpc.spring.schema.base.FBaseNamespaceHandlerSupport;
import cn.faury.fdk.rpc.spring.schema.parser.FExporterBeanDefinitionParser;
import cn.faury.fdk.rpc.spring.schema.parser.FImporterBeanDefinitionParser;


/**
 * 服务导出命名空间解析器
 *
 * Created by faury.
 *
 */
public class FNamespaceHandler extends FBaseNamespaceHandlerSupport {

	/**
	 *
	 */
	@Override
	public void doInit() {
		this.registerBeanDefinitionParser("exporter", new FExporterBeanDefinitionParser());
		this.registerBeanDefinitionParser("importer", new FImporterBeanDefinitionParser());
	}

}
