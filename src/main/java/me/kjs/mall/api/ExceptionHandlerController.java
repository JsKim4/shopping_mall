package me.kjs.mall.api;


import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.common.dto.ResponseDto;
import me.kjs.mall.common.exception.StatusException;
import me.kjs.mall.common.exception.ValidationErrorException;
import me.kjs.mall.connect.exception.FileConvertFailException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;


@RestControllerAdvice
@Slf4j
public class ExceptionHandlerController {

    @ExceptionHandler(StatusException.class)
    public ResponseDto statusExceptionHandler(StatusException ex) {
        printExceptionLog(ex);

        return ResponseDto.fail(ex);
    }

    @ExceptionHandler(ValidationErrorException.class)
    public ResponseDto validationErrorExceptionHandler(ValidationErrorException ex) {
        return ResponseDto.fail(ex.getErrors());
    }

    private void printExceptionLog(Exception ex) {
        log.error("=====================================================");
        log.error(ex.getMessage());

        for (int i = 0; i < ex.getStackTrace().length && i < 3; i++) {
            log.error("ex.getStackTrace[{}].getLineNumber : {}", i, ex.getStackTrace()[i].getLineNumber());
            log.error("ex.getStackTrace[{}].getClassName : {}", i, ex.getStackTrace()[i].getClassName());
        }
        log.error("=====================================================");
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseDto maxUploadSizeExceededExceptionHandler(MaxUploadSizeExceededException ex) {
        return ResponseDto.fail(new FileConvertFailException());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseDto MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        log.error("e.getBindingResult() : {}", e.getBindingResult());
        return ResponseDto.fail(e.getBindingResult());
    }

}
