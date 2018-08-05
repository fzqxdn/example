package com.cln.utils;

import java.io.IOException;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class Configration
{
	private static Logger log = LoggerFactory.getLogger(Configration.class.getName());



    private static Properties config = null;

    /**
     * 返回系统res.properties配置信息
     * 
     * @param key
     *            key值
     * @return value值
     */
    public static String getProperty(String key) {
        if (config == null)
        {
            synchronized (Configration.class)
            {
                if (null == config)
                {
                    try
                    {
                        Resource resource = new ClassPathResource("config.properties");
                        config = PropertiesLoaderUtils.loadProperties(resource);
                    }
                    catch (IOException e)
                    {
                    	log.error(e.getMessage(), e);
                    }
                }
            }
        }
        return config.getProperty(key);
    }

}
