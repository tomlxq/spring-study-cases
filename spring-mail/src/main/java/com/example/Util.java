package com.example;

/**
 * Created by tom on 2016/5/10.
 */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class Util {
    static Logger logger= LoggerFactory.getLogger(Util.class);
    public static MailServerVO convert (JavaMailSenderImpl mailServer) {
        if(mailServer!=null) {
            return new MailServerVO(
                    mailServer.getHost(),
                    mailServer.getPort(),
                    mailServer.getUsername(),
                    mailServer.getPassword(),
                    mailServer.getDefaultEncoding(),
                    mailServer.getJavaMailProperties());
        }
        return null;
    }

    public static JavaMailSenderImpl update(
            JavaMailSenderImpl sender, MailServerVO vo) {
        if(vo!=null) {
            Properties javaMailProperties = sender.getJavaMailProperties();

            String host = vo.getHost();
            Integer port = vo.getPort();
            String userName = vo.getUserName();
            String password = vo.getPassword();
            String defaultEncoding = vo.getDefaultEncoding();
            Properties properties = vo.getProperties();

            if (host != null) {
                sender.setHost(host);
            }
            if (port != null) {
                sender.setPort(port);
            }
            if (userName != null) {
                sender.setUsername(userName);
            }
            if (password != null) {
                sender.setPassword(password);
            }
            if (defaultEncoding != null) {
                sender.setDefaultEncoding(defaultEncoding);
            }
            if (properties != null && !properties.isEmpty()) {
                properties.stringPropertyNames().forEach(
                        propKey -> javaMailProperties.setProperty(
                                propKey, properties.getProperty(propKey)));
                sender.setJavaMailProperties(javaMailProperties);
            }
        }
        return sender;
    }

    public static AddressVO convert(InternetAddress addr) {
        if(addr!=null) {
            return new AddressVO(addr.getAddress(),addr.getPersonal());
        }
        return null;
    }

    public static InternetAddress convert(AddressVO vo) {
        if(vo!=null) {
            InternetAddress addr = new InternetAddress();
            addr.setAddress(vo.getAddress());
            if(vo.getPersonal()!=null) {
                try {
                    addr.setPersonal(vo.getPersonal(), "UTF-8");
                } catch (UnsupportedEncodingException e) {

                }
            }
            return addr;
        }
        return null;
    }

    public static InternetAddress[] convert(AddressVO[] vos) {
        if(vos!=null) {
            InternetAddress[] addrs = new InternetAddress[vos.length];

            for (int i = 0; i < vos.length; i++) addrs[i] = convert(vos[i]);
            return addrs;
        }
        return null;
    }


    public static MimeMessage convert(JavaMailSenderImpl sender, MailVO mail) {
        boolean multipart=mail.isMultipart();
        String charset=mail.getCharset();
        InternetAddress from=convert(mail.getFrom());
        //logger.info("{}",mail);
        InternetAddress[] to=convert(mail.getTo());
        //logger.info("{}",to);
        InternetAddress replyTo=convert(mail.getReplyTo());
        InternetAddress[] cc=convert(mail.getCc());
        InternetAddress[] bcc=convert(mail.getBcc());

        String plainText=mail.getPlainText();
        String subject=mail.getSubject();
        String htmlText=mail.getHtmlText();
        // let's attach the infamous windows Sample file (this time copied to c:/)


        //attachments = mail.getAttachments();
        //Map inlineData = mail.getInline();

        if (plainText==null && htmlText==null) {
            throw new IllegalArgumentException(
                    "Please provide plainText or htmlText.");
        }
        //logger.info("{}",validateAddress(to));
        //logger.info("{}",validateAddress(cc));
        if(!validateAddress(to) &&
                !validateAddress(cc) &&
                !validateAddress(bcc)) {
            throw new IllegalArgumentException(
                    "Please provide 'to', 'cc', or 'bcc'.");
        }

        MimeMessage msg = sender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(
                    msg, multipart, charset == null ? "UTF-8" : charset);

            helper.setFrom(from);
            if (replyTo != null) {
                helper.setReplyTo(replyTo);
            }
            if (validateAddress(to)) {
                helper.setTo(to);
            }
            if (validateAddress(cc)) {
                helper.setCc(cc);
            }
            if (validateAddress(bcc)) {
                helper.setBcc(bcc);
            }
            helper.setSubject(subject);

            if(multipart && plainText!=null && htmlText!=null) {
                helper.setText(plainText, htmlText);
            } else if (plainText==null) {
                helper.setText(htmlText, true);
            } else {
                helper.setText(plainText);
            }

            msg = helper.getMimeMessage();
            //FileSystemResource file = new FileSystemResource(new File("c:/Sample.jpg"));
            //helper.addAttachment("CoolImage.jpg", file);

        } catch (MessagingException e) {
            throw new IllegalArgumentException(
                    "Please reconfigure mail content", e);
        }

        return msg;
    }

    private static boolean validateAddress(InternetAddress[] to) {
        return to!=null&&to.length>0?true:false;
    }


}
