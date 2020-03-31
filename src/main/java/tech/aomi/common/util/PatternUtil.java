package tech.aomi.common.util;

import java.util.regex.Pattern;

/**
 * @author 田尘殇Sean(sean.snow @ live.com) createAt 2018/6/25
 */
public class PatternUtil {

    /**
     * email
     */
    public static final Pattern EMAIL = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
    /**
     * phoneNo
     */
    public static final Pattern PHONE_NO = Pattern.compile("[1]\\d{10}");


    public static boolean isEmail(String email) {
        return EMAIL.matcher(email).matches();
    }

    public static boolean isPhoneNo(String phoneNo) {
        return PHONE_NO.matcher(phoneNo).matches();
    }

}
