package lsieun.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import static lsieun.utils.LogUtils.audit;
import static lsieun.utils.LogUtils.err;

public class FileUtils {

    public static void makeFileDir(final String filepath) {
        File f = new File(filepath);
        File dirFile = f.getParentFile();
        if (dirFile.exists()) return;
        boolean flag = dirFile.mkdirs();
        if (!flag) {
            audit.warning("Cant not create dir: " + dirFile);
        }
    }

    public static void writeFile(final byte[] bytes, final String filepath) throws IOException {
        makeFileDir(filepath);
        try (
                FileOutputStream fout = new FileOutputStream(filepath);
                BufferedOutputStream out = new BufferedOutputStream(fout);
        ) {
            out.write(bytes);
            out.flush();
            audit.info("file://" + filepath);
        }
    }

    public static byte[] readFile(final String filepath) throws IOException {
        File file = new File(filepath);
        if (!file.exists()) return null;

        try (
                FileInputStream fin = new FileInputStream(file);
                BufferedInputStream in = new BufferedInputStream(fin)
        ) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buff = new byte[Const.BUFFER_SIZE];
            for (int len = in.read(buff); len != -1; len = in.read(buff)) {
                out.write(buff, 0, len);
            }
            return out.toByteArray();
        }
    }

    public static void writeHtml(final String html, final String filepath) throws IOException {
        byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
        writeFile(bytes, filepath);
    }

    public static String readHtml(final String filepath) throws IOException {
        byte[] bytes = readFile(filepath);
        return ByteUtils.toStr(bytes);
    }

    public static List<String> readLines(final String filepath) {
        List<String> lines = new ArrayList<>();
        File file = new File(filepath);
        if (!file.exists() || !file.isFile()) {
            audit.warning("File Not Exist: " + filepath);
            return lines;
        }

        try (
                FileInputStream in = new FileInputStream(filepath);
                InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(reader);
        ) {
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                lines.add(line);
            }
        } catch (IOException ex) {
            err.log(Level.SEVERE, "unexpected error: " + ex.getMessage(), ex);
        }
        return lines;
    }

    public static void writeLines(final List<String> lines, final String filepath) {
        makeFileDir(filepath);
        try (
                FileOutputStream out = new FileOutputStream(filepath);
                OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
                BufferedWriter bw = new BufferedWriter(writer);
        ) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }

        } catch (IOException ex) {
            err.log(Level.SEVERE, "unexpected error: " + ex.getMessage(), ex);
        }
    }

    public static void negate(String from_path, String to_path) {
        try (
                FileInputStream fin = new FileInputStream(from_path);
                BufferedInputStream in = new BufferedInputStream(fin);
                FileOutputStream fout = new FileOutputStream(to_path);
                BufferedOutputStream out = new BufferedOutputStream(fout);
        ) {
            for (int b = in.read(); b != -1; b = in.read()) {
                int value = (~b) & 0xFF;
                out.write(value);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
