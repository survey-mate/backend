package uk.jinhy.survey_mate_api.data.application.service;

import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uk.jinhy.survey_mate_api.auth.domain.entity.Member;
import uk.jinhy.survey_mate_api.common.aws.S3Service;
import uk.jinhy.survey_mate_api.common.response.Status;
import uk.jinhy.survey_mate_api.common.response.exception.GeneralException;
import uk.jinhy.survey_mate_api.common.util.Util;
import uk.jinhy.survey_mate_api.data.application.dto.DataServiceDTO;
import uk.jinhy.survey_mate_api.data.domain.entity.Data;
import uk.jinhy.survey_mate_api.data.domain.entity.PurchaseHistory;
import uk.jinhy.survey_mate_api.data.domain.repository.DataRepository;

@RequiredArgsConstructor
@Service
public class DataService {

    private final DataRepository dataRepository;
    private final S3Service s3Service;

    public Data createData(Member seller, DataServiceDTO.CreateDataDTO dto) {
        MultipartFile file = dto.getFile();

        if(file == null) {
            throw new GeneralException(Status.BAD_REQUEST);
        }

        String fileURL = s3Service.uploadFile(
            s3Service.generateDataFileKeyName(Util.generateRandomString(10)), file);

        Data data = Data.builder()
            .seller(seller)
            .fileUrl(fileURL)
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
        if (!data.getSeller().equals(seller)) {
            return;
        }

        String newTitle = dto.getTitle();
        if (newTitle != null) {
            data.updateTitle(newTitle);
        }

        String newDescription = dto.getDescription();
        if (newDescription != null) {
            data.updateDescription(newDescription);
        }

        dataRepository.save(data);
    }

    @Transactional
    public void deleteData(Member seller, Long dataId) {
        Data data = dataRepository.findByDataId(dataId).get();
        if (data.getSeller().equals(seller)) {
            dataRepository.deleteById(dataId);
        }
    }

    @Transactional
    public Long buyData(Member buyer, Long dataId) {
        Data data = dataRepository.findByDataId(dataId).get();

        PurchaseHistory purchaseHistory = PurchaseHistory.builder()
            .data(data)
            .buyer(buyer)
            .build();
        data.addPurchaseHistory(purchaseHistory);
        dataRepository.save(data);

        return data.getPrice();
    }

    public Data getData(Long dataId) {
        return dataRepository.findByDataId(dataId).get();
    }

    public List<Data> getDataListAsBuyer(Member buyer) {
        return dataRepository.findByBuyer(buyer);
    }

    public List<Data> getDataListAsSeller(Member seller) {
        return dataRepository.findBySeller(seller);
    }

    public List<Data> getRecentDataList() {
        return dataRepository.findRecentData();
    }
}
