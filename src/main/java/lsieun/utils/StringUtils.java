package lsieun.utils;

public class StringUtils {
    public static boolean isBlank(final String str) {
        if (str == null) return true;
        if ("".equals(str.trim())) return true;
        return false;
    }
}
