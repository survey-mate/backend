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
                    .put(Emailv31.Message.HTMLPART, dto.getContent())
                    .put(Emailv31.Message.INLINEDATTACHMENTS, new JSONArray()
                        .put(new JSONObject()
                            .put("ContentType", "image/png")
                            .put("Filename", "logo.png")
                            .put("ContentID", "logo")
                            .put("Base64Content",
                                "iVBORw0KGgoAAAANSUhEUgAAAV4AAABECAMAAAD+z61hAAACtVBMVEUAAAD///////////////////////////////////////////////////////////9gRv9hR/9iSP9iSf9jSv9kSv9kS/9lTP9mTf9nTv9nT/9oT/9pUP9pUf9qUv9rUv9rU/9sVP9sVf9tVf9uVv9uV/9vV/9wWP9wWf9xWv5xWv9yW/9zXP90Xf91Xv91X/92X/92YP93Yf94Yv96ZP97Zv98Z/99Z/99aP9/av+Aa/+AbP+BbP+Cbf+Db/+Eb/+EcP+Fcf+Fcv+Gcv+Hc/+HdP+IdP+Jdf+Jdv+Kd/+LeP+Mef+Mev+Oe/+PfP+Pff+Rf/+Tgf+VhP+WhP+Whf+Xhv+Xh/+Yh/+ZiP+Zif+aif+biv+bi/+cjP+djf+ejv+ej/+fj/+gkP+gkf+hkf+hkv+ik/+jlP+jlf2llv+ll/+omv+pm/+qnP+rnf+rnv+sn/+tn/+toP+uof+vov+wo/+wpP+ypf+ypv+zpv+0p/+0qP+1qf+2qv+3q/+3rP+4rP+5rf+5rv+6rv+7sP+8sf+9sv++s/++tP+/tP+/tf/Atv/Btv/Bt/3CuP/Duf/Eu/3Eu//FvP/GvP/Hvv/Ivv/Iv//JwP/Jwf/Kwf/Lwv/Lw//MxP/Nxf/Oxv/Px//QyP3QyP/Qyf/Ryf/Ryv/Sy//Ty//TzP/Uzf/Vzv/W0P/X0f/Y0f/Y0v/Z0//a0//a1P/b1v/c1v/d1//d2P/e2f/e2vvg2//h3P/i3f/i3v/j3v/k3//k4P/l4f/m4v/n4//o5P/p5f/p5v/q5v/q5//r6P/s6f/t6v/u6//v7P/v7f/w7v/x7v/x7//x8Pvy8P/z8P/z8f/08v/08//18//19P/29P/29f/39v/49v/49//4+Pv5+P/6+f/7+v/7+//8+//8/Pz9/P/9/f/+/v////9lvxipAAAAEHRSTlMAAhIbNVV3hpOr0eLq7fj5iS0UtQAAAAFiS0dE5sFsWgUAAAXFSURBVHja7dv7X1NlHAdwu9/rwyXU0ryUZmoubwjLeyaaXcRKvCZZalYzNM0Lp9LKoUZZpqGFVCZ5SZ1lRSpmSglLMYHHVGgygn3+jn44O2dnc5tnY1u98Pn8dAZfDuP9OnzPc9lpB5k4pp0kkLySV0bySl7JKyN5Ja/klZG8bYY3eURe8S+iicY0iSPFecOTJGVrefsoToaKM7+3xGwN773r3AwX95rukjNq3pw6Xi61z0rP6HhTC2kma1OlaBS8HbbTXL7uIEkj5k01q0vuulOaRspbSPNZI00j5J3CSPKMRI2It1uNAc+1IfuhgP6a1H+JcaJRJ8dnEfGuM9ht7BO0ZKbx8rVL1Qh4e/tmE83zQhVVGOcXfSWreV7FB/dCyKIS4+WbL1lN8yb51hkKQ1cd8Vt/SJauZnlH6Gr1PUIWPek/eBgmXc3y5uloq0KVpM0468+7QLqa5S3W0caoXxj9Sdlv/mkKHPp+Jl3N8updtSUFADCm5fIzi8PS1Syvvg75h/q6yMTErUa6muXVR70V6ut9Jngb4/2uUzq2FV7drEF9vcDMukN83/PQb1tYW3AfAOA1Id4C9gsxFgCmCSGEc1c2AGCFEMUAXhZiPwBMFEIIIbIAdHEKYQO2CrHVcNoqIU7eDbSvEuKkNiotE6K6s3r8tPrz4jQADFKPZ8eOl+pkLG2z5z/m7VNPkjyYDABvkHbgMPkYAOR6f//zADCavHgXsIdcDACT1G9NAIAV5Km0wR7yEcN5L5Ccqda5jKPSXPV4mrZ1CwDp6vH8GPJqM+Luo8b75fHpC3a4E8g7j7ywlGRGUN5TWyrJ49qEKBtdmslBXl6noigDAKDrOXLWevIbBPDuViegGm8BSe5VjzMVxUmWKMu9vBcVRRkZQ96yMJvt3d9xJ4x3Ltm86GGLpWNQ3lKM0RrZu+SHmEyWw8t7yGodrJ5jMVnRSM+wQF5P365uH29aHd3V9DyoVewjJ6tH6eR5q9WaFEPe8Cu5VmeieHvWkHStvR9BeaveLyd3AgBGktVJG8nX4WsOx9RzdKolyRIE8jLvRfp4s8nt75GLgvKSZHIseWv6hV1Zq0zUrW3gliaSVV0BYAm5BjhCjjf03uP91RtTFTmqjhx4KS9sJD0ZgbwVPOpghc67lcx9lPw1Mbws7xWuMtOVGN5eVuv4ZS3kLG8j/j6lp4tM9/KeJt3a21xF7iEPQuMts1i09eqO1eRmBPKuJMlVGm83N+k8YVhD8eM9a7FYEFNeOsP28uWJ4c0lL86pJ2cAQAbJMw3kyRSt95b69vuGkyQX6ryVNptN+w/8gpxzCW+Wh/SM1Xjnan/O6mC8LpvNZo0tLz2bhoZu553PJ4T3HrXL13YzziKnaPKlmR42e29gSZUkOQAIHJiF4h30HflzP433AFmiKOvJmtQQzWF+jHlJOrcVKv55VRs9bk5M7+234QJbdg/x3t2XnSF/eEq/sEuxhdzhrXybZBki4LWRSzTegaSnF5BaR05MHG+wFKmX9HOJ4QXQyfhxik5pbWBSHDbqYuW4hPG2mSUdc7yvAACGSN748GYBALIStWJ2hfGW+Pdeud4bHW+Zh2SFYMNRej5f583ahRO8g7UiuVvRKt7Z9STtDpYv18c7xjv4ObnX1jre3fu40u5w/b6+ofnNj7784KX2xtKlWplNukbHu/HT8zl2h1LOaeQ/f//50wOGygx9zWGodI2Sd9P0HLsjf++POfpcXkuPE1pVpXwKK0reoqKpM+yOfMfBHPKvAyXjfHXpui5XSNYoec+5pzbaHfmHmEOy/uNMfZklv9G3TyyfcIuSt9DtKeDO4yVOFpA89oS62TbRVtxoKFotVWM9azM+3SY/nR5P3kkSNY68sjXEk3dbijSNH+9X8qnMOPKultduAO/lEgHurde1k4kwpnFvv/FqqRUv3ltuuEZaxYP3jttuvun6a6+SUjIyMjIy/4/8C27igDz0lDfqAAAAAElFTkSuQmCC")))));
        try {
            response = client.post(request);
        } catch (MailjetException e) {
            log.error(e.getMessage(), e);
            throw new GeneralException(Status.MAIL_SEND_FAIL);
        }
    }
}
