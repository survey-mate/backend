package uk.jinhy.survey_mate_api.statement.presentation;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.jinhy.survey_mate_api.common.response.ApiResponse;
import uk.jinhy.survey_mate_api.common.response.Status;
import uk.jinhy.survey_mate_api.member.Member;
import uk.jinhy.survey_mate_api.statement.application.dto.StatementServiceDTO;
import uk.jinhy.survey_mate_api.statement.application.service.StatementService;
import uk.jinhy.survey_mate_api.statement.domain.entity.Statement;
import uk.jinhy.survey_mate_api.statement.presentation.converter.StatementConverter;
import uk.jinhy.survey_mate_api.statement.presentation.dto.StatementControllerDTO;

import java.util.List;

import static uk.jinhy.survey_mate_api.common.response.ApiResponse.onSuccess;

@RequiredArgsConstructor
@RequestMapping("/statement")
@Controller
public class StatementController {
    private final StatementService statementService;

    @PostMapping(value = "/create")
    @Operation(summary = "사용내역 생성")
    public ApiResponse<?> createStatement(@ModelAttribute StatementControllerDTO.CreateStatementDTO controllerDTO) {
        Member member = controllerDTO.getMember();
        StatementServiceDTO.CreateStatementDTO serviceDTO = StatementConverter.convertCreateDto(controllerDTO);

        // TODO
        // 인증 실패 시 예외처리

        Statement createdStatement = statementService.createStatement(member, serviceDTO);

        return ApiResponse.onSuccess(Status.CREATED.getHttpStatus().toString(), Status.CREATED.getMessage(), createdStatement.getStatementId());
    }

    @GetMapping(value = "/list")
    @Operation(summary = "전체 사용내역 조회")
    public ApiResponse<?> getStatementList(@ModelAttribute StatementControllerDTO.GetStatementDTO controllerDTO) {
        Member member = controllerDTO.getMember();

        // TODO
        // 인증 실패 시 예외처리

        List<Statement> statementList = statementService.getStatementList(member);

        return ApiResponse.onSuccess(Status.OK.getHttpStatus().toString(), Status.OK.getMessage(), statementList);
    }

    @GetMapping(value = "/total")
    @Operation(summary = "전체 포인트 조회")
    public ApiResponse<?> getTotalAmount(@ModelAttribute StatementControllerDTO.GetStatementDTO controllerDTO) {
        Member member = controllerDTO.getMember();

        // TODO
        // 인증 실패 시 예외처리

        Long totalAmount = statementService.getTotalAmount(member);

        return ApiResponse.onSuccess(Status.OK.getHttpStatus().toString(), Status.OK.getMessage(), totalAmount);
    }
}
