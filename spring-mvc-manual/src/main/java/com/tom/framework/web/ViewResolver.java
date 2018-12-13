package com.tom.framework.web;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.collections.MapUtils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ViewResolver {
    String view;
    File template;


    public String parse(ModelAndView mv) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(this.template, "r");
            String line = null;
            while (null != (line = randomAccessFile.readLine())) {
                Matcher matcher = getMatcher(line);
                if (matcher.find()) {
                    for (int i = 1; i <= matcher.groupCount(); i++) {
                        String group = matcher.group(i);
                        if (mv.getModel().containsKey(group)) {
                            line = line.replaceAll("\\$\\{" + group + "\\}", MapUtils.getString(mv.getModel(), group));
                        }
                    }
                }
                stringBuffer.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuffer.toString();
    }

    private Matcher getMatcher(String line) {
        Pattern compile = Pattern.compile("\\$\\{(.+?)\\}");
        return compile.matcher(line);
    }
}
