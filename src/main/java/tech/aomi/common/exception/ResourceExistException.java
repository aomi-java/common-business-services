package tech.aomi.common.exception;

/**
 * @author 田尘殇Sean(sean.snow @ live.com) Create At 16/7/25
 */
public class ResourceExistException extends ServiceException {

    private static final long serialVersionUID = -4228680693515151328L;

    public ResourceExistException() {
        super();
    }

    public ResourceExistException(String message) {
        super(message);
    }

    public ResourceExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceExistException(Throwable cause) {
        super(cause);
    }

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.RESOURCE_EXIST;
    }
}

