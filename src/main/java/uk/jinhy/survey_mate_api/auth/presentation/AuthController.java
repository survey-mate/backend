package uk.jinhy.survey_mate_api.auth.presentation;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.jinhy.survey_mate_api.auth.application.service.AuthService;
import uk.jinhy.survey_mate_api.auth.domain.entity.Member;
import uk.jinhy.survey_mate_api.auth.presentation.dto.LoginControllerDTO;
import uk.jinhy.survey_mate_api.auth.presentation.dto.MemberControllerDTO;
import uk.jinhy.survey_mate_api.common.response.ApiResponse;
import uk.jinhy.survey_mate_api.common.response.Status;
import uk.jinhy.survey_mate_api.jwt.JwtTokenInfo;

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
}
