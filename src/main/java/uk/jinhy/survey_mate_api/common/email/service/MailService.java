package uk.jinhy.survey_mate_api.common.email.service;


import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.CodePointLength;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.jinhy.survey_mate_api.common.email.service.dto.MailSenderDTO;
import uk.jinhy.survey_mate_api.common.email.service.dto.MailServiceDTO.SendEmailDTO;

@Component
@RequiredArgsConstructor
public class MailService {

    private final MailSender mailSender;
    private final TemplateRenderer templateRenderer;

    @Value("${spring.mail.username}")
    private String sender;

    public void sendEmail(SendEmailDTO sendEmailDTO) {
        String content = templateRenderer.render(
            sendEmailDTO.getTemplateFileName(),
            sendEmailDTO.getTemplateContext()
        );
        MailSenderDTO.SendMailDTO sendMailDTO = MailSenderDTO.SendMailDTO.builder()
            .content(content)
            .from(sender)
            .to(sendEmailDTO.getReceiver())
            .subject(sendEmailDTO.getSubject())
            .build();
        mailSender.sendMail(sendMailDTO);
    }
}
