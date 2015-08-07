package org.tangerine.apiresolver.core.mapping;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;


public class SimpleApiMapping extends ApiMapping {

	@Override
	public Map<String, MappingObject> resolveApiMappings() {
		Map<String, MappingObject> mappingMap = new HashMap<String, MappingObject>();
		Map<String, ApiExportor> apiMappings =  getApplicationContext().getBeansOfType(ApiExportor.class, false, true);
		
		for (ApiExportor apiExportor : apiMappings.values()) {
			for (Method method : getMappingMethod(apiExportor.getClass())) {
				String mapping = getMappingName(apiExportor.getClass(), method);
				mappingMap.put(mapping, new MappingObject(mapping, apiExportor, method));
			}
		}
		
		return mappingMap;
	}
	
	@Override
	public String getMappingName(Class<?> clz, Method method) {
		StringBuffer mapping = new StringBuffer();
		mapping.append(StringUtils.uncapitalize(clz.getSimpleName()).replaceAll("Service|Api|API|Exportor", ""));
		mapping.append(".").append(method.getName());
		return mapping.toString();
	}
	
	@Override
	public List<Method> getMappingMethod(Class<?> clz) {
		
		List<Method> suffixs = new ArrayList<Method>();
		
		for (Method method : clz.getDeclaredMethods()) {
			if (method.getModifiers() == Modifier.PUBLIC) {
				suffixs.add(method);
			}
		}
		
		return suffixs;
	}
}
