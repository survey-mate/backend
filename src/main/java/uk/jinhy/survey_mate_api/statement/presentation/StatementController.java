package uk.jinhy.survey_mate_api.statement.presentation;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import uk.jinhy.survey_mate_api.auth.application.service.AuthService;
import uk.jinhy.survey_mate_api.common.response.ApiResponse;
import uk.jinhy.survey_mate_api.common.response.Status;
import uk.jinhy.survey_mate_api.auth.domain.entity.Member;
import uk.jinhy.survey_mate_api.common.response.exception.GeneralException;
import uk.jinhy.survey_mate_api.statement.application.dto.StatementServiceDTO;
import uk.jinhy.survey_mate_api.statement.application.service.StatementService;
import uk.jinhy.survey_mate_api.statement.domain.entity.Statement;
import uk.jinhy.survey_mate_api.statement.presentation.converter.StatementConverter;
import uk.jinhy.survey_mate_api.statement.presentation.dto.StatementControllerDTO;

import java.util.List;

import static uk.jinhy.survey_mate_api.common.response.ApiResponse.onSuccess;

@RequiredArgsConstructor
@RequestMapping("/statement")
@RestController
public class StatementController {
    private final StatementService statementService;
    private final AuthService authService;

    @GetMapping(value = "/list")
    @Operation(summary = "전체 사용내역 조회")
    public ApiResponse<?> getStatementList() {
        Member member = authService.getCurrentMember();

        if(member == null) {
            throw new GeneralException(Status.UNAUTHORIZED);
        }

        List<Statement> statementList = statementService.getStatementList(member);
        StatementControllerDTO.StatementListDTO responseDTO = new StatementControllerDTO.StatementListDTO(statementList);

        return ApiResponse.onSuccess(Status.OK.getCode(), Status.OK.getMessage(), responseDTO);
    }

    @GetMapping(value = "/total")
    @Operation(summary = "전체 포인트 조회")
    public ApiResponse<?> getTotalAmount() {
        Member member = authService.getCurrentMember();

        if(member == null) {
            throw new GeneralException(Status.UNAUTHORIZED);
        }

        Long totalAmount = statementService.getTotalAmount(member);
        StatementControllerDTO.TotalAmountDTO responseDTO = new StatementControllerDTO.TotalAmountDTO(totalAmount);

        return ApiResponse.onSuccess(Status.OK.getCode(), Status.OK.getMessage(), responseDTO);
    }
}
