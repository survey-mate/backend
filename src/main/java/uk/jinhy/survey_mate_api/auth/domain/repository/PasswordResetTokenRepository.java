package uk.jinhy.survey_mate_api.auth.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.jinhy.survey_mate_api.auth.domain.entity.PasswordResetToken;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, String> {

    boolean existsByEmailAddressAndToken(String emailAddress, String token);

}
