package uk.jinhy.survey_mate_api.common.email.service;

import java.util.Map;

public interface TemplateRenderer {

    String render(String fileName, Map<String, Object> context);
}
