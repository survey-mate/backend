package uk.jinhy.survey_mate_api.survey.presentation;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import uk.jinhy.survey_mate_api.auth.application.service.AuthService;
import uk.jinhy.survey_mate_api.auth.domain.entity.Member;
import uk.jinhy.survey_mate_api.common.response.ApiResponse;
import uk.jinhy.survey_mate_api.common.response.Status;
import uk.jinhy.survey_mate_api.survey.application.dto.SurveyCommandServiceDTO;
import uk.jinhy.survey_mate_api.survey.application.service.SurveyCommandService;
import uk.jinhy.survey_mate_api.survey.application.service.SurveyQueryService;
import uk.jinhy.survey_mate_api.survey.application.service.SurveyServiceFacade;
import uk.jinhy.survey_mate_api.survey.domain.entity.Survey;
import uk.jinhy.survey_mate_api.survey.presentation.converter.SurveyConverter;
import uk.jinhy.survey_mate_api.survey.presentation.dto.SurveyControllerDTO;

@RequiredArgsConstructor
@RequestMapping("/survey")
@RestController
@PreAuthorize("hasRole('USER')")
public class SurveyController {

    private final SurveyQueryService surveyQueryService;
    private final SurveyCommandService surveyCommandService;
    private final SurveyServiceFacade surveyServiceFacade;
    private final SurveyConverter surveyConverter;
    private final AuthService authService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    @Operation(summary = "설문 추가")
    public ApiResponse<SurveyControllerDTO.SurveyDTO> createSurvey(
        @RequestBody @Valid SurveyControllerDTO.CreateSurveyRequestDTO dto
    ) {
        Member registrant = authService.getCurrentMember();
        SurveyCommandServiceDTO.CreateSurveyDTO serviceDto = surveyConverter.toServiceCreateSurveyDto(
            dto);
        Survey survey = surveyServiceFacade.createSurvey(registrant, serviceDto);
        SurveyControllerDTO.SurveyDTO resultDto = surveyConverter.toSurveyDTO(survey);
        return ApiResponse.onSuccess(Status.CREATED.getCode(), Status.CREATED.getMessage(),
            resultDto);
    }

    @PatchMapping("/{surveyId}")
    @Operation(summary = "설문 수정")
    public ApiResponse<SurveyControllerDTO.SurveyDTO> editSurvey(
        @RequestBody @Valid SurveyControllerDTO.EditSurveyRequestDTO dto,
        @PathVariable("surveyId") Long surveyId
    ) {
        Member registrant = authService.getCurrentMember();
        SurveyCommandServiceDTO.EditSurveyDTO serviceDto = surveyConverter.toServiceEditSurveyDto(
            surveyId,
            dto);
        surveyCommandService.editSurvey(registrant, serviceDto);
        return ApiResponse.onSuccess(Status.OK.getCode(), Status.OK.getMessage(), null);
    }

    @DeleteMapping("/{surveyId}")
    @Operation(summary = "설문 삭제")
    public ApiResponse<Object> deleteSurvey(@PathVariable("surveyId") Long surveyId) {
        Member registrant = authService.getCurrentMember();
        surveyCommandService.deleteSurvey(registrant, surveyId);
        return ApiResponse.onSuccess(Status.OK.getCode(), Status.OK.getMessage(), null);
    }

    @GetMapping("/{surveyId}")
    @Operation(summary = "설문 상세정보 불러오기")
    public ApiResponse<SurveyControllerDTO.SurveyDetailDTO> getSurveyDetail(
        @PathVariable("surveyId") Long surveyId) {
        Survey survey = surveyQueryService.getSurvey(surveyId);
        boolean isResponded = surveyQueryService.isResponded(survey, null);
        SurveyControllerDTO.SurveyDetailDTO dto = surveyConverter.toSurveyDetailDto(isResponded,
            survey);
        return ApiResponse.onSuccess(Status.OK.getCode(), Status.OK.getMessage(), dto);
    }

    @GetMapping("")
    @Operation(summary = "설문 불러오기")
    public ApiResponse<SurveyControllerDTO.SurveyListDTO> getSurveysList(
        @RequestParam("page") int pageNumber) {
        List<Survey> surveyList = surveyQueryService.getSurveyList(pageNumber);
        List<SurveyControllerDTO.SurveyDTO> dtoList = surveyList.stream()
            .map(surveyConverter::toSurveyDTO)
            .toList();
        SurveyControllerDTO.SurveyListDTO dto = new SurveyControllerDTO.SurveyListDTO(dtoList);
        return ApiResponse.onSuccess(Status.OK.getCode(), Status.OK.getMessage(), dto);
    }

    @GetMapping("/answer/{rewardUrl}")
    @Operation(summary = "포인트 수령")
    public ApiResponse<SurveyControllerDTO.RewardResultDTO> earnRewardsAfterAnswerTheSurvey(
        @PathVariable("rewardUrl") String rewardUrl) {
        Member respondent = authService.getCurrentMember();
        surveyServiceFacade.answerSurvey(respondent, rewardUrl);
        return ApiResponse.onSuccess(Status.OK.getCode(), Status.OK.getMessage(), null);
    }

    @GetMapping("/registrant")
    @Operation(summary = "내가 등록한 설문 불러오기")
    public ApiResponse<SurveyControllerDTO.SurveyListDTO> getSurveysListByRegistrant() {
        Member registrant = authService.getCurrentMember();
        List<Survey> surveyList = surveyQueryService.getMySurveyList(registrant);
        List<SurveyControllerDTO.SurveyDTO> dtoList = surveyList.stream()
            .map(surveyConverter::toSurveyDTO)
            .toList();
        SurveyControllerDTO.SurveyListDTO dto = new SurveyControllerDTO.SurveyListDTO(dtoList);
        return ApiResponse.onSuccess(Status.OK.getCode(), Status.OK.getMessage(), dto);
    }

    @GetMapping("/respondent")
    @Operation(summary = "내가 응답한 설문 불러오기")
    public ApiResponse<SurveyControllerDTO.SurveyListDTO> getSurveysListByRespondent() {
        Member registrant = authService.getCurrentMember();
        List<Survey> surveyList = surveyQueryService.getAnsweredSurveyList(registrant);
        List<SurveyControllerDTO.SurveyDTO> dtoList = surveyList.stream()
            .map(surveyConverter::toSurveyDTO)
            .toList();
        SurveyControllerDTO.SurveyListDTO dto = new SurveyControllerDTO.SurveyListDTO(dtoList);
        return ApiResponse.onSuccess(Status.OK.getCode(), Status.OK.getMessage(), dto);
    }

    @GetMapping("/new")
    @Operation(summary = "최신 설문 불러오기")
    public ApiResponse<SurveyControllerDTO.SurveyListDTO> getRecentSurveysList() {
        List<Survey> surveyList = surveyQueryService.getRecentSurveyList();
        List<SurveyControllerDTO.SurveyDTO> dtoList = surveyList.stream()
            .map(surveyConverter::toSurveyDTO)
            .toList();
        SurveyControllerDTO.SurveyListDTO dto = new SurveyControllerDTO.SurveyListDTO(dtoList);
        return ApiResponse.onSuccess(Status.OK.getCode(), Status.OK.getMessage(), dto);
    }
}
