package uk.jinhy.survey_mate_api.statement.presentation;

import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.jinhy.survey_mate_api.auth.application.service.AuthService;
import uk.jinhy.survey_mate_api.auth.domain.entity.Member;
import uk.jinhy.survey_mate_api.common.response.ApiResponse;
import uk.jinhy.survey_mate_api.common.response.Status;
import uk.jinhy.survey_mate_api.statement.application.service.StatementService;
import uk.jinhy.survey_mate_api.statement.domain.entity.Statement;
import uk.jinhy.survey_mate_api.statement.presentation.converter.StatementConverter;
import uk.jinhy.survey_mate_api.statement.presentation.dto.StatementControllerDTO;

@RequiredArgsConstructor
@RequestMapping("/api/statement")
@RestController
public class StatementController {

    private final StatementService statementService;
    private final AuthService authService;

    private final StatementConverter converter;

    @GetMapping(value = "/list")
    @Operation(summary = "포인트 전체 내역 조회")
    public ApiResponse<StatementControllerDTO.StatementListDTO> getStatementList() {
        Member member = authService.getCurrentMember();

        List<Statement> statementList = statementService.getStatementList(member);
        StatementControllerDTO.StatementListDTO responseDTO = converter.toControllerStatementListDto(
            statementList);

        return ApiResponse.onSuccess(Status.OK.getCode(), Status.OK.getMessage(), responseDTO);
    }

    @GetMapping(value = "/list/buyer")
    @Operation(summary = "포인트 사용 내역 조회")
    public ApiResponse<StatementControllerDTO.StatementListDTO> getStatementListAsBuyer() {
        Member member = authService.getCurrentMember();

        List<Statement> statementList = statementService.getStatementListAsBuyer(member);
        StatementControllerDTO.StatementListDTO responseDTO = converter.toControllerStatementListDto(
            statementList);

        return ApiResponse.onSuccess(Status.OK.getCode(), Status.OK.getMessage(), responseDTO);
    }

    @GetMapping(value = "/list/seller")
    @Operation(summary = "포인트 수령 내역 조회")
    public ApiResponse<StatementControllerDTO.StatementListDTO> getStatementListAsSeller() {
        Member member = authService.getCurrentMember();

        List<Statement> statementList = statementService.getStatementListAsSeller(member);
        StatementControllerDTO.StatementListDTO responseDTO = converter.toControllerStatementListDto(
            statementList);

        return ApiResponse.onSuccess(Status.OK.getCode(), Status.OK.getMessage(), responseDTO);
    }

    @GetMapping(value = "/total")
    @Operation(summary = "전체 포인트 조회")
    public ApiResponse<StatementControllerDTO.TotalAmountDTO> getTotalAmount() {
        Member member = authService.getCurrentMember();

        Long totalAmount = statementService.getTotalAmount(member);
        StatementControllerDTO.TotalAmountDTO responseDTO =
            new StatementControllerDTO.TotalAmountDTO(totalAmount);

        return ApiResponse.onSuccess(Status.OK.getCode(), Status.OK.getMessage(), responseDTO);
    }
}
