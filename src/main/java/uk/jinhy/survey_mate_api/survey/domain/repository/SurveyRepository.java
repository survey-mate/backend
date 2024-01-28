package uk.jinhy.survey_mate_api.survey.domain.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uk.jinhy.survey_mate_api.auth.domain.entity.Member;
import uk.jinhy.survey_mate_api.survey.domain.entity.Survey;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SurveyRepository extends JpaRepository<Survey, Long> {
    List<Survey> findByEndedAtIsBeforeOrderByCreatedAtDesc(Pageable pageable, LocalDateTime time);
    Optional<Survey> findBySurveyId(Long id);
    Optional<Survey> findByRewardUrl(String url);
    @Query("select survey from Survey survey order by survey.createdAt limit 15")
    List<Survey> findRecentSurvey();
    List<Survey> findByRegistrant(Member member);
    @Query("select survey from Survey survey join Answer answer on survey = answer.survey where answer.respondent = :member")
    List<Survey> findByRespondent(Member member);
}
