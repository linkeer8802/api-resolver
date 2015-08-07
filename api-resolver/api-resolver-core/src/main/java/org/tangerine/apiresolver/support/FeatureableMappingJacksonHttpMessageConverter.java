package org.tangerine.apiresolver.support;

import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;

public class FeatureableMappingJacksonHttpMessageConverter extends MappingJacksonHttpMessageConverter{
	
	public FeatureableMappingJacksonHttpMessageConverter() {
	}
	
	public FeatureableMappingJacksonHttpMessageConverter(String[] enableFeatures, String[] disableFeatures) {
		super();
		
		if (enableFeatures != null) {
			getObjectMapper().enable(convertToFeatures(enableFeatures));
		}
		if (disableFeatures != null) {
			getObjectMapper().disable(convertToFeatures(disableFeatures));
		}
		
	}
	
	public Feature[] convertToFeatures(String[] values) {

		Feature[] features = new Feature[values.length];
		
		for (int i = 0; i < values.length; i++) {
			features[i] =  Enum.valueOf(Feature.class, values[i]);
		}
		
		return features;
	}
}
