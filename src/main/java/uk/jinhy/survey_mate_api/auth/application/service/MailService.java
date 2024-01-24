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
import uk.jinhy.survey_mate_api.auth.presentation.dto.MailControllerDTO;
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

    public void sendEmail(MailControllerDTO mailDto, String code) {
        String emailContent = generateEmailContent(code);
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true, "UTF-8");
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(mailDto.getReceiver());
            mimeMessageHelper.setSubject("학교 계정 이메일 인증 코드 전송");
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

    private String generateEmailContent(String code){
        Context context = new Context();
        context.setVariable("code", code);
        String emailTemplate = "SurveyEmail.html";
        return springTemplateEngine.process(emailTemplate, context);
    }

}
