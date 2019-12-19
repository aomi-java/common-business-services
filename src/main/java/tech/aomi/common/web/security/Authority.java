package tech.aomi.common.web.security;

/**
 * @author Sean Create At 2019/12/19
 */
public class Authority extends SecurityExpression {

    private Authority(String value, String method) {
        super(value, method);
    }

    public Authority(String authority) {
        this(authority, "hasAuthority");
    }

    public static String toString(String authority) {
        return new Authority(authority).toString();
    }

}
