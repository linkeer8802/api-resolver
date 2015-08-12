package org.tangerine.apiresolver.core;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.tangerine.apiresolver.annotation.Param;
import org.tangerine.apiresolver.core.mapping.ApiMapping;
import org.tangerine.apiresolver.core.mapping.MappingObject;
import org.tangerine.apiresolver.exception.APIAccessException;
import org.tangerine.apiresolver.exception.NoSuchAPIException;
import org.tangerine.apiresolver.exception.ParameterMissingException;
import org.tangerine.apiresolver.util.ParameterUtil;


public class ApiResolver implements BeanFactoryAware, InitializingBean {

//	protected String baseInterfacePackage;
	
	private Map<String,MappingObject> mappingObjects = new HashMap<String, MappingObject>();
	
	protected BeanFactory beanFactory;
	
//	public abstract Object getApiBean(String serviceName, String interfaceName) throws Exception;
	
//	public Class<?> getApiClass(String interfaceName) throws Exception {
//		try {
//			return Class.forName(interfaceName);
//		} catch (ClassNotFoundException e) {
//			throw new NoSuchAPIException("没有这个API接口", e);
//		}
//	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		Map<String, ApiMapping> regApiMappings = ((DefaultListableBeanFactory)beanFactory).getBeansOfType(ApiMapping.class);
		for (ApiMapping apiMapping : regApiMappings.values()) {
			mappingObjects.putAll(apiMapping.resolveApiMappings());
		}
	}
	
	public final Object resolve(String apiIdentifier, Map<String, String[]> params) throws Exception {
		
		String[] versions = params.get("version");
		StringBuffer buf = new StringBuffer(apiIdentifier);
		if (versions != null && versions.length > 0 && versions[0] != null) {
			buf.append(".v").append(versions[0].replace(".", "_"));
			apiIdentifier = buf.toString();
		}
		
		 MappingObject mappingObject = mappingObjects.get(apiIdentifier);
		 if (mappingObject == null) {
			 throw new NoSuchAPIException("No Such API, apiIdentifier=" + apiIdentifier);
		 }
	    
		try {
			return mappingObject.getTargetApi().invoke(
					mappingObject.getTarget(), 
					getMethodArgs(mappingObject.getTargetApi(), params));
		} catch (InvocationTargetException e) {
			throw new APIAccessException(e.getTargetException());
		}
	}
	
	public final Object resolve(HttpServletRequest request) throws Exception {
		return resolve(request.getParameter("api"), request.getParameterMap());
	}
	
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}	
//	public String getBaseInterfacePackage() {
//		return baseInterfacePackage;
//	}
//
//	public void setBaseInterfacePackage(String baseInterfacePackage) {
//		this.baseInterfacePackage = baseInterfacePackage;
//	}

	///////////////////////////////private//////////////////////////////////////////////////////////////
	
	protected Object[] getMethodArgs(Method method, Map<String, String[]> params) throws Exception {
		
//		/**通过ASM方式获得方法参数名**/
//		LocalVariableTableParameterNameDiscoverer parameterNameDiscoverer = 
//				new LocalVariableTableParameterNameDiscoverer();
//		String[] names = parameterNameDiscoverer.getParameterNames(method);

		Type[] parameterTypes = method.getGenericParameterTypes();
		Object[] args = new Object[parameterTypes.length];
		List<ApiParamMeta> apiParamMetaInfos = ApiMetaResolver.getApiArgMetas(method);
		
		int index = 0;
		for (Type parameterType : parameterTypes) {
			Class<?> typeClz = ParameterUtil.getTypeClass(parameterType);
			//简单或集合类型
			if (BeanUtils.isSimpleProperty(typeClz) 
					|| typeClz.isAssignableFrom(List.class)
					|| typeClz.isAssignableFrom(Map.class)) {
				
				if (apiParamMetaInfos.get(index) == null) {
					throw new IllegalStateException("无法解析方法["+method.getName()+"]的参数名，请在方法参数上使用@Param注解.");
				}
				String nameOfArgument = apiParamMetaInfos.get(index).getName();	
				
				//Map类型	
				if (typeClz.isAssignableFrom(Map.class)) {
					HashMap<String, Object> argMap = new HashMap<String, Object>();
					for (String name : params.keySet()) {
						if (name.startsWith(nameOfArgument)) {
							Object value = params.get(name);
							if (value.getClass().isArray()) {
								value = ((Object[]) value)[0];
							}
							argMap.put(name.replace(nameOfArgument + "_", ""), value);
						}
					}
					if (argMap.isEmpty()) {
						args[index] = params;
					} else {
						args[index] = argMap;
					}
				} else {
					args[index] = convertParamValuesIfNecessary(params.get(nameOfArgument), parameterType, apiParamMetaInfos.get(index));
				}
			//实体、vo等javaBean类型
			} else { 
				args[index] = getBeanValue(typeClz, params);
			}
			index++;
		}
		
		return args;
	}

