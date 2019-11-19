package tech.aomi.common.exception;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 版本异常错误
 *
 * @author Sean createAt 2019-01-03
 */
public class VersionException extends ServiceException {

    private static final long serialVersionUID = 8290946496049763067L;

    /**
     * 期望版本
     */
    private Object expectVersion;

    /**
     * 实际版本
     */
    private Object version;

    public VersionException() {
        this("版本不符合");
    }

    public VersionException(String msg) {
        this(msg, null, null);
    }

    public VersionException(Object version, Object expectVersion) {
        this("版本不符,预期版本:" + expectVersion + ", 实际版本:" + version, version, expectVersion);
    }

    public VersionException(String msg, Object version, Object expectVersion) {
        super(msg);
        this.version = version;
        this.expectVersion = expectVersion;
    }

    @Override
    public Serializable getErrorCode() {
        return ErrorCode.VERSION_ERROR;
    }

    @Override
    public Serializable getPayload() {
        HashMap<String, Object> payload = new HashMap<>();
        payload.put("version", version);
        payload.put("expectVersion", expectVersion);
        return payload;
    }
}
