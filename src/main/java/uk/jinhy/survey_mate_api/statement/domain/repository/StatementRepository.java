package uk.jinhy.survey_mate_api.statement.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uk.jinhy.survey_mate_api.member.Member;
import uk.jinhy.survey_mate_api.statement.domain.entity.Statement;

import java.util.List;
import java.util.Optional;

public interface StatementRepository extends JpaRepository<Statement, Long> {
    Optional<Statement> findByStatementId(Long id);
    List<Statement> findByMember(Member member);

    @Query("select sum(statement.amount) from Statement statement where statement.member = :member")
    Long findTotalAmountByMember(Member member);
    @Query("select statement from Statement statement where statement.member = :buyer and statement.amount  < 0")
    List<Statement> findByBuyer(Member buyer);
    @Query("select statement from Statement statement where statement.member = :seller and statement.amount  > 0")
    List<Statement> findBySeller(Member seller);
}
