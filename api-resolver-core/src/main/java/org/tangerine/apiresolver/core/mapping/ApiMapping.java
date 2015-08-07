package org.tangerine.apiresolver.core.mapping;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public abstract class ApiMapping implements ApplicationContextAware {

	private ApplicationContext applicationContext;
	
	public abstract String getMappingName(Class<?> clz, Method method);
	
	public abstract List<Method> getMappingMethod(Class<?> clz);
	
	public abstract Map<String, MappingObject> resolveApiMappings();
	
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
