package tech.aomi.common.web.security;

/**
 * @author Sean Create At 2019/12/19
 */
public class Role extends SecurityExpression {
    private Role(String value, String method) {
        super(value, method);
    }

    public Role(String role) {
        this(role, "hasRole");
    }

    public static String toString(String role) {
        return new Role(role).toString();
    }
}
