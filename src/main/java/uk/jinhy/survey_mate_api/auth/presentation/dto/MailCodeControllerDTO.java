package uk.jinhy.survey_mate_api.auth.presentation.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MailCodeControllerDTO {

    @NotNull
    private String code;

    @NotNull
    private String emailAddr;

}
