//package org.tangerine.apiresolver;
//
//import org.springframework.beans.factory.support.DefaultListableBeanFactory;
//import org.springframework.util.StringUtils;
//import org.tangerine.apiresolver.core.AbstractApiResolver;
//
//import com.alibaba.dubbo.config.ApplicationConfig;
//import com.alibaba.dubbo.config.ReferenceConfig;
//import com.alibaba.dubbo.config.RegistryConfig;
//
//public class DubboApiResolver extends AbstractApiResolver {
//
//	@Override
//	public Object getApiBean(String serviceName, String interfaceName) throws Exception {
//		
//		ReferenceConfig<?> reference = null;
//		String beanName = "Dubbo-Proxy-Bean-" + StringUtils.capitalize(serviceName);
//		
//	    if (!beanFactory.containsBean(beanName)) {  
//	    	reference = new ReferenceConfig<Object>();
//			reference.setApplication(beanFactory.getBean(ApplicationConfig.class));
//			reference.setRegistry(beanFactory.getBean(RegistryConfig.class));
//			reference.setInterface(interfaceName);
//			((DefaultListableBeanFactory)beanFactory).registerSingleton(beanName, reference);
//	        
//	     } else {
//	    	 reference = beanFactory.getBean(beanName, ReferenceConfig.class);
//	     } 
//		return reference.get();
//	}
//}
