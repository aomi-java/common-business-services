package tech.aomi.common.exception;

import java.io.Serializable;

/**
 * 资源不可用
 * 例如：
 * 资源已经被使用
 * 资源被锁定
 *
 * @author 田尘殇Sean(sean.snow @ live.com) createAt 2017/4/17.
 */
public class ResourceStatusException extends ServiceException {

    private static final long serialVersionUID = -3707905419287218716L;

    public ResourceStatusException() {
        this("资源不可用");
    }

    public ResourceStatusException(String message) {
        super(message);
    }

    public ResourceStatusException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceStatusException(Throwable cause) {
        super(cause);
    }

    @Override
    public Serializable getErrorCode() {
        return ErrorCode.RESOURCE_STATUS;
    }
}
