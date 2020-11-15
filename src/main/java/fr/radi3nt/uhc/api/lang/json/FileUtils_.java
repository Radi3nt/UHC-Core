package fr.radi3nt.uhc.api.lang.json;

import fr.radi3nt.uhc.api.lang.Logger;
import fr.radi3nt.uhc.uhc.UHCCore;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class FileUtils_ {
    public static void createFile(final File file) throws IOException {
        if (!file.exists()) {
            if (file.getParentFile().mkdirs()) {
                Logger.getGeneralLogger().log(UHCCore.getPrefix() + file.getName());
            }
            if (file.createNewFile()) {
                Logger.getGeneralLogger().log(UHCCore.getPrefix() + " Create " + file.getName());
            }
        }
    }

    public static void save(final File file, final String text) {
        try {
            createFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (final FileWriter fw = new FileWriter(file)) {
            fw.write(text);
            fw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveJson(final File file, final JsonObject jsonObject) {
        try {
            createFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (final Writer fw = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
            jsonObject.writeTo(fw, WriterConfig.PRETTY_PRINT);
            fw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void copy(final InputStream source, final String destination) {
        Logger.getGeneralLogger().log(UHCCore.getPrefix() + " Copying ->" + source + "\n\tto ->" + destination);
        final File file = new File(destination);
        try {
            createFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (source != null) {
            try (final OutputStream out = new FileOutputStream(file)) {
                final byte[] buf = new byte[1024];
                int len;
                while ((len = source.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    source.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } finally {
                try {
                    source.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    public static String convert(final InputStream inputStream) {
        try (final BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return br.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String loadContent(final File file) {
        if (file.exists()) {
            try (final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
                final StringBuilder text = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    text.append(line);
                }
                return text.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }
}