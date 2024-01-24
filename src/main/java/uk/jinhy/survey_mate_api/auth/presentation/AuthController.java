package uk.jinhy.survey_mate_api.auth.presentation;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.jinhy.survey_mate_api.auth.application.service.AuthService;
import uk.jinhy.survey_mate_api.auth.domain.entity.Member;
import uk.jinhy.survey_mate_api.auth.presentation.dto.LoginControllerDTO;
import uk.jinhy.survey_mate_api.auth.presentation.dto.MailCodeControllerDTO;
import uk.jinhy.survey_mate_api.auth.presentation.dto.MailControllerDTO;
import uk.jinhy.survey_mate_api.auth.presentation.dto.MemberControllerDTO;
import uk.jinhy.survey_mate_api.common.response.ApiResponse;
import uk.jinhy.survey_mate_api.common.response.Status;


@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/join")
    @Operation(summary = "회원가입")
    public ApiResponse<?> join(MemberControllerDTO.MemberRequestDTO requestDTO){
        Member member = authService.join(requestDTO);
        return ApiResponse.onSuccess(Status.CREATED.getHttpStatus().toString(),
                Status.CREATED.getMessage(), member.getMemberId());
    }

    @PostMapping("/login")
    @Operation(summary = "로그인")
    public ApiResponse<?> login(LoginControllerDTO requestDTO){
        String jwtTokenInfo = authService.login(requestDTO);
        return ApiResponse.onSuccess(Status.OK.getHttpStatus().toString(),
                Status.OK.getMessage(), jwtTokenInfo);
    }

    @PostMapping("/email/certification-request")
    @Operation(summary = "이메일 인증 코드 요청")
    public ApiResponse<?> sendEmailCode(
            @RequestBody MailControllerDTO mailDto
            ){
        String result = authService.sendMailCode(mailDto);
        return ApiResponse.onSuccess(Status.OK.getHttpStatus().toString(),
                Status.OK.getMessage(), result);
    }

    @PostMapping("/email/certification")
    @Operation(summary = "이메일 인증 코드 확인")
    public ApiResponse<?> checkEmailCode(
            @RequestBody MailCodeControllerDTO mailCodeDto
            ){
        String result = authService.checkEmailCode(mailCodeDto);
        return ApiResponse.onSuccess(Status.OK.getHttpStatus().toString(),
                Status.OK.getMessage(), result);
    }
}
