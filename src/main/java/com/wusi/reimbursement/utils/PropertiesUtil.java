package com.wusi.reimbursement.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Properties;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public final class PropertiesUtil extends PropertyPlaceholderConfigurer {
    private static final byte[] KEY = new byte[]{9, -1, 0, 5, 39, 8, 6, 19};
    private static Map<String, String> ctxPropertiesMap;
    private List<String> decryptProperties;

    public PropertiesUtil() {
    }

    protected void loadProperties(Properties props) throws IOException {
        super.loadProperties(props);
        ctxPropertiesMap = new HashMap();

        String keyStr;
        String value;
        for(Iterator var2 = props.keySet().iterator(); var2.hasNext(); ctxPropertiesMap.put(keyStr, value)) {
            Object key = var2.next();
            keyStr = key.toString();
            value = props.getProperty(keyStr);
            if (this.decryptProperties != null && this.decryptProperties.contains(keyStr)) {
                props.setProperty(keyStr, value);
            }
        }

    }

    public void setDecryptProperties(List<String> decryptProperties) {
        this.decryptProperties = decryptProperties;
    }

    public static String getString(String key) {
        try {
            return (String)ctxPropertiesMap.get(key);
        } catch (MissingResourceException var2) {
            return null;
        }
    }

    public static int getInt(String key) {
        return Integer.parseInt((String)ctxPropertiesMap.get(key));
    }

    public static int getInt(String key, int defaultValue) {
        String value = (String)ctxPropertiesMap.get(key);
        return StringUtils.isBlank(value) ? defaultValue : Integer.parseInt(value);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        String value = (String)ctxPropertiesMap.get(key);
        return StringUtils.isBlank(value) ? defaultValue : new Boolean(value);
    }

}
