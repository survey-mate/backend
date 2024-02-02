package uk.jinhy.survey_mate_api.auth.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.jinhy.survey_mate_api.auth.domain.entity.Member;

public interface MemberRepository extends JpaRepository<Member, String> {

    boolean existsByMemberId(String id);

    boolean existsByNickname(String nickname);

}
