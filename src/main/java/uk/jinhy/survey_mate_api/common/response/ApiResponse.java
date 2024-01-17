package uk.jinhy.survey_mate_api.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "message", "result"})
public class ApiResponse<T> {

    private final boolean isSuccess;

    private final String status;

    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object data;

   /* public static <T> ApiResponse<T> onSuccess(T result){
        return new ApiResponse<>();
    }
*/
    public static <T> ApiResponse<T> onFailure (String status, String message, Object data){
        return new ApiResponse<>(false, status, message, data);
    }

}
