package me.kjs.mall.common.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.kjs.mall.common.exception.StatusException;
import org.springframework.validation.Errors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto<T> {
    private int status;
    private String message;
    private T data;

    public static <T> ResponseDto ok(T data) {
        return ResponseDto.builder().status(200)
                .message("요청이 성공적으로 수행되었습니다.")
                .data(data)
                .build();
    }

    public static <T> ResponseDto created(T data) {
        return ResponseDto.builder()
                .status(201)
                .data(data)
                .message("요청이 성공적으로 수행되었습니다.").build();
    }

    public static ResponseDto noContent() {
        return ResponseDto.builder()
                .status(204)
                .message("요청이 성공적으로 수행되었습니다.").build();
    }

    public static ResponseDto fail(StatusException exception) {
        return ResponseDto.builder()
                .status(exception.getStatus())
                .message(exception.getMessage())
                .build();
    }


    public static ResponseDto fail(Errors errors) {
        return ResponseDto.builder()
                .status(400)
                .message("입력값을 확인해 주세요.")
                .data(errors)
                .build();
    }

    public static ResponseDto tokenExpired() {
        return ResponseDto.builder()
                .status(401)
                .message("토큰이 만료 되었습니다.")
                .build();
    }
}
