package uk.jinhy.survey_mate_api.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "message", "result"})
public class ApiResponse<T> {

    private final boolean isSuccess;

    private final String status;

    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public static <T> ApiResponse<T> onSuccess(String status, String message, T data) {
        return new ApiResponse<>(true, status, message, data);
    }

    public static <T> ApiResponse<T> onFailure(String status, String message, T data) {
        return new ApiResponse<>(false, status, message, data);
    }

}
