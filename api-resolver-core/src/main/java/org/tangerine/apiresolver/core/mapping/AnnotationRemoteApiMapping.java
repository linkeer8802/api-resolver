//package org.tangerine.apiresolver.core.mapping;
//
//import java.lang.reflect.Method;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//
//public class AnnotationRemoteApiMapping extends ApiMapping {
//
//	@Override
//	public Map<String, MappingObject> resolveApiMappings() {
//		Map<String, MappingObject> mappingMap = new HashMap<String, MappingObject>();
//		Map<String, Object> apiMappings =  getApplicationContext()
//				.getBeansWithAnnotation(org.tangerine.apiresolver.annotation.ApiMapping.class);
//		
//		String preffix = null;
//		for (Object apiExportor : apiMappings.values()) {
//			preffix = getMappingPreffix(apiExportor.getClass());
//			for (Method method : getMappingMethod(apiExportor.getClass())) {
//				String mapping = preffix + "." + 
//					method.getAnnotation(org.tangerine.apiresolver.annotation.ApiMapping.class).value();
//				mappingMap.put(mapping, new MappingObject(mapping, apiExportor, method));
//			}
//		}
//		
//		return mappingMap;
//	}
//	
//	@Override
//	protected List<Method> getMappingMethod(Class<?> clz) {
//		List<Method> suffixs = new ArrayList<Method>();
//		
//		for (Method method : clz.getDeclaredMethods()) {
//			if (method.getAnnotation(org.tangerine.apiresolver.annotation.ApiMapping.class) != null) {
//				suffixs.add(method);
//			}
//		}
//		
//		return suffixs;
//	}
//}
