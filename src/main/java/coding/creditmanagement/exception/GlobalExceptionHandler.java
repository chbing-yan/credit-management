package coding.creditmanagement.exception;

import coding.creditmanagement.response.MyResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//@ControllerAdvice
@RestControllerAdvice
public class GlobalExceptionHandler {
    //全局处理，捕获异常并按服务器内部错误处理
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)

    public MyResponse handleException(Exception ex) {
        return  MyResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),ex.getMessage());
    }
    @ExceptionHandler({MethodArgumentNotValidException.class, MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MyResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
        return MyResponse.error(HttpStatus.BAD_REQUEST.value(),ex.getMessage());
    }

}
