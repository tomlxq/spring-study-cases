package com.tom.common.view;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.result.StrutsResultSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * User: TOM
 * Date: 2016/5/12
 * email: beauty9235@gmail.com
 * Time: 10:35
 */
public class ExcelResult extends StrutsResultSupport {
    private static final Logger log = LoggerFactory.getLogger(ExcelResult.class);
    private static final long serialVersionUID = 1L;
    protected String contentType = "application/vnd.ms-excel";

    protected void doExecute(String location, ActionInvocation invocation) throws Exception {
        HttpServletResponse response = ServletActionContext.getResponse();
        response.reset();
        response.setContentType(contentType);
        InputStream in = null;
        try {
            ValueStack stack = invocation.getStack();
            //TOM hack For xls
            in = ServletActionContext.getServletContext().getResourceAsStream(location);
            OutputStream out = response.getOutputStream();
            Object context = ExcelUtils.buildContextObject(stack);
            String filename = ExcelUtils.getExcelFileName(context);
            response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + ".xls\"");
            ExcelUtils.export(in, context, out);
            out.flush();
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }
}