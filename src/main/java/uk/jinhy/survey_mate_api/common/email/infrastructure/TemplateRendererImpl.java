package uk.jinhy.survey_mate_api.common.email.infrastructure;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import uk.jinhy.survey_mate_api.common.email.service.TemplateRenderer;

@RequiredArgsConstructor
@Component
public class TemplateRendererImpl implements TemplateRenderer {

    private final SpringTemplateEngine springTemplateEngine;

    @Override
    public String render(String fileName, Map<String, Object> context) {
        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(context);
        return springTemplateEngine.process(fileName, thymeleafContext);
    }
}
