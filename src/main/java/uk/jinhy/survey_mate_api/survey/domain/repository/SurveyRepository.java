package uk.jinhy.survey_mate_api.survey.domain.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uk.jinhy.survey_mate_api.auth.domain.entity.Member;
import uk.jinhy.survey_mate_api.survey.domain.entity.Survey;

public interface SurveyRepository extends JpaRepository<Survey, Long> {

    List<Survey> findByEndedAtAfterOrderByEndedAt(LocalDateTime time, Pageable pageable);

    Optional<Survey> findBySurveyId(Long id);

    Optional<Survey> findByRewardUrl(String url);

    @Query("select survey from Survey survey order by survey.createdAt limit 15")
    List<Survey> findRecentSurvey();

    List<Survey> findByRegistrant(Member member);

    @Query("select survey from Survey survey "
        + "join Answer answer on survey = answer.survey where answer.respondent = :member")
    List<Survey> findByRespondent(Member member);
}