//	private Annotation getAnnotationOfParam(Annotation[][] parameterAnnotations, Integer index) {
//		
//		if (parameterAnnotations == null || parameterAnnotations.length < index) {
//			return null;
//		}
//		
//		for (Annotation annotation : parameterAnnotations[index]) {
//			if (annotation.annotationType().equals(Param.class)) {
//				return annotation;
//			}
//		}
//		
//		return null;
//	}
	
	private Object getBeanValue(Class<?> typeClz, Map<String, String[]> params) throws Exception {
		
		Object bean = BeanUtils.instantiate(typeClz);
		PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(typeClz);
		for (PropertyDescriptor propertyDescriptor : targetPds) {
			if (propertyDescriptor.getWriteMethod() != null) {
				Object value = convertParamValuesIfNecessary(params.get(
											propertyDescriptor.getName()), 
											propertyDescriptor.getPropertyType(), 
											ApiParamMeta.getFromParam(typeClz.getDeclaredField(
													propertyDescriptor.getName()).getAnnotation(Param.class)));
				propertyDescriptor.getWriteMethod().invoke(bean, value);
			}
		}
		
		return bean;
	}

	protected Object convertParamValuesIfNecessary(String[] paramValues, Type paramType, ApiParamMeta apiParamMeta) {
		
		Boolean required = null;
		String defaultValue = null;
		if (apiParamMeta != null) {
			required = apiParamMeta.getRequired();
			defaultValue = apiParamMeta.getDefaultValue().trim().equals("") 
										? null : apiParamMeta.getDefaultValue();
			if (paramValues == null) {
				if (required && defaultValue == null) {
					throw new ParameterMissingException("缺少请求参数【"+apiParamMeta.getName()+"】");
				}
				//设置默认值
				if (defaultValue != null) {
					paramValues = new String[]{defaultValue};
				}
			}
		}
		
		//可选值
		if (paramValues == null) {
			return null;
		}
		
		Object value = null;
		Class<?> paramClass = ParameterUtil.getTypeClass(paramType);
		//数组类型
		if (paramClass.isArray()) {
			value = convertArrayParamValues(paramValues, paramClass.getComponentType());
		//List集合类型	
		} else if (paramClass.isAssignableFrom(List.class)) {
			value = convertArrayParamValues(paramValues, ParameterUtil.getParameterizedType(paramType));
			value = getCollectionFromArray(value, new ArrayList<Object>());;
		//Set集合类型	
	    } else if (paramClass.isAssignableFrom(Set.class)) {
			value = convertArrayParamValues(paramValues, ParameterUtil.getParameterizedType(paramType));
			value = getCollectionFromArray(value, new HashSet<Object>());
		//基本数据类型	
		} else {
			value = ((DefaultListableBeanFactory)beanFactory)
						.getTypeConverter().convertIfNecessary(paramValues[0], paramClass);
		}
		
		return value;
	}

	private Collection<?> getCollectionFromArray(Object value, Collection<Object> collection) {
		for (int i = 0; i < Array.getLength(value); i++) {
			collection.add(Array.get(value, i));
		}
		return collection;
	}

	private Object convertArrayParamValues(String[] paramValues, Class<?> componentType) {
		Object value;
		if (componentType.isInstance(String.class)) {
			return paramValues;
		} 
		value = Array.newInstance(componentType, paramValues.length);
		for (int i = 0; i < paramValues.length; i++) {
			Array.set(value, i, ((DefaultListableBeanFactory)beanFactory)
					.getTypeConverter().convertIfNecessary(paramValues[i], componentType));
		}
		return value;
	}	
}
