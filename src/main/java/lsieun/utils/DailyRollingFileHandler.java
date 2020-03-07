package lsieun.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ErrorManager;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;

public class DailyRollingFileHandler extends StreamHandler {
    public static final String DEFAULT_LOG_FILENAME_PATTERN = "audit_%d_%u.log";
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private final long log_file_size;
    private String pattern;
    private String current_filename;
    private File file;
    private FileOutputStream fos;

    public DailyRollingFileHandler(String pattern, long log_file_size) throws IllegalArgumentException, IOException {
        if (log_file_size < 1000000) {
            this.log_file_size = 1000000;
        } else {
            this.log_file_size = log_file_size;
        }

        if (pattern == null) {
            pattern = DEFAULT_LOG_FILENAME_PATTERN;
        }

        if (pattern.length() < 1) {
            throw new IllegalArgumentException("Pattern length is less than 1");
        }

        if (!pattern.contains("%d")) {
            pattern += "%d";
        }

        if (!pattern.contains("%u")) {
            pattern += "%u";
        }

        this.pattern = pattern;
        this.current_filename = generateFilename(new Date());
        this.file = new File(current_filename);
        this.fos = new FileOutputStream(file, true);
        super.setOutputStream(fos);
    }

    private synchronized String generateFilename(Date date) {
        String dateStr = dateFormat.format(date);
        String prePattern = pattern.replaceAll("%d", dateStr);

        int unique = 0;
        String filename = prePattern.replaceAll("%u", String.valueOf(unique));

        // Moving to a new date. Maybe we should find a new unique number
        File logFile = new File(filename);
        while (logFile.exists() && logFile.length() > log_file_size) {
            unique++;
            filename = prePattern.replaceAll("%u", String.valueOf(unique));
            logFile = new File(filename);
        }

        return filename;
    }

    @Override
    public synchronized void publish(LogRecord record) {
        if (!isLoggable(record)) {
            return;
        }

        String filename = generateFilename(new Date(record.getMillis()));

        // Change the log file
        if (!current_filename.equals(filename)) {
            file = new File(filename);
            try {
                fos = new FileOutputStream(file, true);
            } catch (IOException ex) {
                super.reportError(null, ex, ErrorManager.GENERIC_FAILURE);
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                super.reportError(null, ex, ErrorManager.GENERIC_FAILURE);
            }
            current_filename = filename;
            super.setOutputStream(fos);
        }

        super.publish(record);
        super.flush();
    }
}
