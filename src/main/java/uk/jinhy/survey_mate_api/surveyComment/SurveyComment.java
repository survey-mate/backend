package uk.jinhy.survey_mate_api.surveyComment;

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
import uk.jinhy.survey_mate_api.member.Member;
import uk.jinhy.survey_mate_api.survey.domain.Survey;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SurveyComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @CreatedDate
    private LocalDateTime createdAt;

    @NotNull
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @NotNull
    private Member surveyCommenter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id")
    @NotNull
    private Survey survey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private SurveyComment parentSurveyComment;

    @Builder.Default
    @OneToMany(mappedBy = "parentSurveyComment", orphanRemoval = true)
    private List<SurveyComment> children = new ArrayList<>();

    public void confirmSurveyCommenter(Member surveyCommenter){
        this.surveyCommenter = surveyCommenter;
        surveyCommenter.addSurveyComment(this);
    }

    public void confirmSurvey(Survey survey){
        this.survey = survey;
        survey.addSurveyComment(this);
    }
}
