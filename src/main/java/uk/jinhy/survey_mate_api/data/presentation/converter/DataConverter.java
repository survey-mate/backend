package uk.jinhy.survey_mate_api.data.presentation.converter;

import org.springframework.stereotype.Component;
import uk.jinhy.survey_mate_api.data.application.dto.DataServiceDTO;
import uk.jinhy.survey_mate_api.data.presentation.dto.DataControllerDTO;

@Component
public class DataConverter {
    public DataServiceDTO.CreateDataDTO toServiceCreateDataDto(
           DataControllerDTO.CreateDataRequestDTO controllerDTO
    ) {
        return DataServiceDTO.CreateDataDTO.builder()
                .title(controllerDTO.getTitle())
                .description(controllerDTO.getDescription())
                .price(controllerDTO.getAmount())
                .file(controllerDTO.getFile())
                .build();
    }

    public DataServiceDTO.EditDataDTO toServiceEditDataDto(
            DataControllerDTO.EditDataRequestDTO controllerDTO
    ) {
        return DataServiceDTO.EditDataDTO.builder()
                .dataId(controllerDTO.getDataId())
                .title(controllerDTO.getTitle())
                .description(controllerDTO.getDescription())
                .build();
    }
}
