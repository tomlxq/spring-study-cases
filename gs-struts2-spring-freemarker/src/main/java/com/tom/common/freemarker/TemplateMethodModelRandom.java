package com.tom.common.freemarker;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.List;

/**
 * User: TOM
 * Date: 12-6-29
 * Time: 上午11:52
 * Email: beauty9235@gmail.com
 */

public class TemplateMethodModelRandom implements TemplateMethodModel {
    public Object exec(List args) throws TemplateModelException {
        int size=6;
        String seed="0123456789ABCDEF";
        if(args!=null){
            if(args.size()==1||args.size()==2){
                size=Integer.parseInt(args.get(0).toString());
            }
           if(args.size()==2){
                seed=args.get(1).toString();
            }
        }
        return   RandomStringUtils.random(size,seed);
    }
}
