package uk.jinhy.survey_mate_api.survey.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.jinhy.survey_mate_api.survey.application.SurveyService;

@RequiredArgsConstructor
@RequestMapping("/survey")
@Controller
public class SurveyController {
    private final SurveyService surveyService;
}
