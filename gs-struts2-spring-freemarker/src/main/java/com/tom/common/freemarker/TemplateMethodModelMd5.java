package com.tom.common.freemarker;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.List;

/**
 * User: TOM
 * Date: 12-6-23
 * Time: 上午11:40
 * Email: beauty9235@gmail.com
 */

public class TemplateMethodModelMd5 implements TemplateMethodModel {
    public Object exec(List args) throws TemplateModelException {
        return DigestUtils.md5Hex((String) args.get(0));
    }
}
