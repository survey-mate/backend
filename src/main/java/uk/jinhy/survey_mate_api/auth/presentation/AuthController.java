package uk.jinhy.survey_mate_api.auth.presentation;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
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
    public ApiResponse<?> join(
            @RequestBody @Valid AuthControllerDTO.MemberRequestDTO requestDTO){
        AuthControllerDTO.MemberResponseDTO memberResponseDTO = authService.join(requestDTO);
        return ApiResponse.onSuccess(Status.CREATED.getCode(),
                Status.CREATED.getMessage(), memberResponseDTO);
    }

    @PostMapping("/login")
    @Operation(summary = "로그인")
    public ApiResponse<?> login(
            @RequestBody @Valid AuthControllerDTO.LoginRequestDTO requestDTO){
        AuthControllerDTO.JwtResponseDTO jwtResponseDTO = authService.login(requestDTO);
        return ApiResponse.onSuccess(Status.OK.getCode(),
                Status.CREATED.getMessage(), jwtResponseDTO);
    }

    @PostMapping("/email/certification-request")
    @Operation(summary = "이메일 인증 코드 요청")
    public ApiResponse<?> sendEmailCode(
            @RequestBody @Valid AuthControllerDTO.CertificateCodeRequestDTO requestDTO
            ){
        authService.sendMailCode(requestDTO);
        return ApiResponse.onSuccess(Status.OK.getCode(),
                Status.OK.getMessage(), null);
    }

    @PostMapping("/email/certification")
    @Operation(summary = "이메일 인증 코드 확인")
    public ApiResponse<?> checkEmailCode(
            @RequestBody @Valid AuthControllerDTO.MailCodeRequestDTO mailCodeDto
            ){
        AuthControllerDTO.EmailCodeResponseDTO emailCodeResponseDTO = authService.checkEmailCode(mailCodeDto);
        return ApiResponse.onSuccess(Status.CREATED.getCode(),
                Status.CREATED.getMessage(), emailCodeResponseDTO);
    }

    @PostMapping("/password/certification-request")
    @Operation(summary = "비밀번호 재설정 인증 코드 요청")
    public ApiResponse<?> sendPasswordResetCode(
            @RequestBody @Valid AuthControllerDTO.CertificateCodeRequestDTO requestDTO
    ){
        authService.sendPasswordResetCode(requestDTO);
        return ApiResponse.onSuccess(Status.OK.getCode(),
                Status.OK.getMessage(), null);
    }

    @PostMapping("/password/certification")
    @Operation(summary = "비밀번호 재설정 인증코드 확인")
    public ApiResponse<?> checkPasswordResetCode(
            @RequestBody @Valid AuthControllerDTO.PasswordResetCodeRequestDTO resetDTO
    ){
        AuthControllerDTO.PasswordResetCodeResponseDTO passwordResetCodeResponseDTO
                = authService.checkPasswordResetCode(resetDTO);
        return ApiResponse.onSuccess(Status.CREATED.getCode(),
                Status.CREATED.getMessage(), passwordResetCodeResponseDTO);
    }

    @PatchMapping("/password/reset")
    @Operation(summary = "비밀번호 재설정")
    public ApiResponse<?> resetPassword(
            @RequestBody @Valid AuthControllerDTO.PasswordResetRequestDTO requestDTO
    ){
        authService.resetPassword(requestDTO);
        return ApiResponse.onSuccess(Status.OK.getCode(),
                Status.OK.getMessage(), null);
    }

    @PatchMapping("/password/update")
    @Operation(summary = "비밀번호 변경")
    public ApiResponse<?> updatePassword(
            @RequestBody @Valid AuthControllerDTO.PasswordUpdateRequestDTO requestDto
    ){
        authService.updatePassword(requestDto);
        return ApiResponse.onSuccess(Status.OK.getCode(),
                Status.OK.getMessage(), null);
    }
}
