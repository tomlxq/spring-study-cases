package com.tom.common.freemarker;

/**
 * User: TOM
 * Date: 11-4-28
 * Time: 下午2:28
 * Email: beauty9235@gmail.com
 */


import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

import java.util.List;
import com.tom.common.StringUtil;
public class TemplateMethodModelBase64Encode implements TemplateMethodModel {
    public Object exec(List args) throws TemplateModelException {
        return StringUtil.base64Encode((String) args.get(0));
    }
}


