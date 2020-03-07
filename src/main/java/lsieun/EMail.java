package lsieun;

import lsieun.email.Message;
import lsieun.utils.*;

import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.prefs.Preferences;

import static lsieun.utils.LogUtils.*;

public class EMail {
    private static final String email_node = PropertyUtils.getProperty("email.record.node");

    public static void main(String[] args) {
        BannerUtils.display();
        String user_home = System.getProperty("user.home");
        String dir_path = user_home + PropertyUtils.getProperty("email.message.box.dir");
        File dirFile = new File(dir_path);
        dirFile.mkdirs();
        audit.info(() -> "message box dir: " + dir_path);
        audit.info(() -> String.format("email pref node: %s/.java/.userPrefs/%s/", user_home, email_node));


        while (true) {
            try {
                Preferences prefs = Preferences.userRoot().node(email_node);
                File[] files = dirFile.listFiles(file -> file.getName().endsWith(".txt"));
                if (files != null && files.length > 0) {
                    for (File f : files) {
                        String filename = f.getName();
                        long old_ts = prefs.getLong(filename, 0L);
                        List<String> lines = FileUtils.readLines(f.getAbsolutePath());
                        for (String line : lines) {
                            if (StringUtils.isBlank(line)) continue;
                            if (line.startsWith("#")) continue;

                            try {
                                Message m = Message.fromStr(line);
                                if (m == null) continue;

                                long ts = m.timestamp;
                                String title = m.title;
                                String text = m.text;

                                if (ts <= old_ts) continue;

                                EmailUtils.send(title, text);
                                audit.info(() -> String.format("(%s) ===> %s, %s, %s", filename, ts, title, text));
                                prefs.putLong(filename, m.timestamp);
                            } catch (Exception ex) {
                                audit.log(Level.WARNING, "unexpected error: " + ex.getMessage(), ex);
                            }
                        }

                    }
                }
                prefs.flush();

                audit.info("Wake up at " + DateUtils.toReadable(Const.SLEEP_TIME));
                Thread.sleep(Const.SLEEP_TIME);
            } catch (Exception ex) {
                EmailUtils.send("Email Error", "邮件服务出错了，请及时修复！");
                audit.log(Level.WARNING, "unexpected error: " + ex.getMessage(), ex);
                break;
            }
        }
    }
}
