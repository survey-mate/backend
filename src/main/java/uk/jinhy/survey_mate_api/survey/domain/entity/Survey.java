package uk.jinhy.survey_mate_api.survey.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import uk.jinhy.survey_mate_api.auth.domain.entity.Member;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Survey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long surveyId;

    @CreatedDate
    private LocalDateTime createdAt;

    @NotNull
    private LocalDateTime endedAt;

    @NotNull
    private String linkUrl;

    @NotNull
    private Long reward;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member registrant;

    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    private String rewardUrl;

    @Builder.Default
    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL)
    private List<Answer> answerList = new ArrayList<>();

    public void addAnswer(Answer answer) {
        answerList.add(answer);
    }

    @Builder.Default
    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL)
    private List<SurveyComment> surveyCommentList = new ArrayList<>();

    public void addSurveyComment(SurveyComment surveyComment) {
        surveyCommentList.add(surveyComment);
    }

    public void confirmRegistrant(Member registrant) {
        this.registrant = registrant;
        registrant.addSurvey(this);
    }

    public boolean isSurveyEnded() {
        LocalDateTime currentTime = LocalDateTime.now();
        return currentTime.isBefore(this.endedAt);
    }

    public boolean isAnswered(Member member) {
        return answerList.stream()
            .anyMatch(a -> a.getRespondent().equals(member));
    }

    public void updateTitle(String newTitle) {
        title = newTitle;
    }

    public void updateLinkUrl(String newLinkUrl) {
        linkUrl = newLinkUrl;
    }

    public void updateDescription(String newDescription) {
        description = newDescription;
    }
}
