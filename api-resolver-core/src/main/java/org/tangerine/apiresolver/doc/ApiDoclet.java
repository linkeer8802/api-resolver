package org.tangerine.apiresolver.doc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.tangerine.apiresolver.annotation.ApiMapping;
import org.tangerine.apiresolver.core.ApiMetaResolver;
import org.tangerine.apiresolver.core.ApiParamMeta;
import org.tangerine.apiresolver.core.OutPutBean;
import org.tangerine.apiresolver.core.mapping.AnnotationApiMapping;
import org.tangerine.apiresolver.core.mapping.ApiExportor;
import org.tangerine.apiresolver.core.mapping.SimpleApiMapping;
import org.tangerine.apiresolver.doc.entity.ApiCategory;
import org.tangerine.apiresolver.doc.entity.ApiDoc;
import org.tangerine.apiresolver.doc.entity.ApiTypeDoc;
import org.tangerine.apiresolver.doc.entity.ParamDoc;
import org.tangerine.apiresolver.doc.entity.RefTypeDoc;
import org.tangerine.apiresolver.doc.entity.ResultDoc;
import org.tangerine.apiresolver.util.BeanUtil;
import org.tangerine.apiresolver.util.DocletUtil;
import org.tangerine.apiresolver.util.FileUtil;
import org.tangerine.apiresolver.util.ParameterUtil;

import com.google.gson.GsonBuilder;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.Doc;
import com.sun.javadoc.Doclet;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.ParamTag;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.RootDoc;
import com.sun.javadoc.Tag;

 
public class ApiDoclet extends Doclet {

	private static File docMetaDataDir;
	
	public static final String DOC_ENCODING= "UTF-8";
	
	public static final String CATEGORY_FILE_PREFIX = "api_category";
	public static final String APIDOC_FILE_PREFIX = "api_doc";
	public static final String APITYPE_FILE_PREFIX = "api_type";
	
	public static final String DOC_DIR = "META-INF" + File.separator + "APIDOC";
	
	
	
	private final static List<ApiDoc> apiDocs = new ArrayList<ApiDoc>();
	private final static Map<Class<?>, ApiTypeDoc> apiTypes = new HashMap<Class<?>, ApiTypeDoc>();
	private final static List<ApiCategory> apiCategorys = new ArrayList<ApiCategory>();
	
	public static void init() {
		String _outputRootDir = System.getProperty("outputRootDir");
		if (_outputRootDir != null) {
			docMetaDataDir = new File(_outputRootDir, DOC_DIR);
			FileUtil.clearDir(docMetaDataDir);
			if (!docMetaDataDir.exists()) {
				docMetaDataDir.mkdirs();
			}
		}
	}
	
    public static boolean start(RootDoc root) {
    	init();
    	
        ClassDoc[] classes = root.classes();
        try {
			for (ClassDoc classDoc : classes) {
				
//				if (!(DocletUtil.hasInterface(classDoc) || DocletUtil.hasAnnotation(classDoc))) {
//					continue;
//				}
				
				ApiCategory category = new ApiCategory();
				category.setCid(UUID.randomUUID().toString());
				category.setDesc(classDoc.commentText());
				String tagName = getTagText(classDoc, "summary", true);
				category.setName(StringUtils.isEmpty(tagName) ? classDoc.commentText() : tagName);
				apiCategorys.add(category);
				
				for (MethodDoc methodDoc : classDoc.methods(true)) {
					String mapping = getApiMapping(classDoc, methodDoc);
					ApiDoc apiDoc = new ApiDoc();
					apiDoc.setId(UUID.randomUUID().toString());
					apiDoc.setMapping(mapping);
					apiDoc.setCategory(category.getCid());
					apiDoc.setDesc(methodDoc.commentText());
					tagName = getTagText(methodDoc, "summary", true);
					apiDoc.setName(StringUtils.isEmpty(tagName) ? methodDoc.commentText() : tagName);
					apiDoc.setAuthor(getTagText(methodDoc, "author", false));
					apiDoc.setVersion(getTagText(methodDoc, "version", false));
					
					parameterDoc(apiDoc, classDoc, methodDoc);
					
					returnDoc(apiDoc, classDoc, methodDoc);
					
					apiDocs.add(apiDoc);
				}
			}
			
			saveToFile(apiCategorys, CATEGORY_FILE_PREFIX);
			saveToFile(apiDocs, APIDOC_FILE_PREFIX);
			saveToFile(apiTypes.values(), APITYPE_FILE_PREFIX);
		} catch (Exception e) {
			throw new IllegalStateException("ApiDoclet Exception.", e);
		}
        
        return true;
    }

