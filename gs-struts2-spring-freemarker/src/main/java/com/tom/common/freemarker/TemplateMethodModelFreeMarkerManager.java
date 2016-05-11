package com.tom.common.freemarker;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.apache.struts2.views.freemarker.FreemarkerManager;

import javax.servlet.ServletContext;

/**
 * User: TOM
 * Date: 11-5-4
 * Time: 下午6:04
 * Email: beauty9235@gmail.com
 */
public class TemplateMethodModelFreeMarkerManager extends FreemarkerManager {
    protected Configuration createConfiguration(ServletContext servletContext)
            throws TemplateException {
        Configuration configuration = super.createConfiguration(servletContext);
        configuration.setSharedVariable("encode",new TemplateMethodModelBase64Encode());
        configuration.setSharedVariable("decode",new TemplateMethodModelBase64Decode());
        configuration.setSharedVariable("md5",new TemplateMethodModelMd5());
        configuration.setSharedVariable("random",new TemplateMethodModelRandom());
        configuration.setSharedVariable("acceptLanguage",new TemplateMethodModelLang());
        return configuration;
    }
}