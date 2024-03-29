package uk.jinhy.survey_mate_api.data.application.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uk.jinhy.survey_mate_api.auth.domain.entity.Member;
import uk.jinhy.survey_mate_api.statement.application.dto.StatementServiceDTO;
import uk.jinhy.survey_mate_api.statement.application.service.StatementService;


@RequiredArgsConstructor
@Service
public class DataServiceFacade {

    private final DataService dataService;
    private final StatementService statementService;

    @Transactional
    public void buyData(Member buyer, Long dataId) {
        Member seller = dataService.getData(dataId).getSeller();
        Long price = dataService.buyData(buyer, dataId);

        StatementServiceDTO.PayPointDTO payPointDTO = StatementServiceDTO
            .PayPointDTO
            .builder()
            .description("설문 응답 데이터 구매")
            .amount(price)
            .build();

        StatementServiceDTO.EarnPointDTO earnPointDTO = StatementServiceDTO
                .EarnPointDTO
                .builder()
                .description("설문 응답 데이터 판매")
                .amount(price)
                .build();

        statementService.payPoint(buyer, payPointDTO);
        statementService.earnPoint(seller, earnPointDTO);
    }
}