	private static String getApiMapping(ClassDoc classDoc, MethodDoc methodDoc) throws Exception {
		
		Class<?> classz = Class.forName(classDoc.qualifiedTypeName());
		Class<?>[] interfaces = classz.getInterfaces();
		for (Class<?> inf : interfaces) {
			if (inf.isAssignableFrom(ApiExportor.class)) {
				return new SimpleApiMapping().getMappingName(classz, DocletUtil.getMethodByDoc(classz, methodDoc));
			}
		}
		
		ApiMapping annotation = classz.getAnnotation(ApiMapping.class);
		if (annotation != null) {
			return new AnnotationApiMapping().getMappingName(classz, DocletUtil.getMethodByDoc(classz, methodDoc));
		}
		
		return "";
	}
    
    public static void parameterDoc(ApiDoc apiDoc, ClassDoc classDoc, MethodDoc methodDoc) throws Exception {
    	ParamTag[] paramTags = methodDoc.paramTags();
    	Method method = DocletUtil.getMethodByDoc(Class.forName(classDoc.qualifiedTypeName()), methodDoc);
		List<ApiParamMeta> apiArgMetas = ApiMetaResolver.getApiParamMetas(method);
		
		int index = 0;
		for (Parameter parameter : methodDoc.parameters()) {
			
			Class<?> paramClz = null;
			if (!parameter.type().isPrimitive()) {
				paramClz = Class.forName(parameter.type().qualifiedTypeName());
			}
			
			//简单或集合类型
			if (parameter.type().isPrimitive()
					|| BeanUtils.isSimpleProperty(paramClz) 
					|| paramClz.isAssignableFrom(List.class)
					|| paramClz.isAssignableFrom(Map.class)) {
				
				ParamDoc paramDoc = new ParamDoc();
				paramDoc.setName(parameter.name());
				paramDoc.setType(parameter.typeName());
				
				ParamTag paramTag = getParamTag(paramTags, parameter.name());
				if (paramTag == null) {
					throw new IllegalStateException("missing @param code comments[parameter name=" + parameter.name() + "] in postion:" + methodDoc.position());
				}
				String[] paramTexts = paramTag.parameterComment().split("(e.g|eg|e.g.|example|exam|例如|如)(:|：)", 2);
				paramDoc.setDesc(paramTexts[0]);
				paramDoc.setExampleValue(paramTexts.length==2 ? paramTexts[1] : "");
				
				ApiParamMeta apiParamMeta = apiArgMetas.get(index);
				if (index < methodDoc.parameters().length && apiParamMeta != null) {
					paramDoc.setName(apiParamMeta.getName());
					paramDoc.setRequired(apiParamMeta.getRequired());
					paramDoc.setDefaultValue(apiParamMeta.getDefaultValue());
				}
				apiDoc.addParam(paramDoc);
			//实体、vo等javaBean类型
			} else {
				ClassDoc parameterClassDoc = classDoc.findClass(paramClz.getName());
				for (ApiParamMeta apiParamMeta : ApiMetaResolver.getApiParamMetaByBeanValue(paramClz)) {
					ParamDoc paramDoc = new ParamDoc();
					paramDoc.setName(apiParamMeta.getName());
					paramDoc.setRequired(apiParamMeta.getRequired());
					paramDoc.setDefaultValue(apiParamMeta.getDefaultValue());
					
					FieldDoc fieldDoc = DocletUtil.getFieldDoc(parameterClassDoc, apiParamMeta.getName());
					if (fieldDoc != null) {
						paramDoc.setType(fieldDoc==null ? "" : fieldDoc.type().typeName());
						String[] paramTexts = fieldDoc.commentText().split("(e.g|eg|e.g.|example|exam|例如|如)(:|：)", 2);
						paramDoc.setDesc(paramTexts[0]);
						paramDoc.setExampleValue(paramTexts.length==2 ? paramTexts[1] : "");
					}
					
					apiDoc.addParam(paramDoc);
				}
			}
			index++;
		}
	}
    
    public static void returnDoc(ApiDoc apiDoc, ClassDoc classDoc, MethodDoc methodDoc) throws Exception {
    	
    	Method method = DocletUtil.getMethodByDoc(Class.forName(classDoc.qualifiedTypeName()), methodDoc);
    	
    	Type returnType = method.getGenericReturnType();
    	
		if (returnType.toString().equalsIgnoreCase("void")) {
			return;
		}

		ResultDoc resultDoc = new ResultDoc();
		Tag[] tags = methodDoc.tags("return");
		if (tags != null && tags.length == 1) {
			String[] returnTexts = tags[0].text().split("(e.g|eg|e.g.|example|exam|例如|如)(:|：)", 2);
			resultDoc.setDesc(returnTexts[0]);
			resultDoc.setExampleValue(returnTexts.length==2 ? returnTexts[1] : "");
			resultDoc.setName("");
			resultDoc.setType(methodDoc.returnType().typeName() + methodDoc.returnType().dimension());
		}
    	
		addRefType(classDoc, returnType, resultDoc);
		
		apiDoc.setResultDoc(resultDoc);
	}

