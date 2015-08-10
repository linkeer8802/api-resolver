package org.tangerine.apiresolver.core.mapping;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;


public class AnnotationApiMapping extends ApiMapping {

	@Override
	public Map<String, MappingObject> resolveApiMappings() {
		Map<String, MappingObject> mappingMap = new HashMap<String, MappingObject>();
		Map<String, Object> apiMappings =  getApplicationContext()
				.getBeansWithAnnotation(org.tangerine.apiresolver.annotation.ApiMapping.class);
		
		for (Object apiExportor : apiMappings.values()) {
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
		String mappingPrefix = clz.getAnnotation(org.tangerine.apiresolver.annotation.ApiMapping.class).value();
		if (!StringUtils.isEmpty(mappingPrefix)) {
			mapping.append(mappingPrefix).append(".");
		}
//		System.out.println("=================================");
//		System.out.println("=================================" + method);
//		System.out.println("=================================" + method.toGenericString());
		mapping.append(method.getAnnotation(org.tangerine.apiresolver.annotation.ApiMapping.class).value());
		String version = clz.getAnnotation(org.tangerine.apiresolver.annotation.ApiMapping.class).version();
		if (StringUtils.isEmpty(version)) {
			version = method.getAnnotation(org.tangerine.apiresolver.annotation.ApiMapping.class).version();
		}
		if (!StringUtils.isEmpty(version)) {
			mapping.append(".v").append(version.replace(".", "_"));
		}
		return mapping.toString();
	}
	
	@Override
	public List<Method> getMappingMethod(Class<?> clz) {
		List<Method> suffixs = new ArrayList<Method>();
		
		for (Method method : clz.getDeclaredMethods()) {
			if (method.getAnnotation(org.tangerine.apiresolver.annotation.ApiMapping.class) != null) {
				suffixs.add(method);
			}
		}
		
		return suffixs;
	}
}
