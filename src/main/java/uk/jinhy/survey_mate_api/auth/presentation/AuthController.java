package uk.jinhy.survey_mate_api.auth.presentation;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.jinhy.survey_mate_api.auth.application.service.AuthService;
import uk.jinhy.survey_mate_api.auth.domain.entity.Member;
import uk.jinhy.survey_mate_api.auth.presentation.dto.AuthControllerDTO;
import uk.jinhy.survey_mate_api.auth.presentation.dto.CodeResponseDTO;
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
        String mailValidationCode = authService.sendMailCode(requestDTO);
        CodeResponseDTO codeResponseDTO = CodeResponseDTO.builder()
                .code(mailValidationCode)
                .build();
        return ApiResponse.onSuccess(Status.OK.getCode(),
                Status.OK.getMessage(), codeResponseDTO);
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
        String accountValidationCode = authService.sendPasswordResetCode(requestDTO);
        CodeResponseDTO codeResponseDTO = CodeResponseDTO.builder()
                .code(accountValidationCode)
                .build();
        return ApiResponse.onSuccess(Status.OK.getCode(),
                Status.OK.getMessage(), codeResponseDTO);
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

    @DeleteMapping("/account")
    @Operation(summary = "회원 탈퇴")
    public ApiResponse<?> deleteAccount(
            @RequestBody AuthControllerDTO.DeleteAccountRequestDTO requestDTO
    ){
        authService.deleteAccount(requestDTO);
        return ApiResponse.onSuccess(Status.OK.getCode(),
                Status.OK.getMessage(), null);
    }

    @GetMapping("/account/isStudent")
    @Operation(summary = "학생 계정 확인")
    public ApiResponse<?> isStudentAccount(){
        boolean isStudentAccount = authService.isStudentAccount();
        AuthControllerDTO.IsStudentAccountResponseDTO responseDto
                = AuthControllerDTO.IsStudentAccountResponseDTO.builder()
                .isStudentAccount(isStudentAccount)
                .build();
        return ApiResponse.onSuccess(Status.OK.getCode(),
                Status.OK.getMessage(), responseDto);
    }

}
