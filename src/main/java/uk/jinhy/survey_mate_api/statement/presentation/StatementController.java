package uk.jinhy.survey_mate_api.statement.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.jinhy.survey_mate_api.statement.application.service.StatementService;

@RequiredArgsConstructor
@RequestMapping("/statement")
@Controller
public class StatementController {
    private final StatementService statementService;
}
