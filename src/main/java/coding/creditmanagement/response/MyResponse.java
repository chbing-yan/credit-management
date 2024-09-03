package coding.creditmanagement.response;
public class MyResponse<T> {
    private String status;
    private int code;
    private String message;
    private T data;


    // Constructors
    public MyResponse() {
    }

    public MyResponse(String status, int code, String message, T data) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.data = data;

    }

    // Getters and Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


    // Static factory methods for convenience
    public static <T> MyResponse<T> success(T data) {
        return new MyResponse<>("success", 200, "Request processed successfully.", data);
    }

    public static <T> MyResponse<T> error(int code, String message) {
        return new MyResponse<>("fail", code, message, null);
    }
}
