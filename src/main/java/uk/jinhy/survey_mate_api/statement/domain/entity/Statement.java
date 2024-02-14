package uk.jinhy.survey_mate_api.statement.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import uk.jinhy.survey_mate_api.auth.domain.entity.Member;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Statement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long statementId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @NotNull
    private Member member;

    @NotNull
    private Long amount;

    @CreatedDate
    private LocalDateTime createdAt;

    @NotNull
    private String description;

    @NotNull
    private Long balance;

    public void confirmMember(Member member) {
        this.member = member;
        member.addStatement(this);
    }

}
