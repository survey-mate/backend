package uk.jinhy.survey_mate_api.auth.presentation;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.jinhy.survey_mate_api.auth.application.service.AuthService;
import uk.jinhy.survey_mate_api.auth.domain.entity.Member;
import uk.jinhy.survey_mate_api.auth.presentation.dto.AuthControllerDTO;
import uk.jinhy.survey_mate_api.common.response.ApiResponse;
import uk.jinhy.survey_mate_api.common.response.Status;


@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/join")
    @Operation(summary = "회원가입")
    public ApiResponse<?> join(AuthControllerDTO.MemberControllerDTO.MemberRequestDTO requestDTO){
        Member member = authService.join(requestDTO);
        return ApiResponse.onSuccess(Status.CREATED.getHttpStatus().toString(),
                Status.CREATED.getMessage(), member.getMemberId());
    }

    @PostMapping("/login")
    @Operation(summary = "로그인")
    public ApiResponse<?> login(AuthControllerDTO.LoginControllerDTO requestDTO){
        String jwtTokenInfo = authService.login(requestDTO);
        return ApiResponse.onSuccess(Status.OK.getHttpStatus().toString(),
                Status.OK.getMessage(), jwtTokenInfo);
    }

    @PostMapping("/email/certification-request")
    @Operation(summary = "이메일 인증 코드 요청")
    public ApiResponse<?> sendEmailCode(
            @RequestBody AuthControllerDTO.CertificateCodeRequestDTO requestDTO
            ){
        String result = authService.sendMailCode(requestDTO);
        return ApiResponse.onSuccess(Status.OK.getHttpStatus().toString(),
                Status.OK.getMessage(), result);
    }

    @PostMapping("/email/certification")
    @Operation(summary = "이메일 인증 코드 확인")
    public ApiResponse<?> checkEmailCode(
            @RequestBody AuthControllerDTO.MailCodeControllerDTO mailCodeDto
            ){
        String token = authService.checkEmailCode(mailCodeDto);
        return ApiResponse.onSuccess(Status.OK.getHttpStatus().toString(),
                Status.OK.getMessage(), token);
    }

    @PostMapping("/password/certification-request")
    @Operation(summary = "비밀번호 재설정 인증 코드 요청")
    public ApiResponse<?> sendPasswordResetCode(
            @RequestBody AuthControllerDTO.CertificateCodeRequestDTO requestDTO
    ){
        String result = authService.sendPasswordResetCode(requestDTO);
        return ApiResponse.onSuccess(Status.OK.getHttpStatus().toString(),
                Status.OK.getMessage(), result);
    }

    @PostMapping("/password/certification")
    @Operation(summary = "비밀번호 재설정 인증코드 확인")
    public ApiResponse<?> checkPasswordResetCode(
            @RequestBody AuthControllerDTO.PasswordResetCodeDTO resetDTO
    ){
        String token = authService.checkPasswordResetCode(resetDTO);
        return ApiResponse.onSuccess(Status.OK.getHttpStatus().toString(),
                Status.OK.getMessage(), token);
    }

    @PostMapping("/password/reset")
    @Operation(summary = "비밀번호 재설정")
    public ApiResponse<?> resetPassword(
            @RequestBody AuthControllerDTO.PasswordResetControllerDTO requstDTO
    ){
        String memberId = authService.resetPassword(requstDTO);
        return ApiResponse.onSuccess(Status.OK.getHttpStatus().toString(),
                Status.OK.getMessage(), memberId + "계정의 비밀번호가 수정되었습니다.");
    }

    @PostMapping("/password/update")
    @Operation(summary = "비밀번호 변경")
    public ApiResponse<?> updatePassword(
            @RequestBody AuthControllerDTO.PasswordUpdateRequestDTO requestDto
    ){
        String memberId = authService.updatePassword(requestDto);
        return ApiResponse.onSuccess(Status.OK.getHttpStatus().toString(),
                Status.OK.getMessage(), memberId + "계정의 비밀번호가 수정되었습니다.");
    }
}
