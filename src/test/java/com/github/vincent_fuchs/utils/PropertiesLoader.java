package com.github.vincent_fuchs.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.Properties;

public class PropertiesLoader {

	
	public static Properties load(String resourcePath) throws IOException {
		java.io.InputStream in = PropertiesLoader.class.getResourceAsStream(resourcePath);
		try  {
			Properties properties = new Properties();
			properties.load(new InputStreamReader(in, "UTF8"));
			return properties;
		}
		finally {
			in.close();
		}
	}

	public static void write(Properties properties, File file) throws IOException {
		File parent = file.getParentFile();
		parent.mkdirs();
		
		FileOutputStream out = new FileOutputStream(file);
		try  {
			properties.store(new OutputStreamWriter(out, "UTF8"), "ras");
		}
		finally {
			out.close();
		}
	}
	
	/**
	 * Looks for given file in classpath, and overwrites content with provided properties
	 * @param properties
	 * @param file
	 * @throws IOException
	 */
	public static void overwriteInClasspath(String file, Properties properties) throws IOException {
		
		URL fileUrl = PropertiesLoader.class.getResource(file);
				
		FileOutputStream out = new FileOutputStream(new File(fileUrl.getFile()));
		try  {
			properties.store(new OutputStreamWriter(out, "UTF8"), "File has been programmatically written");
		}
		finally {
			out.close();
		}
	}
}
