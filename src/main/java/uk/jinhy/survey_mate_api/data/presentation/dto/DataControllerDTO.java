package uk.jinhy.survey_mate_api.data.presentation.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;
import uk.jinhy.survey_mate_api.auth.domain.entity.Member;
import uk.jinhy.survey_mate_api.data.domain.entity.Data;

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

        private Data data;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class DataListDTO {

        private List<Data> datas;
    }

}
