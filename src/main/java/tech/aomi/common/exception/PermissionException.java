package tech.aomi.common.exception;

/**
 * 无访问权限异常
 *
 * @author 田尘殇Sean(sean.snow @ live.com) Create At 16/9/9
 */
public class PermissionException extends ServiceException {

    private static final long serialVersionUID = 191621824668303402L;

    public PermissionException() {
        super();
    }

    public PermissionException(String message) {
        super(message);
    }

    public PermissionException(String message, Throwable cause) {
        super(message, cause);
    }

    public PermissionException(Throwable cause) {
        super(cause);
    }

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.PERMISSION_ACCESS;
    }
}
