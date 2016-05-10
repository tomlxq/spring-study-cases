package com.example;

/**
 * Created by tom on 2016/5/10.
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class MailServiceImpl implements MailService {
    private JavaMailSenderImpl sender;

    public MailServiceImpl() {}

    public MailServiceImpl(MailServerVO mailServer) {
        setMailServer(mailServer);
    }

    @Override
    public MailServerVO getMailServer() {
        return Util.convert(sender);
    }

    @Override
    public void setMailServer(MailServerVO mailServer) {
        if(sender==null)
            sender = new JavaMailSenderImpl();
        sender = Util.update(sender, mailServer);
    }

    @Override
    public void sendMail(MailVO mail) {
        if(mail!=null) {
            MimeMessage msg = Util.convert(sender, mail);
            sender.send(msg);
        }
    }

    @Override
    public void sendMails(List<MailVO> mails) {
        if(mails!=null && !mails.isEmpty()) {
            List<MimeMessage> list = new ArrayList<>();
            mails.forEach(mail -> list.add(Util.convert(sender, mail)));
            sender.send(list.toArray(new MimeMessage[mails.size()]));
        }
    }
}
