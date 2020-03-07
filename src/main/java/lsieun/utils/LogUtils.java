package lsieun.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.*;

public class LogUtils {
    public final static Logger audit = Logger.getLogger("audit");

    static {
        try {
            InputStream in = LogUtils.class.getClassLoader().getResourceAsStream("logging.properties");

            Properties props = new Properties();
            props.load(in);

            String destination = props.getProperty("log.output.destination");

            String format = props.getProperty("log.output.format");
            System.setProperty("java.util.logging.SimpleFormatter.format", format);

            String log_level = props.getProperty("log.level");
            Level level = Level.parse(log_level);


            Handler handler;
            if ("file".equalsIgnoreCase(destination)) {
                int log_file_size = Integer.parseInt(props.getProperty("log.file.size"));
                String log_file_name = props.getProperty("log.file.name");
                String user_dir = System.getProperty("user.dir");
                String log_filepath_pattern = String.format("%s/%s", user_dir, log_file_name);
                handler = new DailyRollingFileHandler(log_filepath_pattern, log_file_size);
            } else {
                handler = new ConsoleHandler();
            }
            handler.setLevel(Level.ALL);

            audit.setUseParentHandlers(false);
            audit.setLevel(level);
            audit.addHandler(handler);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }
}
