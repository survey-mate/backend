package uk.jinhy.survey_mate_api.common.email.infrastructure;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.resource.Emailv31;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import uk.jinhy.survey_mate_api.common.email.service.MailSender;
import uk.jinhy.survey_mate_api.common.email.service.dto.MailSenderDTO.SendMailDTO;
import uk.jinhy.survey_mate_api.common.response.Status;
import uk.jinhy.survey_mate_api.common.response.exception.GeneralException;

@Slf4j
@RequiredArgsConstructor
@Component
public class MailSenderImpl implements MailSender {

    @Override
    public void sendMail(SendMailDTO dto) {
        MailjetRequest request;
        MailjetResponse response;

        ClientOptions options = ClientOptions.builder()
            .apiKey(System.getenv("MAIL_ACCESS_KEY"))
            .apiSecretKey(System.getenv("MAIL_SECRET_KEY"))
            .build();

        MailjetClient client = new MailjetClient(options);

        request = new MailjetRequest(Emailv31.resource)
            .property(Emailv31.MESSAGES, new JSONArray()
                .put(new JSONObject()
                    .put(Emailv31.Message.FROM, new JSONObject()
                        .put("Email", "no_reply@jinhy.uk")
                        .put("Name", "썰매(Survey Mate)"))
                    .put(Emailv31.Message.TO, new JSONArray()
                        .put(new JSONObject()
                            .put("Email", dto.getTo())
                            .put("Name", "You")))
                    .put(Emailv31.Message.SUBJECT, dto.getSubject())
                    .put(Emailv31.Message.HTMLPART, dto.getContent())));
        try {
            response = client.post(request);
        } catch (MailjetException e) {
            log.error(e.getMessage(), e);
            throw new GeneralException(Status.MAIL_SEND_FAIL);
        }
    }
}

