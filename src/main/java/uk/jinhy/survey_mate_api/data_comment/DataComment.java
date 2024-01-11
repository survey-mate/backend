package uk.jinhy.survey_mate_api.data_comment;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import jdk.jfr.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.jinhy.survey_mate_api.data.Data;
import uk.jinhy.survey_mate_api.member.Member;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member dataCommenter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "data_id")
    private Data data;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private DataComment parentDataComment;

    @Builder.Default
    @OneToMany(mappedBy = "parentDataComment", orphanRemoval = true)
    private List<DataComment> children = new ArrayList<>();

    private String content;

    @Timestamp
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
