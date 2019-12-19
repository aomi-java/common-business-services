package tech.aomi.common.web.security;

import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Sean Create At 2019/12/19
 */
@AllArgsConstructor
public abstract class SecurityExpression {

    private String value;

    private String method;

    public static <T extends SecurityExpression> String and(SecurityExpression... items) {
        List<String> tmp = Arrays.stream(items).map(SecurityExpression::toString).collect(Collectors.toList());
        return String.join(" and ", tmp);
    }

    public static  String and(List<SecurityExpression> items) {
        List<String> tmp = items.stream().map(SecurityExpression::toString).collect(Collectors.toList());
        return String.join(" and ", tmp);
    }

    public static String or(SecurityExpression... items) {
        List<String> tmp = Arrays.stream(items).map(SecurityExpression::toString).collect(Collectors.toList());
        return String.join(" or ", tmp);
    }

    public static String or(List<SecurityExpression> items) {
        List<String> tmp = items.stream().map(SecurityExpression::toString).collect(Collectors.toList());
        return String.join(" or ", tmp);
    }

    @Override
    public String toString() {
        return method + "('" + value + "')";
    }
}
