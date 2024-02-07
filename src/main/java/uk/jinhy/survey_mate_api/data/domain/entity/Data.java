package uk.jinhy.survey_mate_api.data.domain.entity;

import jakarta.persistence.*;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import uk.jinhy.survey_mate_api.auth.domain.entity.Member;


@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Data {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dataId;

    @CreatedDate
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @NotNull
    private Member seller;

    @NotNull
    private String fileUrl;

    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    private Long price;

    @NotNull
    private Boolean isDeleted;

    @Builder.Default
    @OneToMany(mappedBy = "data", cascade = CascadeType.ALL)
    private List<PurchaseHistory> purchaseHistoryList = new ArrayList<>();

    public void addPurchaseHistory(PurchaseHistory purchaseHistory) {
        purchaseHistoryList.add(purchaseHistory);
    }

    public void confirmSeller(Member seller) {
        this.seller = seller;
        seller.addData(this);
    }

    public boolean isPurchased(Member member) {
        return purchaseHistoryList.stream()
                .anyMatch(a -> a.getBuyer().equals(member));
    }

    public void updateTitle(String newTitle) {
        title = newTitle;
    }

    public void updateDescription(String newDescription) {
        title = newDescription;
    }

    public void updatePrice(Long newPrice) { price = newPrice; }

    public void updateIsDeleted(Boolean newIsDeleted) { isDeleted = newIsDeleted;}

    public void updateFileUrl(String newFileUrl) {
        title = newFileUrl;
    }
}
