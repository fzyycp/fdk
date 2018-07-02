package cn.faury.fdk.pay.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

/**
 * .properties 属性文件配置加载工具
 */
public class Configs {
	
	private Logger logger = LoggerFactory.getLogger(Configs.class);
	
	private Map<String, String> props = new HashMap<String, String>();
	
	private static final Configs configs = new Configs();
	
	private Configs() {
		loadPropertyFilesFromClassPath();
	}

	public static Configs me() {
		return configs;
	}
	
	private void loadPropertyFilesFromClassPath(){
		logger.error("=====Configs Start loadPropertyFilesFromClassPath =====");
		String path = Configs.class.getResource("").getPath();
		
		int index = path.indexOf("WEB-INF");
		
		if (index == -1) {
			path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		} else {
			path = path.substring(path.startsWith("file:") ? 5 : 0, index)
					+ "WEB-INF" + File.separator + "classes";
		}
		
		File dir = new File(path);
		
		if (dir.exists() && dir.isDirectory()) {
			File[] files = dir.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.endsWith(".properties");
				}
			});
			for (File file : files) {
				logger.debug("加载配置文件:" + file.getName());
				loadProperty(file);
			}
		}
		logger.error("=====Configs Finish loadPropertyFilesFromClassPath =====");
	}
	
	private void loadProperty(File propertyFile){
		
		Properties property = new Properties();

		InputStream is = null;

		try {
			is = new FileInputStream(propertyFile);

			property.load(is);

		} catch (IOException e) {
			
			throw new IllegalArgumentException("属性文件无法加载");
			
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (Exception e2) {
			}
		}
		if (property != null) {
			for (Entry<Object, Object> entry : property.entrySet()) {
				props.put(entry.getKey().toString(), entry.getValue()
						.toString());
			}
		}
	}
	
	public void loadProperty(String propertyFileName){
		
		if (propertyFileName == null || "".equals(propertyFileName)) {
			throw new IllegalArgumentException("属性文件不能为空");
		}
		if (propertyFileName.contains("..")) {
			throw new IllegalArgumentException("属性文件不能包含 \"..\"");
		}
		
		Properties property = new Properties();
		
		InputStream is = null;
		
		try {
			is = this.getClass().getClassLoader().getResourceAsStream(propertyFileName);
			
			property.load(is);
			
		} catch (IOException e) {
			throw new IllegalArgumentException( "属性文件无法加载");
		} finally {
			try {
				if(is!=null) is.close();
			} catch (Exception e2) {
			}
		}
		if (property != null) {
			for (Entry<Object, Object> entry : property.entrySet()) {
				props.put(entry.getKey().toString(), entry.getValue().toString());
			}
		}
	}
	
	public String getProperty(String key) {
		if (this.props.containsKey(key)) {
			return props.get(key);
		}
		return null;

	}

	public String getProperty(String key, String defaultValue) {
		if (this.props.containsKey(key)) {
			return props.get(key);
		}
		return defaultValue;

	}

	public Integer getPropertyToInt(String key) {
		Integer resultInt = null;
		String resultStr = this.getProperty(key);
		if (resultStr != null)
			resultInt = Integer.parseInt(resultStr);
		return resultInt;
	}

	public Integer getPropertyToInt(String key, Integer defaultValue) {
		Integer result = getPropertyToInt(key);
		return result != null ? result : defaultValue;
	}

	public Boolean getPropertyToBoolean(String key) {
		String resultStr = this.getProperty(key);
		Boolean resultBool = null;
		if (resultStr != null) {
			if (resultStr.trim().equalsIgnoreCase("true"))
				resultBool = true;
			else if (resultStr.trim().equalsIgnoreCase("false"))
				resultBool = false;
		}
		return resultBool;

	}

	public Boolean getPropertyToBoolean(String key, boolean defaultValue) {
		Boolean result = getPropertyToBoolean(key);
		return result != null ? result : defaultValue;
	}
}
