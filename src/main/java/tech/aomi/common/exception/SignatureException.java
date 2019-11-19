package tech.aomi.common.exception;

/**
 * @author 田尘殇Sean(sean.snow @ live.com) createAt 2018/7/2
 */
public class SignatureException extends ServiceException {

    private static final long serialVersionUID = -2693525780077183404L;

    public SignatureException() {
        super();
    }

    public SignatureException(String message) {
        super(message);
    }

    public SignatureException(String message, Throwable cause) {
        super(message, cause);
    }

    public SignatureException(Throwable cause) {
        super(cause);
    }

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.SIGNATURE_INVALID;
    }

}
