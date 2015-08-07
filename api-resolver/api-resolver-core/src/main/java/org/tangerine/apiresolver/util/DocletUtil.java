package org.tangerine.apiresolver.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;
import org.tangerine.apiresolver.annotation.ApiMapping;
import org.tangerine.apiresolver.annotation.Param;
import org.tangerine.apiresolver.core.mapping.ApiExportor;

import com.sun.javadoc.AnnotationDesc;
import com.sun.javadoc.AnnotationDesc.ElementValuePair;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.ProgramElementDoc;

public class DocletUtil {

	public static AnnotationDesc getAnnotationDesc(ProgramElementDoc doc, Class<? extends Annotation> annotationClz) {
		for (AnnotationDesc annotationDesc : doc.annotations()) {
			if (annotationDesc.annotationType()
					.qualifiedTypeName().equals(annotationClz.getName())) {
				return annotationDesc;
				}
		}
		return null;
	}
	
	public static Object getAnnotationDescValue(AnnotationDesc annotationDesc, String name) {
		for (ElementValuePair elementValuePair : annotationDesc.elementValues()) {
			if (elementValuePair.element().name().equals(name)) {
				return elementValuePair.value().value();
			}
		};
		return null;
	}
	
	public static Object getAnnotationValue(ProgramElementDoc doc, Class<? extends Annotation> annotationClz, String name) {
		AnnotationDesc annotationDesc = getAnnotationDesc(doc, annotationClz);
		return annotationDesc == null ? null : getAnnotationDescValue(annotationDesc, name);
	}
	
	public static Method getMethodByDoc(Class<?> classz, MethodDoc methodDoc) throws Exception {
		Class<?>[] parameterTypes = new Class<?>[methodDoc.parameters().length];
		for (int i = 0; i < methodDoc.parameters().length; i++) {
			Parameter parameter = methodDoc.parameters()[i];
			if (parameter.type().isPrimitive()) {
				if (parameter.type().simpleTypeName().equals("byte")) parameterTypes[i] = byte.class;
				else if (parameter.type().simpleTypeName().equals("boolean")) parameterTypes[i] = boolean.class;
				else if (parameter.type().simpleTypeName().equals("short")) parameterTypes[i] = short.class;
				else if (parameter.type().simpleTypeName().equals("char")) parameterTypes[i] = char.class;
				else if (parameter.type().simpleTypeName().equals("int")) parameterTypes[i] = int.class;
				else if (parameter.type().simpleTypeName().equals("long")) parameterTypes[i] = long.class;
				else if (parameter.type().simpleTypeName().equals("float")) parameterTypes[i] = float.class;
				else if (parameter.type().simpleTypeName().equals("double")) parameterTypes[i] = double.class;
			} else {
				parameterTypes[i] = Class.forName(parameter.type().qualifiedTypeName());
			}
			if (!StringUtils.isEmpty(parameter.type().dimension())) {
				int dimension = StringUtil.getStrCount(parameter.type().dimension(), "[]");
				parameterTypes[i] = Array.newInstance(parameterTypes[i], 
						(int[])Array.newInstance(int.class, dimension)).getClass();
			}
		}
		
		return classz.getDeclaredMethod(methodDoc.name(), parameterTypes);
	}
	
	public static Field getFieldByDoc(ClassDoc classDoc, FieldDoc fieldDoc) throws Exception {
		Class<?> classz = Class.forName(classDoc.qualifiedTypeName());
		return classz.getDeclaredField(fieldDoc.name());
	}
	
	public static FieldDoc getFieldDoc(ClassDoc classDoc, String name) {
		for (FieldDoc fieldDoc : classDoc.fields()) {
			if (fieldDoc.name().equals(name) 
					||name.equals(getAnnotationValue(fieldDoc, Param.class, "value"))) {
				return fieldDoc;
			} 
		}
		
		return null;
	}
	
	public static boolean hasInterface (ClassDoc classDoc) {
		if (classDoc.interfaces().length > 0) {
			for (ClassDoc inf : classDoc.interfaces()) {
				if (inf.qualifiedTypeName().equals(ApiExportor.class.getName())) {
					return true;
				}
			}
		}
		
		return false;
	}

	public static boolean hasAnnotation (ClassDoc classDoc) {
		if (classDoc.interfaces().length > 0) {
			for (AnnotationDesc annotationDesc : classDoc.annotations()) {
				if (annotationDesc.annotationType().qualifiedTypeName().equals(ApiMapping.class.getName())) {
					return true;
				}
			}
		}
		
		return false;
	}
}
