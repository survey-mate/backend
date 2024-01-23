package uk.jinhy.survey_mate_api.statement.application.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uk.jinhy.survey_mate_api.member.Member;
import uk.jinhy.survey_mate_api.statement.application.dto.StatementServiceDTO;
import uk.jinhy.survey_mate_api.statement.domain.entity.Statement;
import uk.jinhy.survey_mate_api.statement.domain.repository.StatementRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class StatementService {
    private StatementRepository statementRepository;

    public Optional<Statement> createStatement(Member member, StatementServiceDTO.CreateStatementDTO dto) {
        Statement statement = Statement.builder()
                .amount(dto.getAmount())
                .description(dto.getDescription())
                .build();

        if(getTotalAmount(member) + statement.getAmount() < 0) {
            return null;
        }

        statementRepository.save(statement);
        return Optional.of(statement);
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