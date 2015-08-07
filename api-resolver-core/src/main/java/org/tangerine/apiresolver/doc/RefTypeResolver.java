package org.tangerine.apiresolver.doc;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.SimpleTypeConverter;
import org.tangerine.apiresolver.doc.entity.ApiTypeDoc;
import org.tangerine.apiresolver.doc.entity.RefTypeDoc;
import org.tangerine.apiresolver.doc.entity.ResultDoc;
import org.tangerine.apiresolver.support.ProjectClassLoader;
import org.tangerine.apiresolver.util.ArrayUtil;
import org.tangerine.apiresolver.util.JsonUtil;
import org.tangerine.apiresolver.util.ParameterUtil;
import org.tangerine.apiresolver.util.StringUtil;

public class RefTypeResolver {

	private Map<String, Object> reftypeBeanContainer = new HashMap<String, Object>();
	
	private SimpleTypeConverter typeConverter = new SimpleTypeConverter();
	
	@SuppressWarnings({ "unchecked"})
	public String resolveResultExample(ResultDoc resultDoc) {
		
		Object resultExample = null;
		try {
			String resultType = resultDoc.getType();
			resultExample = resolveSingleResultExample(resultDoc.getExampleValue(), resultType);
			
			if (resultExample == null) {
				if (resultType.equals("List")) {
					resultExample = new ArrayList<Object>();
					((List<Object>)resultExample).add(resolveSingleResultExample(
							resultDoc.getExampleValue(), resultDoc.getRefTypes().iterator().next().getName()));
				}
				int dimension = StringUtil.getStrCount(resultType, "[]");
				// array
				if (dimension > 0) {
					int[] dimensions = (int[])Array.newInstance(int.class, dimension);
					Arrays.fill(dimensions, 1);
					resultExample = Array.newInstance(getRefTypeClass(
							resultDoc.getRefTypes().iterator().next().getName()), dimensions);
					ArrayUtil.fill(resultExample, resolveSingleResultExample(
							resultDoc.getExampleValue(), resultDoc.getRefTypes().iterator().next().getName()));
				} 
			}
		} catch (Exception e) {
			resultExample = null;
		}
		
		return resultExample == null ? null : JsonUtil.toPrettyJson(resultExample);
	}
	
	public void instantiationAllApiTypes() throws Exception {
		for (ApiTypeDoc type : DocContainer.get().getApiTypeDocs()) {
			if (!reftypeBeanContainer.containsKey(type.getName())) {
				instantiationApiType(type);
			}
		}
	}
	
	private Class<?> getRefTypeClass(String name) throws Exception {
		Class<?> refTypeClass = ParameterUtil.getSimpleTypeClass(name);
		if (refTypeClass == null) {
			ApiTypeDoc apiTypeDoc = DocContainer.get().getApiTypeDocQuery().getApiTypeDoc(name);
			if (apiTypeDoc != null) {
				refTypeClass = ProjectClassLoader.C(apiTypeDoc.getQualifiedTypeName());
			}
		}
		
		return refTypeClass;
	}
	
	private Object resolveSingleResultExample(Object exampleValue, String resultType) {
		Object resultExample = null;
		
		try {
			//java bean
			if (reftypeBeanContainer.containsKey(resultType)) {
				resultExample = reftypeBeanContainer.get(resultType);
			}
			// simple type
			else {
				Class<?> resultTypeClass = ParameterUtil.getSimpleTypeClass(resultType);
				if (resultTypeClass != null) {
					if (exampleValue == null || StringUtils.isEmpty(exampleValue.toString())) {
						exampleValue = ParameterUtil.getSimpleTypeDefaultValue(resultType);
					}
					resultExample = typeConverter.convertIfNecessary(exampleValue, resultTypeClass);
				}
			}
		} catch (Exception e) {
			resultExample = null;
		}
		
		return resultExample;
	}
	
	private Object instantiationApiType(ApiTypeDoc type) throws Exception {
		
		Class<?> apiTypeClz = ProjectClassLoader.C(type.getQualifiedTypeName());
		Object resultBean = apiTypeClz.newInstance();
		
		for (ResultDoc resultDoc : type.getAttrs()) {
			PropertyDescriptor propertyDescriptor = BeanUtils.getPropertyDescriptor(apiTypeClz, resultDoc.getName());
				
			if (resultDoc.getIsSimpleType()) {
				if (resultDoc.getRefTypes().size() == 0) {
					propertyDescriptor.getWriteMethod().invoke(resultBean, 
							resolveSingleResultExample(resultDoc.getExampleValue(), resultDoc.getType()));
				} else {
					List<Object> refValues = getRefValues(resultDoc);
					int dimension = StringUtil.getStrCount(resultDoc.getType(), "[]");
					// array
					if (dimension > 0) {
						int[] dimensions = (int[])Array.newInstance(int.class, dimension);
						Arrays.fill(dimensions, 1);
						Object instanceValue = Array.newInstance(getRefTypeClass(
								resultDoc.getRefTypes().iterator().next().getName()), dimensions);
						ArrayUtil.fill(instanceValue, refValues.get(0));
					} else if (resultDoc.getType().equalsIgnoreCase("List")) {
						List<Object> list = new ArrayList<Object>();
						list.add(refValues.get(0));
						propertyDescriptor.getWriteMethod().invoke(resultBean, list);
					} else if (resultDoc.getType().equalsIgnoreCase("Set")) {
						Set<Object> set = new HashSet<Object>();
						set.add(refValues.get(0));
						propertyDescriptor.getWriteMethod().invoke(resultBean, set);
					} else if (resultDoc.getType().equalsIgnoreCase("Map")) {
						Map<Object, Object> map = new HashMap<Object, Object>();
						map.put(refValues.get(0), refValues.get(1));
						propertyDescriptor.getWriteMethod().invoke(resultBean, map);
					} else {
						propertyDescriptor.getWriteMethod().invoke(resultBean, refValues.get(0));
					}
				}
			} else {
				Object refValue = reftypeBeanContainer.get(resultDoc.getType());
				if (refValue == null) {
					ApiTypeDoc apiTypeDoc = DocContainer.get().getApiTypeDocQuery().getApiTypeDoc(resultDoc.getType());
					refValue = instantiationApiType(apiTypeDoc);
				}
				propertyDescriptor.getWriteMethod().invoke(resultBean, refValue);
			}
		}
		reftypeBeanContainer.put(type.getName(), resultBean);
		
		return resultBean;
	}

	private List<Object> getRefValues(ResultDoc resultDoc) throws Exception {
		List<Object> refValues = new ArrayList<Object>();
		//refTypes
		for (RefTypeDoc refTypeDoc : resultDoc.getRefTypes()) {
			//简单类型
			if (refTypeDoc.getIsSimpleType()) {
				refValues.add(resolveSingleResultExample(null, refTypeDoc.getName()));
			//java bean等引用类型	
			} else {
				Object refValue = reftypeBeanContainer.get(refTypeDoc.getName());
				if (refValue == null) {
					ApiTypeDoc apiTypeDoc = DocContainer.get().getApiTypeDocQuery().getApiTypeDoc(refTypeDoc.getName());
					refValue = instantiationApiType(apiTypeDoc);
					refValues.add(refValue);
				}
			}
		}
		return refValues;
	}
}
