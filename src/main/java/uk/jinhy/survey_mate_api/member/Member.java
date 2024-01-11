package uk.jinhy.survey_mate_api.member;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.jinhy.survey_mate_api.answer.Answer;
import uk.jinhy.survey_mate_api.data.Data;
import uk.jinhy.survey_mate_api.data_comment.DataComment;
import uk.jinhy.survey_mate_api.deviceToken.DeviceToken;
import uk.jinhy.survey_mate_api.notification.Notification;
import uk.jinhy.survey_mate_api.purchaseHistory.PurchaseHistory;
import uk.jinhy.survey_mate_api.statement.Statement;
import uk.jinhy.survey_mate_api.survey.Survey;
import uk.jinhy.survey_mate_api.surveyComment.SurveyComment;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    private String nickname;

    private String email;

    private String password;

    private boolean messageConsent;

    private boolean marketingConsent;

    private String school;

    private String yearOfAdmission;

    private Long point;

    private String profileUrl;

    @Builder.Default
    @OneToMany(mappedBy = "respondent", cascade = CascadeType.ALL)
    private List<Answer> answerList = new ArrayList<>();

    public void addAnswer(Answer answer){
        answerList.add(answer);
    }

    @Builder.Default
    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    private List<Data> dataList = new ArrayList<>();

    public void addData(Data data){
        dataList.add(data);
    }

    @Builder.Default
    @OneToMany(mappedBy = "dataCommenter", cascade = CascadeType.ALL)
    private List<DataComment> dataCommentList = new ArrayList<>();

    public void addDataComment(DataComment dataComment){
        dataCommentList.add(dataComment);
    }

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<DeviceToken> deviceTokenList = new ArrayList<>();

    public void addDeviceToken(DeviceToken deviceToken){
        deviceTokenList.add(deviceToken);
    }

    @Builder.Default
    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
    private List<Notification> notificationList = new ArrayList<>();

    public void addNotification(Notification notification){
        notificationList.add(notification);
    }

    @Builder.Default
    @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL)
    private List<PurchaseHistory> purchaseHistoryList = new ArrayList<>();

    public void addPurchaseHistory(PurchaseHistory purchaseHistory){
        purchaseHistoryList.add(purchaseHistory);
    }

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Statement> statementList = new ArrayList<>();

    public void addStatement(Statement statement){
        statementList.add(statement);
    }

    @Builder.Default
    @OneToMany(mappedBy = "registrant", cascade = CascadeType.ALL)
    private List<Survey> surveyList = new ArrayList<>();

    public void addSurvey(Survey survey){
        surveyList.add(survey);
    }

    @Builder.Default
    @OneToMany(mappedBy = "surveyCommenter", cascade = CascadeType.ALL)
    private List<SurveyComment> surveyCommentList = new ArrayList<>();

    public void addSurveyComment(SurveyComment surveyComment){
        surveyCommentList.add(surveyComment);
    }

}
