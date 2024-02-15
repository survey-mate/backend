package uk.jinhy.survey_mate_api.data.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uk.jinhy.survey_mate_api.auth.domain.entity.Member;
import uk.jinhy.survey_mate_api.data.domain.entity.Data;

public interface DataRepository extends JpaRepository<Data, Long> {

    Optional<Data> findByDataId(Long id);

    @Query("select data from Data data "
            + "where data.seller = :member and data.isDeleted = false"
            + " order by data.createdAt")
    List<Data> findBySeller(Member member);

    @Query("select data from Data data "
        + "join PurchaseHistory purchase_history on data = purchase_history.data "
        + "where purchase_history.buyer = :member"
        + " order by data.createdAt")
    List<Data> findByBuyer(Member member);

    @Query("select data from Data data "
            + "where data.isDeleted = false"
            + " order by data.createdAt limit 15 ")
    List<Data> findRecentData();
}
