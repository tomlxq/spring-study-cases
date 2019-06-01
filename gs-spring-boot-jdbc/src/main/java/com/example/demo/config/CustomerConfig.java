package com.example.demo.config;

import com.google.common.collect.Maps;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

@Configuration
//@PropertySource("classpath:jdbc.properties,classpath:system.properties")
public class CustomerConfig extends PropertyPlaceholderConfigurer {
    private static final String PLACEHOLDER_START = "${";
    protected static Map<String, String> ctx;

    /**
     * 解析占位符
     *
     * @param properties
     */
    public static void resolvePlaceHolders(Properties properties) {
        Iterator itr = properties.entrySet().iterator();
        while (itr.hasNext()) {
            final Map.Entry entry = (Map.Entry) itr.next();
            final Object value = entry.getValue();
            if (value != null && String.class.isInstance(value)) {
                final String resolved = resolvePlaceHolder((String) value);
                if (!value.equals(resolved)) {
                    if (resolved == null) {
                        itr.remove();
                    } else {
                        entry.setValue(resolved);
                    }
                }
            }
        }
    }

    /**
     * 解析占位符具体操作
     *
     * @param property
     * @return
     */
    public static String resolvePlaceHolder(String property) {
        if (property.indexOf(PLACEHOLDER_START) < 0) {
            return property;
        }
        StringBuffer buff = new StringBuffer();
        char[] chars = property.toCharArray();
        for (int pos = 0; pos < chars.length; pos++) {
            if (chars[pos] == '$') {
                // peek ahead
                if (chars[pos + 1] == '{') {
                    // we have a placeholder, spin forward till we find the end
                    String systemPropertyName = "";
                    int x = pos + 2;
                    for (; x < chars.length && chars[x] != '}'; x++) {
                        systemPropertyName += chars[x];
                        // if we reach the end of the string w/o finding the
                        // matching end, that is an exception
                        if (x == chars.length - 1) {
                            throw new IllegalArgumentException("unmatched placeholder start [" + property + "]");
                        }
                    }
                    String systemProperty = extractFromSystem(systemPropertyName);
                    buff.append(systemProperty == null ? "" : systemProperty);
                    pos = x + 1;
                    // make sure spinning forward did not put us past the end of the buffer...
                    if (pos >= chars.length) {
                        break;
                    }
                }
            }
            buff.append(chars[pos]);
        }
        String rtn = buff.toString();
        return isEmpty(rtn) ? null : rtn;
    }

    /**
     * 获得系统属性 当然 你可以选择从别的地方获取值
     *
     * @param systemPropertyName
     * @return
     */
    private static String extractFromSystem(String systemPropertyName) {
        try {
            return System.getProperty(systemPropertyName);
        } catch (Throwable t) {
            return null;
        }
    }

    /**
     * 判断字符串的空(null或者.length=0)
     *
     * @param string
     * @return
     */
    public static boolean isEmpty(String string) {
        return string == null || string.length() == 0;
    }

    public static Set<String> getKeys() {
        return ctx.keySet();
    }

    public static String getValue(String key) {
        return MapUtils.getString(ctx, key);
    }

    public static int getInt(String key) {
        return MapUtils.getInteger(ctx, key);
    }

    public static boolean getBoolean(String key) {
        return MapUtils.getBoolean(ctx, key);
    }

    public static long getLong(String key) {
        return MapUtils.getLong(ctx, key);
    }

    @Bean
    public PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
        propertySourcesPlaceholderConfigurer.setLocations(new ClassPathResource("jdbc.properties"), new ClassPathResource("system.properties"));
        //propertySourcesPlaceholderConfigurer.setIgnoreUnresolvablePlaceholders(true);
        //propertySourcesPlaceholderConfigurer.setIgnoreResourceNotFound(true);
        return propertySourcesPlaceholderConfigurer;
    }

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {

        super.processProperties(beanFactoryToProcess, props);
        ctx = Maps.newHashMap();

        props.keySet().iterator().forEachRemaining(key -> ctx.put(key.toString(), props.getProperty(key.toString())));
        ctx.keySet().forEach(key -> System.out.println(key + "=" + ctx.get(key)));
    }
}
