package org.tangerine.apiresolver.util;

import java.io.Reader;
import java.lang.reflect.Type;

import com.google.gson.GsonBuilder;

public class JsonUtil {

	private static GsonBuilder gsonBuilder = new GsonBuilder();
	
	public static String toHtmlPrettyJson(Object object) {
		String json = new GsonBuilder().setPrettyPrinting().create().toJson(object);
		json = json.replace("\n", "<br/>");
		json = json.replace(" ", "&nbsp;");
		return json;
	}
	
	public static String toPrettyJson(Object object) {
		GsonBuilder _gsonBuilder = new GsonBuilder();
		_gsonBuilder.serializeNulls();
		_gsonBuilder.setPrettyPrinting();
		return _gsonBuilder.create().toJson(object);
	}
	
	public static <T> T fromJson(String json, Class<T> classOfT) {
		return gsonBuilder.create().fromJson(json, classOfT);
	}
	
	public static <T> T fromJson(Reader reader, Type typeOfT) throws Exception {
		return gsonBuilder.create().fromJson(reader, typeOfT);
	}
	
	public static String toJson(Object src) {
		return gsonBuilder.create().toJson(src);
	}
}
