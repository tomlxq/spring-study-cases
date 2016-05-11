package com.tom.common.freemarker;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;

/**
 * User: TOM
 * Date: 12-6-29
 * Time: 上午11:52
 * Email: beauty9235@gmail.com
 */

public class TemplateMethodModelLang implements TemplateMethodModel {


    public Object exec(List args) throws TemplateModelException {
        //在任意的class下通过以下方法获取到HttpServletRequest
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        //HttpServletRequest re= ServletActionContext.getRequest();
       // if(re==null)return null;
        Locale clientLocale = request.getLocale();
        return  clientLocale;
       //return re.getHeader("Accept-Language");

    }

}
