package com.demo.java_utilities;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

public class IOStreamUtils {

    public static BufferedReader getReaderFromInputStream(InputStream is) {
        return new BufferedReader(new InputStreamReader(is, UTF_8));
    }

    public static List<String> getLinesFromInputStream(InputStream is)
        throws IOException {
        try (BufferedReader br = getReaderFromInputStream(is)) {
            return br.lines().collect(Collectors.toList());
        }
    }

    public static String getStringFromInputStream(InputStream is)
        throws IOException {
        try (ByteArrayOutputStream result = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[8192];
            int length;
            while ((length = is.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }

            return result.toString("UTF-8");
        } finally {
            is.close();
        }
    }
}
