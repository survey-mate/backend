package uk.jinhy.survey_mate_api.data.application.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uk.jinhy.survey_mate_api.data.application.dto.DataServiceDTO;
import uk.jinhy.survey_mate_api.data.domain.entity.Data;
import uk.jinhy.survey_mate_api.data.domain.entity.PurchaseHistory;
import uk.jinhy.survey_mate_api.data.domain.repository.DataRepository;
import uk.jinhy.survey_mate_api.auth.domain.entity.Member;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DataService {
    private final DataRepository dataRepository;

    public Data createData(Member seller, DataServiceDTO.CreateDataDTO dto) {
        Data data = Data.builder()
                .seller(seller)
                .fileUrl(dto.getFileUrl())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .seller(seller)
                .build();
        dataRepository.save(data);
        return data;
    }

    @Transactional
    public void editData(Member seller, DataServiceDTO.EditDataDTO dto) {
        Long dataId = dto.getDataId();

        Data data = dataRepository.findByDataId(dataId).get();
        if(!data.getSeller().equals(seller)) { return; }

        String newTitle = dto.getTitle();
        if(newTitle != null) { data.updateTitle(newTitle); }

        String newDescription = dto.getDescription();
        if(newDescription != null) { data.updateDescription(newDescription); }

        String newFileUrl = dto.getFileUrl();
        if(newFileUrl != null) { data.updateFileUrl(newFileUrl); }

        dataRepository.save(data);
    }

    @Transactional
    public void deleteData(Member seller, Long dataId) {
        Data data = dataRepository.findByDataId(dataId).get();
        if(data.getSeller().equals(seller)) {
            dataRepository.deleteById(dataId);
        }
    }

    @Transactional
    public void buyData(Member buyer, Long dataId) {
        Data data = dataRepository.findByDataId(dataId).get();

        PurchaseHistory purchaseHistory = PurchaseHistory.builder()
                .data(data)
                .buyer(buyer)
                .build();
        data.addPurchaseHistory(purchaseHistory);
        dataRepository.save(data);
    }

    public Data getData(Long dataId) { return dataRepository.findByDataId(dataId).get(); }

    public List<Data> getDataListAsBuyer(Member buyer) { return dataRepository.findByBuyer(buyer); }

    public List<Data> getDataListAsSeller(Member seller) { return dataRepository.findByBuyer(seller); }

    public List<Data> getRecentDataList() { return dataRepository.findRecentData(); }
}
