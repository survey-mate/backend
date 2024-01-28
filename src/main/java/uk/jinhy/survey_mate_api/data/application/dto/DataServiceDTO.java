package uk.jinhy.survey_mate_api.data.application.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

public class DataServiceDTO {
    @Builder
    @Getter
    public static class CreateDataDTO {
        private String title;
        private String description;
        private Long price;
        private MultipartFile file;
    }

    @Builder
    @Getter
    public static class EditDataDTO {
        private Long dataId;
        private String title;
        private String description;
    }
}
