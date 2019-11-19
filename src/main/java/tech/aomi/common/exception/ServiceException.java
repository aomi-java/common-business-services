package tech.aomi.common.exception;

import java.io.Serializable;

/**
 * 服务统一异常类
 *
 * @author 田尘殇Sean(sean.snow @ live.com) Create At 16/7/25
 */
public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = -8914766196902007963L;

    public ServiceException() {
        super();
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

    /**
     * @return 错误代码
     */
    public Serializable getErrorCode() {
        return ErrorCode.EXCEPTION;
    }

    /**
     * @return 错误代码对应的数据信息
     */
    public Serializable getPayload() {
        return null;
    }
}

