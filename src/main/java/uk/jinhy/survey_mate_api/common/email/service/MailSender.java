package uk.jinhy.survey_mate_api.common.email.service;

import uk.jinhy.survey_mate_api.common.email.service.dto.MailSenderDTO;

public interface MailSender {

    void sendMail(MailSenderDTO.SendMailDTO dto);
}
