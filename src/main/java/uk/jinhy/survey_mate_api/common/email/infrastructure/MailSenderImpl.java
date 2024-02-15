package uk.jinhy.survey_mate_api.common.email.infrastructure;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import uk.jinhy.survey_mate_api.common.email.service.MailSender;
import uk.jinhy.survey_mate_api.common.email.service.dto.MailSenderDTO.SendMailDTO;
import uk.jinhy.survey_mate_api.common.response.Status;
import uk.jinhy.survey_mate_api.common.response.exception.GeneralException;

@Slf4j
@RequiredArgsConstructor
@Component
public class MailSenderImpl implements MailSender {

    @Qualifier("mailSender")
    private final JavaMailSender emailSender;

    @Override
    public void sendMail(SendMailDTO dto) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true, "UTF-8");
            mimeMessageHelper.setFrom(dto.getFrom());
            mimeMessageHelper.setTo(dto.getTo());
            mimeMessageHelper.setSubject(dto.getSubject());
            mimeMessageHelper.setText(dto.getContent(), true);
            mimeMessageHelper.addInline("logo", new ClassPathResource("images/logo.png"));
            emailSender.send(message);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            throw new GeneralException(Status.MAIL_SEND_FAIL);
        } catch (MessagingException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
