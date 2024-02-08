package uk.jinhy.survey_mate_api.data.presentation.converter;

import org.springframework.stereotype.Component;
import uk.jinhy.survey_mate_api.data.application.dto.DataServiceDTO;
import uk.jinhy.survey_mate_api.data.domain.entity.Data;
import uk.jinhy.survey_mate_api.data.presentation.dto.DataControllerDTO;

import java.util.List;
import java.util.stream.Collectors;

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
        Long dataId,
        DataControllerDTO.EditDataRequestDTO controllerDTO
    ) {
        return DataServiceDTO.EditDataDTO.builder()
            .dataId(dataId)
            .title(controllerDTO.getTitle())
            .description(controllerDTO.getDescription())
            .price(controllerDTO.getAmount())
            .file(controllerDTO.getFile())
            .build();
    }

    public DataControllerDTO.DataDTO toControllerDataDto(Data data) {
        return DataControllerDTO.DataDTO.builder()
                .dataId(data.getDataId())
                .title(data.getTitle())
                .description(data.getDescription())
                .createdAt(data.getCreatedAt())
                .build();
    }

    public DataControllerDTO.DataListDTO toControllerDataListDto(List<Data> datas) {
        return DataControllerDTO.DataListDTO.builder()
                .datas(datas.stream().map(data -> toControllerDataDto(data)).collect(Collectors.toList()))
                .build();
    }

    public DataControllerDTO.DataDetailDTO toControllerDataDetailDto(Data data, Boolean isPurchased) {
        return DataControllerDTO.DataDetailDTO.builder()
                .seller(data.getSeller().getNickname())
                .createdAt(data.getCreatedAt())
                .title(data.getTitle())
                .description(data.getDescription())
                .price(data.getPrice())
                .fileUrl(data.getFileUrl())
                .isPurchased(isPurchased)
                .build();
    }
}
