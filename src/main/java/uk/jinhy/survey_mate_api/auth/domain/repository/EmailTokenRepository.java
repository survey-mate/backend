package uk.jinhy.survey_mate_api.auth.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.jinhy.survey_mate_api.auth.domain.entity.EmailToken;

public interface EmailTokenRepository extends JpaRepository<EmailToken, String> {

    boolean existsByEmailAddrAndToken(String emailAddr, String token);

}
