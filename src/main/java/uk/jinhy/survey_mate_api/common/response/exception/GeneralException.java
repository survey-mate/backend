package uk.jinhy.survey_mate_api.common.response.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.jinhy.survey_mate_api.common.response.Body;
import uk.jinhy.survey_mate_api.common.response.Status;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {

    private Status status;

    public Body getBody() {
        return this.status.getBody();
    }
}
