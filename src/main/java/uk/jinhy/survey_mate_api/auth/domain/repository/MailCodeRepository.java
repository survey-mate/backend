package uk.jinhy.survey_mate_api.auth.domain.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import uk.jinhy.survey_mate_api.auth.domain.entity.MailCode;

public interface MailCodeRepository extends JpaRepository<MailCode, String> {

    Optional<MailCode> findByCodeAndEmailAddress(String code, String emailAddress);

}
