package uk.jinhy.survey_mate_api.auth.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class JwtTokenInfo {

    private String accessToken;

    private String refreshToken;

}
