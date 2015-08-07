package org.tangerine.apiresolver.core;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.tangerine.apiresolver.annotation.Param;
import org.tangerine.apiresolver.util.ParameterUtil;

public class ApiMetaResolver {

//	private String nameOfMethod;
//	
//	private String nameOfService;
//	
////	private String nameOfInterface;
//	
//	private ApiResolver apiResolver;
//	
//	public ApiMetaResolver(String methodIdentifier, ApiResolver serviceResolver) {
//		
//		this.apiResolver = serviceResolver;
//		
//		String[] methodIdentifiers = methodIdentifier.split("\\.");
//		this.nameOfMethod = getmethodName(methodIdentifiers);
//		this.nameOfService = getServiceName(methodIdentifiers);
////		this.nameOfInterface = getServiceInterfaceName(methodIdentifier, methodIdentifiers);
//	}
	
//	private String getmethodName(String[] methodIdentifiers) {
//		return methodIdentifiers[methodIdentifiers.length-1];
//	}
//	
//	private String getServiceName(String[] methodIdentifiers) {
//		return methodIdentifiers[methodIdentifiers.length-2];
//	}
	
//	private String getServiceInterfaceName(String methodIdentifier, String[] methodIdentifiers) {
//		String nameOfService = getServiceName(methodIdentifiers);
//		StringBuffer buf = new StringBuffer();
//		buf.append(apiResolver.getBaseInterfacePackage());
//		buf.append(".");
//		buf.append(methodIdentifier.substring(0, methodIdentifier.lastIndexOf(getmethodName(methodIdentifiers))-1));
//		return buf.toString().replace(nameOfService, StringUtils.capitalize(nameOfService));
//	}
	
	/**
	 * 通过ASM方式获得方法参数信息
	 * @param apiMethod
	 * @return
	 */
	private static ApiParamMeta[] getApiParamMetaByASM(Method apiMethod) {
		LocalVariableTableParameterNameDiscoverer parameterNameDiscoverer = 
				new LocalVariableTableParameterNameDiscoverer();
		String[] names = parameterNameDiscoverer.getParameterNames(apiMethod);
		
		if (names != null) {
			ApiParamMeta[] apiParamMetas = new ApiParamMeta[names.length];
			for (int i = 0; i < names.length; i++) {
				apiParamMetas[i] = new ApiParamMeta(names[i]);
			}
			return apiParamMetas;
		}
		return null;
	}
	
	public static LinkedHashSet<ApiParamMeta> getApiParamMetaByBeanValue(Class<?> typeClz) throws Exception {
		
		LinkedHashSet<ApiParamMeta> apiParamMetaInfos = new LinkedHashSet<ApiParamMeta>();
		PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(typeClz);
		for (PropertyDescriptor propertyDescriptor : targetPds) {
			if ( propertyDescriptor.getName() != null
					&& propertyDescriptor.getWriteMethod() != null) {
				Param param = typeClz.getDeclaredField(propertyDescriptor.getName()).getAnnotation(Param.class);
				if (param != null) {
					apiParamMetaInfos.add(ApiParamMeta.getFromParam(param));
				} else {
					apiParamMetaInfos.add(new ApiParamMeta(propertyDescriptor.getName()));
				}
			}
		}
		
		return apiParamMetaInfos;
	}
	/**
	 * 通过注解方式获得方法参数信息
	 * @param apiMethod
	 * @return
	 */
	private static ApiParamMeta[] getApiParamMetaByAnnotation(Method apiMethod) {
		
		Annotation[][] parameterAnnotations = apiMethod.getParameterAnnotations();
		ApiParamMeta[] apiParamMetas = new ApiParamMeta[parameterAnnotations.length];
		
		int i = 0;
		for (Annotation[] annotations : parameterAnnotations) {
			for (Annotation annotation : annotations) {
				if (annotation.annotationType().equals(Param.class)) {
					apiParamMetas[i] = ApiParamMeta.getFromParam((Param)annotation);
				}
			}
			i++;
		}
		
		return apiParamMetas;
	}
	
