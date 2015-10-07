package com.github.vincent_fuchs.utils;


import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesUtils {

	public static Map<String, String> propertiesToMap(Properties properties) {
		Map<String,String> props = new HashMap<String,String>();
	
		for (final String name: properties.stringPropertyNames()){
			props.put(name, properties.getProperty(name));
		}
		return props;
	}

}