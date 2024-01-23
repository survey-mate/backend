package uk.jinhy.survey_mate_api.data.application.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uk.jinhy.survey_mate_api.auth.domain.entity.Member;


@RequiredArgsConstructor
@Service
public class DataServiceFacade {
    private final DataService dataService;

    @Transactional
    public void buyData(Member buyer, Long dataId) {
        // TODO
        // Point 결제 로직 필요
        dataService.buyData(buyer, dataId);
    }
}
