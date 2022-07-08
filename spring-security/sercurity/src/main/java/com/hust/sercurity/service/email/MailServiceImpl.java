package com.hust.sercurity.service.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import org.thymeleaf.spring5.SpringTemplateEngine;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


@Service
public class MailServiceImpl implements EmailServiceInterface{
    @Autowired
    JavaMailSender mailSender;

    @Autowired
    SpringTemplateEngine templateEngine;

    @Override
    public String sendHtmlEmail(final String link,String toMail) throws MessagingException {
        final Context ctx = new Context();
        ctx.setVariable("link",link);
        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper message =
                new MimeMessageHelper(mimeMessage, true, "UTF-8"); // true = multipart
        message.setSubject("Confirmation Email");
        message.setTo(toMail);


        // Create the HTML body using Thymeleaf
        final String htmlContent = this.templateEngine.process("confirm", ctx);

        message.setText(htmlContent, true); // true = isHtml

        // Send mail
        this.mailSender.send(mimeMessage);

        return "Email Sent!";
    }
}