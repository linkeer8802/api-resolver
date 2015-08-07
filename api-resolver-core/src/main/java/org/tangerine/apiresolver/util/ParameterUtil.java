package org.tangerine.apiresolver.util;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParameterUtil {

	private static Map<String, Class<?>> simpleTypeClassesMap = new HashMap<String, Class<?>>();
	
	static {
		simpleTypeClassesMap.put("byte", byte.class);
		simpleTypeClassesMap.put("boolean", boolean.class);
		simpleTypeClassesMap.put("short", short.class);
		simpleTypeClassesMap.put("char", char.class);
		simpleTypeClassesMap.put("int", int.class);
		simpleTypeClassesMap.put("long", long.class);
		simpleTypeClassesMap.put("float", float.class);
		simpleTypeClassesMap.put("double", double.class);
		
		simpleTypeClassesMap.put("String", String.class);
		simpleTypeClassesMap.put("Byte", Byte.class);
		simpleTypeClassesMap.put("Boolean", Boolean.class);
		simpleTypeClassesMap.put("Short", Short.class);
		simpleTypeClassesMap.put("Character", Character.class);
		simpleTypeClassesMap.put("Integer", Integer.class);
		simpleTypeClassesMap.put("Long", Long.class);
		simpleTypeClassesMap.put("Float", Float.class);
		simpleTypeClassesMap.put("Double", Double.class);
		
	}
	
	public static Class<?> getSimpleTypeClass(String name) {
		return simpleTypeClassesMap.get(name);
	}
	
	public static Object getSimpleTypeDefaultValue(String name) {
		Class<?> typeClass = getSimpleTypeClass(name);
		if (typeClass != null) {
			if (Number.class.isAssignableFrom(typeClass) || typeClass.isPrimitive()) {
				return "1";
			} else if (typeClass.equals(String.class)) {return "this is a auto generate string value.";}
			else if (typeClass.equals(Boolean.class)) return true;
		}
		
		return null;
	}
	
	public static Class<?> getTypeClass(Type parameterType) {
		Class<?> typeClz = null;
		if (parameterType instanceof Class) {
			typeClz = (Class<?>)parameterType;
		} else if (parameterType instanceof ParameterizedType) {
			typeClz = (Class<?>)((ParameterizedType)parameterType).getRawType();
		} else if (parameterType instanceof GenericArrayType) {
			typeClz = (Class<?>)((GenericArrayType)parameterType).getGenericComponentType();
		} else {
			throw new IllegalArgumentException("Unsupport parameter type[" + parameterType + "].");
		}
		return typeClz;
	}
	
	public static Class<?> getParameterizedType(Type type) {
		return ((Class<?>) ((ParameterizedType)type).getActualTypeArguments()[0]);
	}
	
	public static List<Class<?>> getGenericTypeClasses(List<Class<?>> genericTypeClasses, Type... genericTypes) {
		
		for (Type genericType : genericTypes) {
			if (genericType instanceof ParameterizedType) {
				getGenericTypeClasses(genericTypeClasses, ((ParameterizedType)genericType).getActualTypeArguments());
			} else if (genericType instanceof GenericArrayType) {
				getGenericTypeClasses(genericTypeClasses, ((GenericArrayType)genericType).getGenericComponentType());
			} else if (genericType instanceof TypeVariable) {
				getGenericTypeClasses(genericTypeClasses, ((TypeVariable<?>)genericType).getBounds());
			} else if (genericType instanceof WildcardType) {
				getGenericTypeClasses(genericTypeClasses, ((WildcardType)genericType).getUpperBounds());
				getGenericTypeClasses(genericTypeClasses, ((WildcardType)genericType).getLowerBounds());
			} else if (genericType instanceof Class) {
				Class<?> classType = (Class<?>)genericType;
				if (classType.isArray()) {
					getGenericTypeClasses(genericTypeClasses, classType.getComponentType());
				} else {
					genericTypeClasses.add(classType);
				}
			} else {
				throw new IllegalStateException("unknown type:" + genericType);
			}
		}
		
		return genericTypeClasses;
	}
}
