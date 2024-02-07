package uk.jinhy.survey_mate_api.data.presentation.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

public class DataControllerDTO {

    @Builder
    @Getter
    @AllArgsConstructor
    public static class CreateDataRequestDTO {

        private String title;
        private String description;
        private Long amount;
        private MultipartFile file;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class EditDataRequestDTO {

        private String title;
        private String description;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class DataDTO {
        private String title;
        private String description;
        private LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class DataDetailDTO {
        private String seller;
        private LocalDateTime createdAt;
        private String title;
        private String description;
        private Long price;
        private String fileUrl;
        private Boolean isPurchased;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class DataListDTO {
        private List<DataDTO> datas;
    }

}
