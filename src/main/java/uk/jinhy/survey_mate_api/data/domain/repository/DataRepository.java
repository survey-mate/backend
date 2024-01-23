package uk.jinhy.survey_mate_api.data.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uk.jinhy.survey_mate_api.data.domain.entity.Data;
import uk.jinhy.survey_mate_api.auth.domain.entity.Member;

import java.util.List;
import java.util.Optional;

public interface DataRepository extends JpaRepository<Data, Long> {
    Optional<Data> findByDataId(Long id);
    List<Data> findBySeller(Member member);
    @Query("select data from Data data join PurchaseHistory purchase_history on data = purchase_history.data where purchase_history.buyer = :member")
    List<Data> findByBuyer(Member member);
    @Query("select data from Data data order by data.createdAt limit 15")
    List<Data> findRecentData();
}
