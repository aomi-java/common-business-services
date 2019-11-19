package tech.aomi.common.exception;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 最小值异常
 *
 * @author 田尘殇Sean(sean.snow @ live.com) createAt 2016/12/28
 */
public class MinValueException extends ServiceException {

    private static final long serialVersionUID = -5673857382693628226L;

    private BigDecimal minValue = BigDecimal.ZERO;

    public MinValueException() {
        super();
    }

    public MinValueException(String message, BigDecimal minValue) {
        this(message, (Throwable) null);
        this.minValue = minValue;
    }

    public MinValueException(String message) {
        super(message);
    }

    public MinValueException(String message, Throwable cause) {
        super(message, cause);
    }

    public MinValueException(Throwable cause) {
        super(cause);
    }

    @Override
    public Serializable getErrorCode() {
        return ErrorCode.MIN_VALUE;
    }

    public BigDecimal getMinValue() {
        return minValue;
    }

}
