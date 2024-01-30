package uk.jinhy.survey_mate_api.statement.application.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uk.jinhy.survey_mate_api.auth.domain.entity.Member;
import uk.jinhy.survey_mate_api.common.response.Status;
import uk.jinhy.survey_mate_api.common.response.exception.GeneralException;
import uk.jinhy.survey_mate_api.statement.application.dto.StatementServiceDTO;
import uk.jinhy.survey_mate_api.statement.domain.entity.Statement;
import uk.jinhy.survey_mate_api.statement.domain.repository.StatementRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class StatementService {
    private final StatementRepository statementRepository;

    public Statement createStatement(Member member, StatementServiceDTO.CreateStatementDTO dto) throws Exception {
        Statement statement = Statement.builder()
                .member(member)
                .amount(dto.getAmount())
                .description(dto.getDescription())
                .build();

        if(getTotalAmount(member) + statement.getAmount() < 0) {
            throw new GeneralException(Status.STATEMENT_NOT_ENOUGH);
        }

        statementRepository.save(statement);
        return statement;
    }

    public Statement getStatement(Long statementId) {
        return statementRepository.findByStatementId(statementId).get();
    }

    public List<Statement> getStatementList(Member member) { return statementRepository.findByMember(member); }

    public Long getTotalAmount(Member member) {
        return statementRepository.findTotalAmountByMember(member);
    }

    public List<Statement> getStatementAsBuyer(Member buyer) {
        return statementRepository.findByBuyer(buyer);
    }

    public List<Statement> getStatementAsSeller(Member seller) {
        return statementRepository.findBySeller(seller);
    }
}
