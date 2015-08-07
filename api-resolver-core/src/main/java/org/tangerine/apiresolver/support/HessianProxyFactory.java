package org.tangerine.apiresolver.support;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.remoting.caucho.HessianClientInterceptor;

public class HessianProxyFactory extends HessianClientInterceptor {
	
	private Object serviceProxy;


	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		this.serviceProxy = new ProxyFactory(getServiceInterface(), this).getProxy(getBeanClassLoader());
	}

	public Object getObject() {
		return this.serviceProxy;
	}

	public Class<?> getObjectType() {
		return getServiceInterface();
	}
}
