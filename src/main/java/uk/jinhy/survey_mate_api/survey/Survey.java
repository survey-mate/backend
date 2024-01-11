package uk.jinhy.survey_mate_api.survey;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.awt.image.MultiPixelPackedSampleModel;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import jdk.jfr.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.jinhy.survey_mate_api.answer.Answer;
import uk.jinhy.survey_mate_api.member.Member;
import uk.jinhy.survey_mate_api.surveyComment.SurveyComment;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Survey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long surveyId;

    private String linkUrl;

    private Long goal;

    private Long reward;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member registrant;

    private Long numberOfQuestions;

    @Timestamp
    private LocalDateTime estimatedTime;

    @Enumerated(EnumType.STRING)
    private SurveyStatus status;

    private String rewardUrl;

    @Builder.Default
    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL)
    private List<Answer> answerList = new ArrayList<>();

    public void addAnswer(Answer answer){
        answerList.add(answer);
    }

    @Builder.Default
    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL)
    private List<SurveyComment> surveyCommentList = new ArrayList<>();

    public void addSurveyComment(SurveyComment surveyComment){
        surveyCommentList.add(surveyComment);
    }

    public void confirmRegistrant(Member registrant){
        this.registrant = registrant;
        registrant.addSurvey(this);
    }
}
