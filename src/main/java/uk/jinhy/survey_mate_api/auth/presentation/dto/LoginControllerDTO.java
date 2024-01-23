package uk.jinhy.survey_mate_api.auth.presentation.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoginControllerDTO {
    private String id;

    private String password;
}