	private static void addRefType(ClassDoc classDoc, Type genericType, ResultDoc resultDoc) throws Exception {
		//java bean对象返回值
		if (genericType instanceof Class  && isBeanType((Class<?>)genericType)) {
			resultDoc.setIsSimpleType(false);
//			addApiType(classDoc, (Class<?>)genericType);
			resultDoc.addRefType(new RefTypeDoc(addApiType(classDoc, (Class<?>)genericType).getName(), false));
		} else {
			resultDoc.setIsSimpleType(true);
		}
		
		//泛型类型返回值
		List<Class<?>> genericTypeClasses = ParameterUtil.getGenericTypeClasses(
							new ArrayList<Class<?>>(), genericType);
		
		for (Class<?> typeClass : genericTypeClasses) {
			if (typeClass.equals(genericType)) continue;
			if (isBeanType(typeClass)) {
				resultDoc.addRefType(new RefTypeDoc(addApiType(classDoc, typeClass).getName(), false));
			} else { //simple Type
				resultDoc.addRefType(new RefTypeDoc(typeClass.getSimpleName(), true));
			}
		}
	}
	
	private static boolean isBeanType(Class<?> clz) {
		return !clz.equals(Object.class) 
				&& !BeanUtils.isSimpleProperty(clz)
				&& (BeanUtil.isStandardJavaBean(clz) 
				|| clz.isAssignableFrom(OutPutBean.class));
	}

	private static ApiTypeDoc addApiType(ClassDoc classDoc, Class<?> resultClz) throws Exception {
		
		if (apiTypes.containsKey(resultClz)) {
			return apiTypes.get(resultClz);
		}
		
		ClassDoc resultClassDoc = classDoc.findClass(resultClz.getName());
		ApiTypeDoc typeDoc = new ApiTypeDoc();
		typeDoc.setName(resultClz.getSimpleName());
		typeDoc.setQualifiedTypeName(resultClz.getName());
		typeDoc.setDesc(resultClassDoc.commentText());
		
		List<ResultDoc> attrs = new ArrayList<ResultDoc>();
		
		for (ApiParamMeta apiParamMeta : ApiMetaResolver.getApiParamMetaByBeanValue(resultClz)) {
			
			ResultDoc resultDoc = new ResultDoc();
			resultDoc.setIsSimpleType(false);
			resultDoc.setName(apiParamMeta.getName());
			
			FieldDoc fieldDoc = DocletUtil.getFieldDoc(resultClassDoc, apiParamMeta.getName());
			if (fieldDoc != null) {
				resultDoc.setType(fieldDoc.type().typeName());
				addRefType(classDoc, DocletUtil.getFieldByDoc(resultClassDoc, fieldDoc).getGenericType() , resultDoc);
				String[] paramTexts = fieldDoc.commentText().split("(e.g|eg|e.g.|example|exam|例如|如)(:|：)", 2);
				resultDoc.setDesc(paramTexts[0]);
				resultDoc.setExampleValue(paramTexts.length==2 ? paramTexts[1] : "");
			}
			
			attrs.add(resultDoc);
		}
		
		typeDoc.setAttrs(attrs);
		
		apiTypes.put(resultClz, typeDoc);
		
		return typeDoc;
	}
    
    private static ParamTag getParamTag(ParamTag[] paramTags, String name) {
    	for (ParamTag paramTag : paramTags) {
			if (paramTag.parameterName().equals(name)) {
				return paramTag;
			}
		}
    	
    	return null;
    }
    
    private static Tag[] getTag(Doc doc, String name, boolean check) {
    	
    	Tag[] tags = doc.tags(name);
    	
    	if (check && tags.length <= 0) {
    		throw new IllegalStateException("api doc:Tag[@"+name+"] must requied in position: " + doc.position());
    	} 
    	
    	if (tags.length <= 0) {
    		return null;
    	} else {
    		return tags;
    	}
    }
    
    private static String getTagText(Doc doc, String name, boolean check) {
    	Tag[] tag = getTag(doc, name, check);
    	return (tag == null || tag.length == 0) ? "" : tag[0].text();
    }
    
    private static void saveToFile(Object apiDoc, String name) throws Exception {
    	name = name + "_" + UUID.randomUUID().toString();
    	
    	File docFile = new File(docMetaDataDir.getAbsoluteFile() + File.separator + name + ".json");
    	OutputStreamWriter osw = null;
		try {
			osw = new OutputStreamWriter(new FileOutputStream(docFile), DOC_ENCODING);
			osw.write(new GsonBuilder().setPrettyPrinting().create().toJson(apiDoc));
		} finally {
			if (osw != null) {
				osw.close();
			}
		}
    }
}
