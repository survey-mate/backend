package uk.jinhy.survey_mate_api.auth.presentation;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.jinhy.survey_mate_api.auth.application.dto.AuthServiceDTO;
import uk.jinhy.survey_mate_api.auth.application.service.AuthService;
import uk.jinhy.survey_mate_api.auth.presentation.converter.AuthConverter;
import uk.jinhy.survey_mate_api.auth.presentation.dto.AuthControllerDTO;
import uk.jinhy.survey_mate_api.auth.presentation.dto.CodeResponseDTO;
import uk.jinhy.survey_mate_api.common.response.ApiResponse;
import uk.jinhy.survey_mate_api.common.response.Status;


@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    private final AuthConverter authConverter;

    @PostMapping("/join")
    @Operation(summary = "회원가입")
    public ApiResponse<?> join(
        @RequestBody @Valid AuthControllerDTO.MemberRequestDTO requestDTO) {
        AuthServiceDTO.MemberDTO memberDTO = authConverter.toServiceMemberDTO(requestDTO);
        AuthControllerDTO.MemberResponseDTO memberResponseDTO = authService.join(memberDTO);
        return ApiResponse.onSuccess(Status.CREATED.getCode(),
            Status.CREATED.getMessage(), memberResponseDTO);
    }

    @PostMapping("/login")
    @Operation(summary = "로그인")
    public ApiResponse<?> login(
        @RequestBody @Valid AuthControllerDTO.LoginRequestDTO requestDTO) {
        AuthServiceDTO.LoginDTO loginDTO = authConverter.toServiceLoginDTO(requestDTO);
        AuthControllerDTO.JwtResponseDTO jwtResponseDTO = authService.login(loginDTO);
        return ApiResponse.onSuccess(Status.OK.getCode(),
            Status.CREATED.getMessage(), jwtResponseDTO);
    }

    @PostMapping("/email/certification-request")
    @Operation(summary = "이메일 인증 코드 요청")
    public ApiResponse<?> sendEmailCode(
        @RequestBody @Valid AuthControllerDTO.CertificateCodeRequestDTO requestDTO
    ) {
        AuthServiceDTO.CertificateCodeDTO certificateCodeDTO = authConverter.toServiceCertificateCodeDTO(requestDTO);
        authService.sendMailCode(certificateCodeDTO);
        return ApiResponse.onSuccess(Status.OK.getCode(),
            Status.OK.getMessage(), null);
    }

    @PostMapping("/email/certification")
    @Operation(summary = "이메일 인증 코드 확인")
    public ApiResponse<?> checkEmailCode(
        @RequestBody @Valid AuthControllerDTO.MailCodeRequestDTO requestDTO
    ) {
        AuthServiceDTO.MailCodeDTO mailCodeDTO = authConverter.toServiceMailCodeDTO(requestDTO);
        AuthControllerDTO.EmailCodeResponseDTO emailCodeResponseDTO = authService.checkEmailCode(mailCodeDTO);
        return ApiResponse.onSuccess(Status.CREATED.getCode(),
            Status.CREATED.getMessage(), emailCodeResponseDTO);
    }

    @PostMapping("/password/certification-request")
    @Operation(summary = "비밀번호 재설정 인증 코드 요청")
    public ApiResponse<?> sendPasswordResetCode(
        @RequestBody @Valid AuthControllerDTO.CertificateCodeRequestDTO requestDTO
    ) {
        AuthServiceDTO.CertificateCodeDTO certificateCodeDTO = authConverter.toServiceCertificateCodeDTO(requestDTO);
        authService.sendPasswordResetCode(certificateCodeDTO);
        return ApiResponse.onSuccess(Status.OK.getCode(),
            Status.OK.getMessage(), null);
    }

    @PostMapping("/password/certification")
    @Operation(summary = "비밀번호 재설정 인증코드 확인")
    public ApiResponse<?> checkPasswordResetCode(
        @RequestBody @Valid AuthControllerDTO.PasswordResetCodeRequestDTO resetDTO
    ) {
        AuthServiceDTO.PasswordResetCodeDTO passwordResetCodeDTO = authConverter.toServicePasswordResetCodeDTO(resetDTO);
        AuthControllerDTO.PasswordResetCodeResponseDTO passwordResetCodeResponseDTO
            = authService.checkPasswordResetCode(passwordResetCodeDTO);
        return ApiResponse.onSuccess(Status.CREATED.getCode(),
            Status.CREATED.getMessage(), passwordResetCodeResponseDTO);
    }

    @PatchMapping("/password/reset")
    @Operation(summary = "비밀번호 재설정")
    public ApiResponse<?> resetPassword(
        @RequestBody @Valid AuthControllerDTO.PasswordResetRequestDTO requestDTO
    ) {
        AuthServiceDTO.PasswordResetDTO passwordResetDTO = authConverter.toServicePasswordResetDTO(requestDTO);
        authService.resetPassword(passwordResetDTO);
        return ApiResponse.onSuccess(Status.OK.getCode(),
            Status.OK.getMessage(), null);
    }

    @PatchMapping("/password/update")
    @Operation(summary = "비밀번호 변경")
    public ApiResponse<?> updatePassword(
        @RequestBody @Valid AuthControllerDTO.PasswordUpdateRequestDTO requestDto
    ) {
        AuthServiceDTO.PasswordUpdateDTO passwordUpdateDTO = authConverter.toServicePasswordUpdateDTO(requestDto);
        authService.updatePassword(passwordUpdateDTO);
        return ApiResponse.onSuccess(Status.OK.getCode(),
            Status.OK.getMessage(), null);
    }

    @DeleteMapping("/account")
    @Operation(summary = "회원 탈퇴")
    public ApiResponse<?> deleteAccount(
        @RequestBody AuthControllerDTO.DeleteAccountRequestDTO requestDTO
    ) {
        AuthServiceDTO.DeleteAccountDTO deleteAccountDTO = authConverter.toServiceDeleteAccountDTO(requestDTO);
        authService.deleteAccount(deleteAccountDTO);
        return ApiResponse.onSuccess(Status.OK.getCode(),
            Status.OK.getMessage(), null);
    }

    @GetMapping("/account/isStudent")
    @Operation(summary = "학생 계정 확인")
    public ApiResponse<?> isStudentAccount() {
        boolean isStudentAccount = authService.isStudentAccount();
        AuthControllerDTO.IsStudentAccountResponseDTO responseDto
            = AuthControllerDTO.IsStudentAccountResponseDTO.builder()
            .isStudentAccount(isStudentAccount)
            .build();
        return ApiResponse.onSuccess(Status.OK.getCode(),
            Status.OK.getMessage(), responseDto);
    }

    @GetMapping("/nickname/{nickname}")
    @Operation(summary = "닉네임 중복 확인")
    public ApiResponse<?> checkNickname(
            @PathVariable("nickname") String nickname) {
        boolean isExist = authService.checkNickname(nickname);
        AuthControllerDTO.IsNicknameExistResponseDTO responseDTO
                = AuthControllerDTO.IsNicknameExistResponseDTO.builder()
                .isNicknameExist(isExist)
                .build();
        return ApiResponse.onSuccess(Status.OK.getCode(),
                Status.OK.getMessage(), responseDTO);
    }

}
