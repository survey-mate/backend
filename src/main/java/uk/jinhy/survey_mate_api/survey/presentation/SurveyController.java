package uk.jinhy.survey_mate_api.survey.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uk.jinhy.survey_mate_api.auth.application.service.AuthService;
import uk.jinhy.survey_mate_api.auth.domain.entity.Member;
import uk.jinhy.survey_mate_api.common.response.ApiResponse;
import uk.jinhy.survey_mate_api.common.response.Status;
import uk.jinhy.survey_mate_api.survey.application.dto.SurveyServiceDTO;
import uk.jinhy.survey_mate_api.survey.application.service.SurveyService;
import uk.jinhy.survey_mate_api.survey.application.service.SurveyServiceFacade;
import uk.jinhy.survey_mate_api.survey.domain.entity.Survey;
import uk.jinhy.survey_mate_api.survey.presentation.converter.SurveyConverter;
import uk.jinhy.survey_mate_api.survey.presentation.dto.SurveyControllerDTO;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/survey")
@RestController
@PreAuthorize("hasRole('USER')")
public class SurveyController {
    private final SurveyService surveyService;
    private final SurveyServiceFacade surveyServiceFacade;
    private final SurveyConverter surveyConverter;
    private final AuthService authService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/survey")
    public ApiResponse<SurveyControllerDTO.SurveyDTO> createSurvey(
            @RequestBody @Valid SurveyControllerDTO.CreateSurveyRequestDTO dto
    ) {
        Member registrant = authService.getCurrentMember();
        SurveyServiceDTO.CreateSurveyDTO serviceDto = surveyConverter.toServiceCreateSurveyDto(dto);
        surveyServiceFacade.createSurvey(registrant, serviceDto);
        return ApiResponse.onSuccess(Status.CREATED.getCode(), Status.CREATED.getMessage(), null);
    }

    @PatchMapping("/{surveyId}")
    public ApiResponse<SurveyControllerDTO.SurveyDTO> editSurvey(
            @RequestBody @Valid SurveyControllerDTO.EditSurveyRequestDTO dto,
            @PathVariable("surveyId") Long surveyId
    ) {
        Member registrant = authService.getCurrentMember();
        SurveyServiceDTO.EditSurveyDTO serviceDto = surveyConverter.toServiceEditSurveyDto(surveyId, dto);
        surveyService.editSurvey(registrant, serviceDto);
        return ApiResponse.onSuccess(Status.OK.getCode(), Status.OK.getMessage(), null);
    }

    @DeleteMapping("/{surveyId}")
    public ApiResponse<Object> deleteSurvey(@PathVariable("surveyId") Long surveyId) {
        Member registrant = authService.getCurrentMember();
        surveyService.deleteSurvey(registrant, surveyId);
        return ApiResponse.onSuccess(Status.OK.getCode(), Status.OK.getMessage(), null);
    }

    @GetMapping("/{surveyId}")
    public ApiResponse<SurveyControllerDTO.SurveyDetailDTO> getSurveyDetail(@PathVariable("surveyId") Long surveyId) {
        Survey survey = surveyService.getSurvey(surveyId);
        boolean isResponded = surveyService.isResponded(survey, null);
        SurveyControllerDTO.SurveyDetailDTO dto = surveyConverter.toSurveyDetailDto(isResponded, survey);
        return ApiResponse.onSuccess(Status.OK.getCode(), Status.OK.getMessage(), null);
    }

    @GetMapping("")
    public ApiResponse<SurveyControllerDTO.SurveyListDTO> getSurveysList(@RequestParam("page") int pageNumber) {
        List<Survey> surveyList = surveyService.getSurveyList(pageNumber);
        List<SurveyControllerDTO.SurveyDTO> dtoList = surveyList.stream()
                .map(surveyConverter::toSurveyDTO)
                .toList();
        SurveyControllerDTO.SurveyListDTO dto = new SurveyControllerDTO.SurveyListDTO(dtoList);
        return ApiResponse.onSuccess(Status.OK.getCode(), Status.OK.getMessage(), dto);
    }

    @GetMapping("/answer/{rewardUrl}")
    public ApiResponse<SurveyControllerDTO.RewardResultDTO> earnRewardsAfterAnswerTheSurvey(@PathVariable("rewardUrl") String rewardUrl) {
        Member respondent = authService.getCurrentMember();
        surveyServiceFacade.answerSurvey(respondent, rewardUrl);
        return ApiResponse.onSuccess(Status.OK.getCode(), Status.OK.getMessage(), null);
    }

    @GetMapping("/registrant")
    public ApiResponse<SurveyControllerDTO.SurveyListDTO> getSurveysListByRegistrant() {
        Member registrant = authService.getCurrentMember();
        List<Survey> surveyList = surveyService.getMySurveyList(registrant);
        List<SurveyControllerDTO.SurveyDTO> dtoList = surveyList.stream()
                .map(surveyConverter::toSurveyDTO)
                .toList();
        SurveyControllerDTO.SurveyListDTO dto = new SurveyControllerDTO.SurveyListDTO(dtoList);
        return ApiResponse.onSuccess(Status.OK.getCode(), Status.OK.getMessage(), dto);
    }

    @GetMapping("/respondent")
    public ApiResponse<SurveyControllerDTO.SurveyListDTO> getSurveysListByRespondent() {
        Member registrant = authService.getCurrentMember();
        List<Survey> surveyList = surveyService.getAnsweredSurveyList(registrant);
        List<SurveyControllerDTO.SurveyDTO> dtoList = surveyList.stream()
                .map(surveyConverter::toSurveyDTO)
                .toList();
        SurveyControllerDTO.SurveyListDTO dto = new SurveyControllerDTO.SurveyListDTO(dtoList);
        return ApiResponse.onSuccess(Status.OK.getCode(), Status.OK.getMessage(), dto);
    }

    @GetMapping("/new")
    public ApiResponse<SurveyControllerDTO.SurveyListDTO> getRecentSurveysList() {
        List<Survey> surveyList = surveyService.getRecentSurveyList();
        List<SurveyControllerDTO.SurveyDTO> dtoList = surveyList.stream()
                .map(surveyConverter::toSurveyDTO)
                .toList();
        SurveyControllerDTO.SurveyListDTO dto = new SurveyControllerDTO.SurveyListDTO(dtoList);
        return ApiResponse.onSuccess(Status.OK.getCode(), Status.OK.getMessage(), dto);
    }
}
