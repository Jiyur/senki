package com.abc.senki.service.implement;

import com.abc.senki.model.entity.UserEntity;
import com.abc.senki.security.jwt.JwtUtils;
import com.abc.senki.service.EmailService;

import freemarker.template.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


@Component
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private Configuration config;
    @Value("${apps.server.host}")
    private String host;


    @Override
    public void sendForgetPasswordMessage(String host,UserEntity user) {
        MimeMessage message = javaMailSender.createMimeMessage();
        String subject = "Reset password";
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());

            Template template= config.getTemplate("email-temp.ftl");
            Map<String, Object> model=new HashMap<>();
            model.put("fullName",user.getFullName());
            model.put("link",host+"/token="+jwtUtils.generateEmailJwtToken(user.getEmail()));
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            helper = new MimeMessageHelper(message, true);
            helper.setFrom("Senki");
            helper.setTo(user.getEmail());
            helper.setSubject(subject);
            helper.setText(html,true);
            javaMailSender.send(message);

//        final String username = "vmlej31287@gmail.com";
//        final String password = "nhvlwdydflkdguxx";
//
//        Properties prop = new Properties();
//        prop.put("mail.smtp.host", "smtp.gmail.com");
//        prop.put("mail.smtp.port", "587");
//        prop.put("mail.smtp.auth", "true");
//        prop.put("mail.smtp.starttls.enable", "true"); //TLS
//
//        Session session = Session.getInstance(prop,
//                new javax.mail.Authenticator() {
//                    protected PasswordAuthentication getPasswordAuthentication() {
//                        return new PasswordAuthentication(username, password);
//                    }
//                });
//
//        try{
//            Template template= config.getTemplate("email-temp.ftl");
//            Map<String, Object> model=new HashMap<>();
//            model.put("fullName",user.getFullName());
//            model.put("link",host+"?token="+jwtUtils.generateEmailJwtToken(user.getEmail()));
//            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
//            Message message = new MimeMessage(session);
//            message.setFrom(new InternetAddress("vmlej31287@gmail.com"));
//            message.setRecipients(
//                    Message.RecipientType.TO,
//                    InternetAddress.parse(user.getEmail())
//            );
//            message.setSubject("Reset password");
//            message.setText(html);
//            Transport.send(message);
//
//
        } catch (MessagingException | IOException | TemplateException e) {
            e.printStackTrace();
        }

    }


}

