package lsieun.utils;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Formatter;

public class ByteUtils {
    private static final String HEX_STR = "0123456789abcdef";

    public static String toStr(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static String toStr(byte[] bytes, String charset) {
        return new String(bytes, Charset.forName(charset));
    }

    public static String toStr(byte[] bytes, Charset charset) {
        return new String(bytes, charset);
    }

    public static String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        Formatter fm = new Formatter(sb);
        for (byte b : bytes) {
            int value = b & 0xFF;
            int hi = value >> 4;
            int lo = value & 0x0F;
            fm.format("%c%c", HEX_STR.charAt(hi), HEX_STR.charAt(lo));
        }
        return sb.toString();
    }

    public static byte[] fromInt(int x) {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.putInt(x);
        return buffer.array();
    }

    public static int toInt(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.put(bytes);
        buffer.flip();//need flip
        return buffer.getInt();
    }

    public static byte[] fromLong(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(x);
        return buffer.array();
    }

    public static long toLong(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.put(bytes);
        buffer.flip();//need flip
        return buffer.getLong();
    }

    public static byte[] fromHex(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            char hi_ch = hexString.charAt(i);
            char lo_ch = hexString.charAt(i + 1);
            int hi = HEX_STR.indexOf(hi_ch);
            int lo = HEX_STR.indexOf(lo_ch);
            data[i / 2] = (byte) (hi << 4 | lo);
        }
        return data;
    }
}
