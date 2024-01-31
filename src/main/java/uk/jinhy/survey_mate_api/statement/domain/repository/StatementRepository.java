package uk.jinhy.survey_mate_api.statement.domain.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uk.jinhy.survey_mate_api.auth.domain.entity.Member;
import uk.jinhy.survey_mate_api.statement.domain.entity.Statement;

public interface StatementRepository extends JpaRepository<Statement, Long> {

    Optional<Statement> findByStatementId(Long id);

    List<Statement> findByMember(Member member);

    @Query("select ifnull(sum(statement.amount), 0) from Statement statement where statement.member = :member")
    Long findTotalAmountByMember(Member member);

    @Query("select statement from Statement statement "
        + "where statement.member = :buyer and statement.amount  < 0")
    List<Statement> findByBuyer(Member buyer);

    @Query("select statement from Statement statement "
        + "where statement.member = :seller and statement.amount  > 0")
    List<Statement> findBySeller(Member seller);
}
