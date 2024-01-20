package uk.jinhy.survey_mate_api.data.application.dto;

import lombok.Builder;
import lombok.Getter;

public class DataServiceDTO {
    @Builder
    @Getter
    public static class CreateDataDTO {
        private String title;
        private String description;
        private Long price;
        private String fileUrl;
    }

    @Builder
    @Getter
    public static class EditDataDTO {
        private Long dataId;
        private String title;
        private String description;
        private String fileUrl;
    }
}
