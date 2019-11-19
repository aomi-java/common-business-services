package tech.aomi.common.exception;

/**
 * @author Sean createAt 2018/11/22
 */
public class ExpiredException extends ServiceException {

    private static final long serialVersionUID = 3590770639688528840L;

    public ExpiredException() {
        super();
    }

    public ExpiredException(String message) {
        super(message);
    }

    public ExpiredException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExpiredException(Throwable cause) {
        super(cause);
    }

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.EXPIRED_ERROR;
    }
}
