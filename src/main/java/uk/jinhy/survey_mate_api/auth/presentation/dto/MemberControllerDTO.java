package uk.jinhy.survey_mate_api.auth.presentation.dto;

import lombok.Builder;
import lombok.Getter;

public class MemberControllerDTO {
    @Builder
    @Getter
    public static class MemberRequestDTO{
        private String memberId;

        private String nickname;

        private String password;

        private boolean messageConsent;

        private boolean marketingConsent;
    }
}
