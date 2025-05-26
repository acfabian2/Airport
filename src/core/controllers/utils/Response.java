package core.controllers.utils;

public class Response {

    private final int code; 
    private final String message; 
    private final Object data;  

    public Response(int code, String message) {
        this(code, message, null);
    }

    public Response(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // Getters
    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    public boolean isSuccess() {
        return code == 200;
    }
}
