package uk.jinhy.survey_mate_api.common.email.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import uk.jinhy.survey_mate_api.common.email.service.dto.MailSenderDTO;
import uk.jinhy.survey_mate_api.common.email.service.dto.MailServiceDTO.SendEmailDTO;

@Component
@RequiredArgsConstructor
public class MailService {

    private final MailSender mailSender;
    private final TemplateRenderer templateRenderer;

    public void sendEmail(SendEmailDTO sendEmailDTO) {
        String content = templateRenderer.render(
            sendEmailDTO.getTemplateFileName(),
            sendEmailDTO.getTemplateContext()
        );
        MailSenderDTO.SendMailDTO sendMailDTO = MailSenderDTO.SendMailDTO.builder()
            .content(content)
            .to(sendEmailDTO.getReceiver())
            .subject(sendEmailDTO.getSubject())
            .build();
        mailSender.sendMail(sendMailDTO);
    }
}
