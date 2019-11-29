package tech.aomi.common.exception;

import java.io.Serializable;

/**
 * 拒绝访问异常
 *
 * @author 田尘殇Sean(sean.snow @ live.com) Create At 16/9/9
 */
public class AccessDeniedException extends ServiceException {

    private static final long serialVersionUID = 191621824668303402L;

    public AccessDeniedException() {
        super();
    }

    public AccessDeniedException(String message) {
        super(message);
    }

    public AccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccessDeniedException(Throwable cause) {
        super(cause);
    }

    @Override
    public Serializable getErrorCode() {
        return ErrorCode.ACCESS_DENIED;
    }
}
