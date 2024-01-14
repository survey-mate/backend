package uk.jinhy.survey_mate_api.data;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.jinhy.survey_mate_api.data_comment.DataComment;
import uk.jinhy.survey_mate_api.member.Member;
import uk.jinhy.survey_mate_api.purchaseHistory.PurchaseHistory;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Data {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dataId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @NotNull
    private Member seller;

    @NotNull
    private String fileUrl;

    private String description;

    private Long price;

    @Builder.Default
    @OneToMany(mappedBy = "data", cascade = CascadeType.ALL)
    private List<DataComment> dataCommentList = new ArrayList<>();

    public void addDataComment(DataComment dataComment){
        dataCommentList.add(dataComment);
    }

    @Builder.Default
    @OneToMany(mappedBy = "data", cascade = CascadeType.ALL)
    private List<PurchaseHistory> purchaseHistoryList = new ArrayList<>();

    public void addPurchaseHistory(PurchaseHistory purchaseHistory){
        purchaseHistoryList.add(purchaseHistory);
    }

    public void confirmSeller(Member seller){
        this.seller = seller;
        seller.addData(this);
    }

}
