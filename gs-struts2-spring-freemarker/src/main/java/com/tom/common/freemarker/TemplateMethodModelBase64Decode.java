package com.tom.common.freemarker;


import com.tom.common.StringUtil;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

import java.util.List;

/**
 * User: TOM
 * Date: 11-4-28
 * Time: 下午2:37
 * Email: beauty9235@gmail.com
 */
public class TemplateMethodModelBase64Decode implements TemplateMethodModel {
    public Object exec(List args) throws TemplateModelException {

        return StringUtil.base64Decode((String) args.get(0));
    }
}
