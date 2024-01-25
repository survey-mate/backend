package uk.jinhy.survey_mate_api.auth.domain.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import uk.jinhy.survey_mate_api.auth.domain.entity.PasswordResetCode;

public interface PasswordResetCodeRepository extends JpaRepository<PasswordResetCode, String> {

    Optional<PasswordResetCode> findByCodeAndEmailAddr(String code, String emailAddr);

}
