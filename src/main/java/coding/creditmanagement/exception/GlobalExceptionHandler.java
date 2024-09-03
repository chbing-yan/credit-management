package coding.creditmanagement.exception;

import coding.creditmanagement.response.MyResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {
    //全局处理，捕获异常并按服务器内部错误处理
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public MyResponse handleException(Exception ex) {
        return  MyResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),ex.getMessage());
    }

}
