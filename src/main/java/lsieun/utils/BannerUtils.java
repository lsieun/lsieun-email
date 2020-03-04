package lsieun.utils;

import java.util.Formatter;
import java.util.List;

import static lsieun.utils.LogUtils.audit;

public class BannerUtils {
    public static void display() {
        String banner_path = PropertyUtils.getRootPath() + "banner.txt";
        List<String> lines = FileUtils.readLines(banner_path);

        StringBuilder sb = new StringBuilder();
        Formatter fm = new Formatter(sb);
        fm.format("%n");
        for (String line : lines) {
            fm.format("%s%n", line);
        }
        audit.info(() -> sb.toString());
    }
}
