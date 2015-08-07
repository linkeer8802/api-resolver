//package org.tangerine.apiresolver;
//
//import org.springframework.beans.factory.support.DefaultListableBeanFactory;
//import org.springframework.util.StringUtils;
//import org.tangerine.apiresolver.core.AbstractApiResolver;
//import org.tangerine.apiresolver.support.HessianProxyFactory;
//
//public class HessianApiResolver extends AbstractApiResolver {
//
//	private String baseRemoteUrl;
//	
//	@Override
//	public Object getApiBean(String serviceName, String interfaceName) throws Exception {
//		
//		Object target = null;
//		HessianProxyFactory hessianProxyFactoryBean = null;
//		String beanName = "Remote-Proxy-Bean-" + StringUtils.capitalize(serviceName);
//		
//	    if (!beanFactory.containsBean(beanName)) {  
//			hessianProxyFactoryBean = new HessianProxyFactory();
//			hessianProxyFactoryBean.setServiceUrl(getBaseRemoteUrl() + "/" + serviceName); 
//			hessianProxyFactoryBean.setServiceInterface(getApiClass(interfaceName));
//			hessianProxyFactoryBean.afterPropertiesSet();
//	        target = hessianProxyFactoryBean.getObject();
//	        ((DefaultListableBeanFactory)beanFactory).registerSingleton(beanName, target);
//	        
//	     } else {
//	    	 target = beanFactory.getBean(beanName);
//	     } 
//	    
//		return target;
//	}
//
//	public String getBaseRemoteUrl() {
//		return baseRemoteUrl;
//	}
//
//	public void setBaseRemoteUrl(String baseRemoteUrl) {
//		this.baseRemoteUrl = baseRemoteUrl;
//	}
//}
