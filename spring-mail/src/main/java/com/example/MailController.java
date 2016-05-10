package com.example;

/**
 * Created by tom on 2016/5/10.
 */
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
public class MailController {
    Logger logger= LoggerFactory.getLogger(MailController.class);
    @Autowired
    private MailService service;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public HttpEntity  home() {
        ResourceSupport home = new ResourceSupport();
        home.add(linkTo(methodOn(MailController.class).home()).withSelfRel());
        home.add(linkTo(methodOn(MailController.class).sendMail(null)).withRel("mail"));
        home.add(linkTo(methodOn(MailController.class).sendMails(null)).withRel("mails"));
        home.add(linkTo(methodOn(MailController.class).getMailServer()).withRel("server"));
        return new ResponseEntity(home, HttpStatus.OK);
    }

    @RequestMapping(value = "/mail/server", method = RequestMethod.GET)
    @ResponseBody
    public HttpEntity getMailServer() {
        MailServerVO mailServer = service.getMailServer();
        mailServer.add(linkTo(methodOn(MailController.class).getMailServer()).withSelfRel());
        mailServer.add(linkTo(methodOn(MailController.class).setMailServer(null)).withRel("server"));
        return new ResponseEntity(mailServer, HttpStatus.OK);
    }

    @RequestMapping(value = "/mail/server", method = RequestMethod.POST)
    @ResponseBody
    public HttpEntity setMailServer(@RequestBody MailServerVO newMailServer) {
        if(newMailServer!=null) {
            service.setMailServer(newMailServer);
        }
        MailServerVO mailServer = service.getMailServer();
        mailServer.add(linkTo(methodOn(MailController.class).getMailServer()).withSelfRel());
        mailServer.add(linkTo(methodOn(MailController.class).setMailServer(null)).withRel("update"));
        return new ResponseEntity(mailServer, HttpStatus.OK);
    }

    @RequestMapping(value = "/mail", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity sendMail(@RequestBody MailVO mail) {
        logger.info("{}",mail);
        if(mail!=null) {
            service.sendMail(mail);
        }
        return new ResponseEntity(mail, HttpStatus.OK);
    }

    @RequestMapping(value = "/mails", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity sendMails(@RequestBody List mails) {
        if(mails!=null) {
            service.sendMails(mails);
        }
        return new ResponseEntity(mails, HttpStatus.OK);
    }

}
