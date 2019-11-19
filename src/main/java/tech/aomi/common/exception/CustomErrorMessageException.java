package tech.aomi.common.exception;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author 田尘殇Sean(sean.snow @ live.com) createAt 2016/10/13
 */
public class CustomErrorMessageException extends ServiceException {

    private static final long serialVersionUID = -6212039500948066739L;

    private String status;

    private String message;

    public CustomErrorMessageException(String msg) {
        super(msg);
    }

    public CustomErrorMessageException(String status, String message) {
        super("[" + status + "]" + message);
        this.status = status;
        this.message = message;
    }

    public CustomErrorMessageException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomErrorMessageException(Throwable cause) {
        super(cause);
    }

    @Override
    public Serializable getErrorCode() {
        return ErrorCode.CUSTOM_ERROR_MESSAGE;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public Serializable getPayload() {
        HashMap<String, String> res = new HashMap<>();
        res.put("status", status);
        res.put("message", message);
        return res;
    }
}
