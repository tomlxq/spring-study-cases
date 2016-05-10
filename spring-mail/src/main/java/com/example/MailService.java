package com.example;

/**
 * Created by tom on 2016/5/10.
 */
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.List;

public interface MailService {
    public MailServerVO getMailServer();
    public void setMailServer(MailServerVO mailServer);
    void sendMail(MailVO mail);
    void sendMails(List<MailVO> mail);
}