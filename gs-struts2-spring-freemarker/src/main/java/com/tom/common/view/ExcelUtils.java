package com.tom.common.view;

import com.opensymphony.xwork2.util.ValueStack;
import com.tom.common.StringUtil;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.LazyDynaBean;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 * User: TOM
 * Date: 2016/5/12
 * email: beauty9235@gmail.com
 * Time: 10:36
 */
public class ExcelUtils {
    static Logger logger = LoggerFactory.getLogger(ExcelUtils.class);
    /**
     * File name, default value use current time in milliseconds
     */
    public static final String FILENAME = "FILENAME";
    /**
     * Date Model Sign
     */
    public static final String DATA = "beans";
    static ThreadLocal context = new ThreadLocal();

    /**
     * set global context
     *
     * @param ctx DynaBean
     */
    public static void setContext(DynaBean ctx) {
        context.set(ctx);
    }

    /**
     * get a global context, it's thread safe
     *
     * @return DynaBean
     */
    public static DynaBean getContext() {
        DynaBean ctx = (DynaBean) context.get();
        if (null == ctx) {
            ctx = new LazyDynaBean();
            setContext(ctx);
        }
        return ctx;
    }

    /**
     * add a object to default context
     *
     * @param key
     * @param value
     */
    public static void addValue(String key, Object value) {
        getContext().set(key, value);
    }

    /**
     * Execute this result, using the specified template location. <p/>The
     * template location has already been interoplated for any variable
     * substitutions <p/>this method obtains the excel template and the object
     * wrapper from ValueStack.
     */
    public static Object buildContextObject(ValueStack stack) {
        DynaBean context = new OgnlDynaBean(stack);
        // add default to context
        try {
            DynaProperty properties[] = ExcelUtils.getContext().getDynaClass().getDynaProperties();
            for (int i = 0; i < properties.length; i++) {
                Object value = ExcelUtils.getContext().get(properties[i].getName());
                if (null != value) {
                    logger.debug("{}:{}", properties[i].getName(), value);
                    context.set(properties[i].getName(), value);
                }
            }
        } catch (Exception e) {
        }
        return context;
    }

    public static String getExcelFileName(Object context) {
        String filename = (String) ((DynaBean) context).get(FILENAME);
        if (StringUtils.isEmpty(filename)) {
            filename = StringUtil.random(20);// String.valueOf(System.currentTimeMillis());
        }
        return filename;
    }

    public static void export(InputStream inputStream, Object context, OutputStream out) throws Exception {
        try {
            Map beanParams = (Map) ((DynaBean) context).get(DATA);
            XLSTransformer transformer = new XLSTransformer();
            org.apache.poi.ss.usermodel.Workbook wb = transformer.transformXLS(inputStream, beanParams);
            wb.write(out);

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
