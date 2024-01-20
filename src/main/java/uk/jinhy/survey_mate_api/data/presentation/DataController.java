package uk.jinhy.survey_mate_api.data.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.jinhy.survey_mate_api.data.application.service.DataService;

@RequiredArgsConstructor
@RequestMapping("/data")
@Controller
public class DataController {
    private final DataService dataService;
}
