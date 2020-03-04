package lsieun.email;

import lsieun.utils.StringUtils;

public class Message {
    public final long timestamp;
    public final String title;
    public final String text;

    public Message(long timestamp, String title, String text) {
        this.timestamp = timestamp;
        this.title = title;
        this.text = text;
    }

    public static String toStr(Message m) {
        return String.format("%s,%s,%s",m.timestamp, m.title, m.text);
    }

    public static Message fromStr(final String str) {
        if (StringUtils.isBlank(str)) return null;
        String[] array = str.split(",", 3);
        long ts = Long.parseLong(array[0]);
        String title = array[1];
        String text = array[2];
        return new Message(ts, title, text);
    }
}
