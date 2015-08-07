//package org.tangerine.apiresolver.core.mapping;
//
//import java.lang.reflect.Method;
//import java.util.HashMap;
//import java.util.Map;
//
//
//public class SimpleRemoteApiMapping extends ApiMapping {
//
//	@Override
//	public Map<String, MappingObject> resolveApiMappings() {
//		Map<String, MappingObject> mappingMap = new HashMap<String, MappingObject>();
//		Map<String, ApiExportor> apiMappings =  getApplicationContext().getBeansOfType(ApiExportor.class, false, true);
//		
//		String preffix = null;
//		for (ApiExportor apiExportor : apiMappings.values()) {
//			preffix = getMappingPreffix(apiExportor.getClass());
//			for (Method method : getMappingMethod(apiExportor.getClass())) {
//				String mapping = preffix + "." + method.getName();
//				mappingMap.put(mapping, new MappingObject(mapping, apiExportor, method));
//			}
//		}
//		
//		return mappingMap;
//	}
//}
