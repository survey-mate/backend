package uk.jinhy.survey_mate_api.auth.presentation.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PasswordResetCodeDTO {

    private String emailAddr;

    private String code;

}
