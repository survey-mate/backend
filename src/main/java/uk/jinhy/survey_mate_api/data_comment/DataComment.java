package uk.jinhy.survey_mate_api.data_comment;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
import uk.jinhy.survey_mate_api.data.Data;
import uk.jinhy.survey_mate_api.auth.domain.entity.Member;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DataComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @NotNull
    private Member dataCommenter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "data_id")
    @NotNull
    private Data data;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private DataComment parentDataComment;

    @Builder.Default
    @OneToMany(mappedBy = "parentDataComment", orphanRemoval = true)
    private List<DataComment> children = new ArrayList<>();

    @NotNull
    private String content;

    @CreatedDate
    private LocalDateTime createdAt;

    public void confirmDataCommenter(Member dataCommenter){
        this.dataCommenter = dataCommenter;
        dataCommenter.addDataComment(this);
    }

    public void confirmData(Data data){
        this.data = data;
        data.addDataComment(this);
    }

}
