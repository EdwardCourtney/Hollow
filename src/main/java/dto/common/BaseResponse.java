package dto.common;

public class BaseResponse {
    public boolean status;
    public String message;

    public BaseResponse() {
    }

    public BaseResponse(boolean status, String message){
        this.status = status;
        this.message = message;
    }
}
