package uk.jinhy.survey_mate_api.auth.application.service;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.context.Context;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.spring6.SpringTemplateEngine;
import uk.jinhy.survey_mate_api.auth.presentation.dto.CertificateCodeRequestDTO;
import uk.jinhy.survey_mate_api.common.response.Status;
import uk.jinhy.survey_mate_api.common.response.exception.GeneralException;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MailService {

    @Qualifier("mailSender")
    private final JavaMailSender emailSender;

    private final SpringTemplateEngine springTemplateEngine;

    @Value("${spring.mail.username}")
    private String sender;

    public void sendEmail(CertificateCodeRequestDTO requestDTO, String code) {
        String emailContent = generateEmailContent(code, requestDTO.getTitle());
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true, "UTF-8");
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(requestDTO.getReceiver());
            mimeMessageHelper.setSubject(requestDTO.getMailSubject());
            mimeMessageHelper.setText(emailContent, true);
            mimeMessageHelper.addInline("logo", new ClassPathResource("images/logo.png"));
            emailSender.send(message);
        } catch (RuntimeException e) {
            log.info(e.toString());
            throw new GeneralException(Status.MAIL_SEND_FAIL);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateEmailContent(String code, String title){
        Context context = new Context();
        context.setVariable("code", code);
        context.setVariable("title", title);
        String emailTemplate = "SurveyEmail.html";
        return springTemplateEngine.process(emailTemplate, context);
    }

}
