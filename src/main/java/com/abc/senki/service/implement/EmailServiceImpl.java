package com.abc.senki.service.implement;

import com.abc.senki.model.entity.UserEntity;
import com.abc.senki.security.jwt.JwtUtils;
import com.abc.senki.service.EmailService;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import freemarker.template.*;
import lombok.SneakyThrows;
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

    @Autowired
    private SendGrid sendGrid;

    @Value("${sendgrid.template-id}")
    private String templateId;


    @Override
    public void sendForgetPasswordMessage(String host,UserEntity user) {
        MimeMessage message = javaMailSender.createMimeMessage();
        String subject = "Reset password";
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());

            Template template= config.getTemplate("email-temp.ftl");
            Map<String, Object> model=new HashMap<>();
            model.put("fullName","USER");

            if(user.getFullName()!=null){
                model.put("fullName",user.getFullName());
            }

            model.put("link",host+"/reset?token="+jwtUtils.generateEmailJwtToken(user.getEmail()));
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            helper = new MimeMessageHelper(message, true);
            helper.setFrom("Senki");
            helper.setTo(user.getEmail());
            helper.setSubject(subject);
            helper.setText(html,true);
            javaMailSender.send(message);

        } catch (MessagingException | IOException | TemplateException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void sendGridEmail(String host, UserEntity user) {
        Mail mail=prepareEmail(host,user.getEmail());
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");

        try {
            request.setBody(mail.build());
            Response response = sendGrid.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    //Configura prepare email
    public Mail prepareEmail(String host,String email){
        Mail mail = new Mail();
        //Set email from
        Email from = new Email();
        from.setName("Senki");
        from.setEmail("senki@em2738.senki.me");

        //Set email to
        Email to= new Email();
        to.setEmail(email);
        //Set email subject
        Personalization personalization = new Personalization();
        personalization.addTo(to);
        //
        mail.setTemplateId(templateId);
        mail.setFrom(from);
        mail.addPersonalization(personalization);
        personalization.addDynamicTemplateData("reset_link",
                host+"/reset?token="+jwtUtils.generateEmailJwtToken(email));

        return mail;
    }

}

