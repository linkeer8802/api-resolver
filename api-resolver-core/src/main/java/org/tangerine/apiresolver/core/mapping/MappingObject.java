package org.tangerine.apiresolver.core.mapping;

import java.lang.reflect.Method;

public class MappingObject {

	private String mapping;
	
	private Object target;
	
	private Method targetApi;

	public MappingObject(String mapping, Object target, Method targetApi) {
		this.mapping = mapping;
		this.target = target;
		this.targetApi = targetApi;
	}
	
	public String getMapping() {
		return mapping;
	}

	public void setMapping(String mapping) {
		this.mapping = mapping;
	}

	public Object getTarget() {
		return target;
	}

	public void setTarget(Object target) {
		this.target = target;
	}

	public Method getTargetApi() {
		return targetApi;
	}

	public void setTargetApi(Method targetApi) {
		this.targetApi = targetApi;
	}
}
