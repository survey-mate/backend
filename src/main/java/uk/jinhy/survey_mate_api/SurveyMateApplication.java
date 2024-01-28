package uk.jinhy.survey_mate_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SurveyMateApplication {
    public static void main(String[] args) {
        SpringApplication.run(SurveyMateApplication.class, args);
    }
}
