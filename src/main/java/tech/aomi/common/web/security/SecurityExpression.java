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

    public static <T> String and(T... items) {
        List<String> tmp = Arrays.stream(items).map(Object::toString).collect(Collectors.toList());
        return String.join(" and ", tmp);
    }

    public static <T> String and(List<T> items) {
        List<String> tmp = items.stream().map(Object::toString).collect(Collectors.toList());
        return String.join(" and ", tmp);
    }

    public static <T> String or(T... items) {
        List<String> tmp = Arrays.stream(items).map(Object::toString).collect(Collectors.toList());
        return String.join(" or ", tmp);
    }

    public static <T> String or(List<T> items) {
        List<String> tmp = items.stream().map(Object::toString).collect(Collectors.toList());
        return String.join(" or ", tmp);
    }

    @Override
    public String toString() {
        return method + "('" + value + "')";
    }
}
