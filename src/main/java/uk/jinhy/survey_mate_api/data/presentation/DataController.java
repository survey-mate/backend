package uk.jinhy.survey_mate_api.data.presentation;

import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import uk.jinhy.survey_mate_api.auth.application.service.AuthService;
import uk.jinhy.survey_mate_api.auth.domain.entity.Member;
import uk.jinhy.survey_mate_api.common.response.ApiResponse;
import uk.jinhy.survey_mate_api.common.response.Status;
import uk.jinhy.survey_mate_api.common.response.exception.GeneralException;
import uk.jinhy.survey_mate_api.data.application.dto.DataServiceDTO;
import uk.jinhy.survey_mate_api.data.application.service.DataService;
import uk.jinhy.survey_mate_api.data.application.service.DataServiceFacade;
import uk.jinhy.survey_mate_api.data.domain.entity.Data;
import uk.jinhy.survey_mate_api.data.presentation.converter.DataConverter;
import uk.jinhy.survey_mate_api.data.presentation.dto.DataControllerDTO;

@RequiredArgsConstructor
@RequestMapping("/data")
@RestController
public class DataController {

    private final DataService dataService;
    private final DataServiceFacade dataServiceFacade;
    private final AuthService authService;

    private final DataConverter converter;

    @PostMapping(value = "", consumes = { "multipart/form-data" })
    @Operation(summary = "설문장터 등록")
    public ApiResponse<?> createDataList(@ModelAttribute DataControllerDTO.CreateDataRequestDTO requestDTO) {
        DataServiceDTO.CreateDataDTO serviceDTO = converter.toServiceCreateDataDto(requestDTO);
        Member member = authService.getCurrentMember();

        dataService.createData(member, serviceDTO);

        return ApiResponse.onSuccess(Status.OK.getCode(), Status.OK.getMessage(), null);
    }

    @PatchMapping(value = "/{dataId}")
    @Operation(summary = "설문장터 수정")
    public ApiResponse<?> editData(
        @ModelAttribute DataControllerDTO.EditDataRequestDTO requestDTO,
        @PathVariable("dataId") Long dataId
    ) {
        DataServiceDTO.EditDataDTO serviceDTO = converter.toServiceEditDataDto(requestDTO);
        Member member = authService.getCurrentMember();

        dataService.editData(member, serviceDTO);

        return ApiResponse.onSuccess(Status.OK.getCode(), Status.OK.getMessage(), null);
    }

    @DeleteMapping(value = "/{dataId}")
    @Operation(summary = "설문장터 삭제")
    public ApiResponse<?> deleteData(@PathVariable("dataId") Long dataId) {
        Member member = authService.getCurrentMember();
        dataService.deleteData(member, dataId);
        return ApiResponse.onSuccess(Status.OK.getCode(), Status.OK.getMessage(), null);
    }

    @GetMapping(value = "/buy/{dataId}")
    @Operation(summary = "설문장터 구매")
    public ApiResponse<?> buyData(
        @ModelAttribute DataControllerDTO.BuyDataRequestDTO requestDTO,
        @PathVariable("dataId") Long dataId
    ) {
        Member member = requestDTO.getMember();

        if (!authService.getCurrentMember().equals(member)) {
            throw new GeneralException(Status.UNAUTHORIZED);
        }

        dataServiceFacade.buyData(member, dataId);
        return ApiResponse.onSuccess(Status.OK.getCode(), Status.OK.getMessage(), null);
    }

    @GetMapping(value = "/list")
    @Operation(summary = "전체 설문장터 조회")
    public ApiResponse<?> getDataList() {
        List<Data> dataList = dataService.getRecentDataList();
        DataControllerDTO.DataListDTO responseDTO = new DataControllerDTO.DataListDTO(dataList);
        return ApiResponse.onSuccess(Status.OK.getCode(), Status.OK.getMessage(), responseDTO);
    }

    @GetMapping(value = "/list/buyer")
    @Operation(summary = "구매한 설문장터 조회")
    public ApiResponse<?> getDataListAsBuyer() {
        Member member = authService.getCurrentMember();
        List<Data> dataList = dataService.getDataListAsBuyer(member);
        DataControllerDTO.DataListDTO responseDTO = new DataControllerDTO.DataListDTO(dataList);
        return ApiResponse.onSuccess(Status.OK.getCode(), Status.OK.getMessage(), responseDTO);
    }

    @GetMapping(value = "/list/seller")
    @Operation(summary = "판매 등록한 설문장터 조회")
    public ApiResponse<?> getDataListAsSeller() {
        Member member = authService.getCurrentMember();
        List<Data> dataList = dataService.getDataListAsSeller(member);
        DataControllerDTO.DataListDTO responseDTO = new DataControllerDTO.DataListDTO(dataList);
        return ApiResponse.onSuccess(Status.OK.getCode(), Status.OK.getMessage(), responseDTO);
    }

    @GetMapping("/{dataId}")
    @Operation(summary = "판매 등록한 설문장터 조회")
    public ApiResponse<?> getDataDetail(@PathVariable("dataId") Long dataId) {
        Data data = dataService.getData(dataId);
        DataControllerDTO.DataDTO responseDTO = new DataControllerDTO.DataDTO(data);
        return ApiResponse.onSuccess(Status.OK.getCode(), Status.OK.getMessage(), responseDTO);
    }


}
