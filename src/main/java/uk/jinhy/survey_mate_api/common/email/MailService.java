package uk.jinhy.survey_mate_api.common.email;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.spring6.SpringTemplateEngine;
import uk.jinhy.survey_mate_api.common.response.Status;
import uk.jinhy.survey_mate_api.common.response.exception.GeneralException;


@Service
@Transactional
@RequiredArgsConstructor
public class MailService {

    @Qualifier("mailSender")
    private final JavaMailSender emailSender;

    private final SpringTemplateEngine springTemplateEngine;

    @Value("${spring.mail.username}")
    private String sender;

    public void sendEmail(MailServiceDTO.SendEmailDTO sendEmailDTO) {
        String content = springTemplateEngine.process(
            sendEmailDTO.getTemplateFileName(),
            sendEmailDTO.getTemplateContext()
        );
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true, "UTF-8");
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(sendEmailDTO.getReceiver());
            mimeMessageHelper.setSubject(sendEmailDTO.getSubject());
            mimeMessageHelper.setText(content, true);
            mimeMessageHelper.addInline("logo", new ClassPathResource("images/logo.png"));
            emailSender.send(message);
        } catch (RuntimeException e) {
            throw new GeneralException(Status.MAIL_SEND_FAIL);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
