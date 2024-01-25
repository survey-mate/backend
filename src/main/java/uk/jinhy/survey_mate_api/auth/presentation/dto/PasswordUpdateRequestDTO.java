package uk.jinhy.survey_mate_api.auth.presentation.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PasswordUpdateRequestDTO {

    private String currPassword;

    private String newPassword;

}
