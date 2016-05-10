package com.example;

/**
 * Created by tom on 2016/5/10.
 */
import org.springframework.hateoas.ResourceSupport;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;


public class MailVO extends ResourceSupport implements Serializable {
    private String subject;
    private String plainText;
    private String htmlText;
    private String charset;
    private boolean multipart = true;

    private AddressVO[] to;
    private AddressVO[] cc;
    private AddressVO[] bcc;
    private AddressVO from;
    private AddressVO replyTo;
  //  private List attachments;
    public MailVO() {}

    public MailVO(
            String subject,
            String plainText,
            String htmlText,
            String charset,
            boolean multipart,
            AddressVO[] to,
            AddressVO[] cc,
            AddressVO[] bcc,
            AddressVO from,
            AddressVO replyTo) {
        this.subject = subject;
        this.plainText = plainText;
        this.htmlText = htmlText;
        this.charset = charset;
        this.multipart = multipart;
        this.to = to;
        this.cc = cc;
        this.bcc = bcc;
        this.from = from;
        this.replyTo = replyTo;

    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPlainText() {
        return plainText;
    }

    public void setPlainText(String plainText) {
        this.plainText = plainText;
    }

    public String getHtmlText() {
        return htmlText;
    }

    public void setHtmlText(String htmlText) {
        this.htmlText = htmlText;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public boolean isMultipart() {
        return multipart;
    }

    public void setMultipart(boolean multipart) {
        this.multipart = multipart;
    }

    public AddressVO[] getTo() {
        return to;
    }

    public void setTo(AddressVO[] to) {
        this.to = to;
    }

    public AddressVO[] getCc() {
        return cc;
    }

    public void setCc(AddressVO[] cc) {
        this.cc = cc;
    }

    public AddressVO[] getBcc() {
        return bcc;
    }

    public void setBcc(AddressVO[] bcc) {
        this.bcc = bcc;
    }

    public AddressVO getFrom() {
        return from;
    }

    public void setFrom(AddressVO from) {
        this.from = from;
    }

    public AddressVO getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(AddressVO replyTo) {
        this.replyTo = replyTo;
    }

    @Override
    public String toString() {
        return "MailVO{" +
                "subject='" + subject + '\'' +
                ", plainText='" + plainText + '\'' +
                ", htmlText='" + htmlText + '\'' +
                ", charset='" + charset + '\'' +
                ", multipart=" + multipart +
                ", to=" + Arrays.toString(to) +
                ", cc=" + Arrays.toString(cc) +
                ", bcc=" + Arrays.toString(bcc) +
                ", from=" + from +
                ", replyTo=" + replyTo +
                "} ";
    }
}