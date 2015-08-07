/**BeanUtil.java**/
package org.tangerine.apiresolver.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;

/**
 * @author Administrator
 *
 */
public class BeanUtil {

	public static boolean isStandardJavaBean(Class<?> beanClz) {
		try {
			try {
				beanClz.getConstructor();
			} catch (NoSuchMethodException e) {
				throw new FatalBeanException(beanClz + " is not a standard java bean.");
			}
			
			Field[] declaredFields = beanClz.getDeclaredFields();
			
			if (declaredFields.length == 0 || beanClz.isPrimitive()) return false;
			
			int nonBeanFieldNum = 0;
			for (Field field : declaredFields) {
				if (getReadMethod(beanClz, field) == null 
						|| getReadMethod(beanClz, field) == null) {
					if (Modifier.isFinal(field.getModifiers())) {
						nonBeanFieldNum++;
						continue;
					}
					throw new FatalBeanException(beanClz + " is not a standard java bean.");
				}
			}
			if (nonBeanFieldNum == declaredFields.length) {
				throw new FatalBeanException(beanClz + " is not a standard java bean.");
			}
		} catch (BeansException e) {
			return false;
		}
		
		return true;
	}
	
	public static Method getReadMethod(Class<?> beanClz, Field field) {
		try {
			String prefix = field.getType().getName().equals("boolean") ? "is" : "get";
			return beanClz.getDeclaredMethod(prefix + StringUtils.capitalize(field.getName()));
		} catch (Exception e) {
			return null;
		}
	}
	
	public static Method getWriteMethod(Class<?> beanClz, Field field) {
		try {
			return beanClz.getDeclaredMethod("set" + StringUtils.capitalize(field.getName()), field.getType());
		} catch (Exception e) {
			return null;
		}
	}
}