	public static List<ApiParamMeta> getApiArgMetas(Method apiMethod) {
		ApiParamMeta[] aSMApiParamMetas = getApiParamMetaByASM(apiMethod);
		ApiParamMeta[] annotationApiParamMetas = getApiParamMetaByAnnotation(apiMethod);
		List<ApiParamMeta> apiParamMetas = new ArrayList<ApiParamMeta>();
		
		for (int i = 0; i < apiMethod.getParameterCount(); i++) {
			if (annotationApiParamMetas != null 
					&& i < annotationApiParamMetas.length 
					&& annotationApiParamMetas[i] != null) {
				apiParamMetas.add(annotationApiParamMetas[i]);
				
			} else if (aSMApiParamMetas != null
						&& i < aSMApiParamMetas.length 
						&& aSMApiParamMetas[i] != null) {
				apiParamMetas.add(aSMApiParamMetas[i]);
			}
		}
		
		return apiParamMetas;
	}
	
	public static List<ApiParamMeta> getApiParamMetas(Method method) throws Exception {
		
		Type[] parameterTypes = method.getGenericParameterTypes();
		List<ApiParamMeta> apiParamMetaInfos = new ArrayList<ApiParamMeta>();
		List<ApiParamMeta> arrApiParameterMetas = ApiMetaResolver.getApiArgMetas(method);
		apiParamMetaInfos.addAll(arrApiParameterMetas);
		
		int index = 0;
		for (Type parameterType : parameterTypes) {
			Class<?> typeClz = ParameterUtil.getTypeClass(parameterType);
			//简单或集合类型
			if (BeanUtils.isSimpleProperty(typeClz) 
					|| typeClz.isAssignableFrom(List.class)
					|| typeClz.isAssignableFrom(Map.class)) {
				
				if (arrApiParameterMetas.get(index) == null) {
					throw new IllegalStateException("不能解析方法["+method.getName()+"]的参数名，请在方法参数上加入@Param注解.");
				}
			//实体、vo等javaBean类型
			} else { 
				apiParamMetaInfos.addAll(getApiParamMetaByBeanValue(typeClz));
			}
			index++;
		}
		
		return apiParamMetaInfos;
	}
	
//	public String getNameOfMethod() {
//		return nameOfMethod;
//	}
//
//	public String getNameOfService() {
//		return nameOfService;
//	}

//	public String getNameOfInterface() {
//		return nameOfInterface;
//	}
	
//	public Method getMethod() throws NoSuchAPIException, Exception {
//		
//		for (Method method : apiResolver.getApiClass(nameOfInterface).getDeclaredMethods()) {
//			if (nameOfMethod.equals(method.getName()) && Modifier.isPublic(method.getModifiers())) {
//				return method;
//			}
//		}
//		
//		throw new NoSuchAPIException("没有这个API接口");
//	}
	
//	public ApiParamMeta[] getApiParameterMetas() throws Exception {
//		return getApiArgMetas(getMethod());
//	}
//	
//	public Set<ApiParamMeta> getApiSimpleParameterMetas() throws Exception {
//		
//		Method apiMethod = getMethod();
//		ApiParamMeta[] apiParameterMetas = getApiArgMetas(apiMethod);
//		Set<ApiParamMeta> apiParameterMetaSet = new LinkedHashSet<ApiParamMeta>();
//		
//		int index = 0;
//		for (Type type : apiMethod.getGenericParameterTypes()) {
//			Class<?> typeClz = ParameterUtil.getTypeClass(type);
//			//简单或集合类型
//			if (BeanUtils.isSimpleProperty(typeClz) 
//					|| typeClz.isAssignableFrom(List.class)
//					|| typeClz.isAssignableFrom(Map.class)) {
//				if (apiParameterMetas[index] == null) {
//					throw new IllegalStateException("不能解析方法["+apiMethod.getName()+"]的参数名，请在方法参数上加入@Param注解.");
//				}
//				apiParameterMetaSet.add(apiParameterMetas[index]);
//			//自定义Bean	
//			} else {
//				PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(typeClz);
//				for (PropertyDescriptor propertyDescriptor : targetPds) {
//					if (propertyDescriptor.getWriteMethod() != null) {
//						ApiParamMeta apiParamMeta = ApiParamMeta.getFromParam(typeClz.getDeclaredField(
//								propertyDescriptor.getName()).getAnnotation(Param.class));
//						if (apiParamMeta == null) {
//							apiParamMeta = new ApiParamMeta(propertyDescriptor.getName());
//						}
//						apiParameterMetaSet.add(apiParamMeta);
//					}
//				}
//			}
//			index++;
//		}
//		
//		return apiParameterMetaSet;
//	}
}
