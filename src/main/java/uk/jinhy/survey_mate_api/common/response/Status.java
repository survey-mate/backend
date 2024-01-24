package uk.jinhy.survey_mate_api.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import uk.jinhy.survey_mate_api.common.response.Body;

@Getter
@AllArgsConstructor
public enum Status {
    OK(HttpStatus.OK, "COMMON200", "성공입니다."),
    CREATED(HttpStatus.CREATED, "COMMON201", "생성되었습니다."),

    BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),
    CONFLICT(HttpStatus.CONFLICT, "COMMON409", "이미 생성되었습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버에 오류가 발생했습니다."),

    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER404", "해당 아이디는 존재하지 않습니다."),
    PASSWORD_INCORRECT(HttpStatus.BAD_REQUEST, "MEMBER400", "비밀번호가 틀렸습니다."),

    JWT_NULL(HttpStatus.UNAUTHORIZED, "JWT401", "JWT가 NULL입니다."),
    JWT_INVALID(HttpStatus.FORBIDDEN, "JWT404", "JWT가 유효하지 않습니다."),

    MAIL_SEND_FAIL(HttpStatus.BAD_REQUEST, "MAIL400", "메일 전송에 실패했습니다."),
    DUPLICATE_MAIL(HttpStatus.CONFLICT, "MAIL409", "이미 가입된 이메일 입니다."),
    MAIL_CODE_DIFFERENT(HttpStatus.NOT_FOUND, "MAILCODE404", "이메일 인증 코드가 일치하지 않습니다."),
    MAIL_CODE_TIME_OUT(HttpStatus.UNAUTHORIZED, "MAILCODE401", "이메일 인증 코드 유효시간이 지났습니다."),
    MAIL_TOKEN_INVALID(HttpStatus.BAD_REQUEST, "MAILTOKEN400", "이메일 인증 토큰이 유효하지 않습니다."),
    NO_SUCH_ALGORITHM(HttpStatus.NOT_FOUND, "MAIL404", "코드 생성 알고리즘을 선택하지 못했습니다."),

    STATEMENT_NOT_ENOUGH(HttpStatus.BAD_REQUEST, "STATEMENT400", "포인트가 부족합니다."),

;
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    public Body getBody() {
        return Body.builder()
                .message(message)
                .code(code)
                .isSuccess(httpStatus.is2xxSuccessful())
                .httpStatus(httpStatus)
                .build();
    }
}