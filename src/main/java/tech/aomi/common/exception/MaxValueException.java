package tech.aomi.common.exception;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 最大值异常
 *
 * @author 田尘殇Sean(sean.snow @ live.com) createAt 2016/12/28
 */
public class MaxValueException extends ServiceException {

    private static final long serialVersionUID = -5673857382693628226L;

    private BigDecimal maxValue = BigDecimal.ZERO;

    public MaxValueException() {
        super();
    }

    public MaxValueException(String message, BigDecimal maxValue) {
        this(message, (Throwable) null);
        this.maxValue = maxValue;
    }

    public MaxValueException(String message) {
        super(message);
    }

    public MaxValueException(String message, Throwable cause) {
        super(message, cause);
    }

    public MaxValueException(Throwable cause) {
        super(cause);
    }

    @Override
    public Serializable getErrorCode() {
        return ErrorCode.MAX_VALUE;
    }

    public BigDecimal getMaxValue() {
        return maxValue;
    }

}
