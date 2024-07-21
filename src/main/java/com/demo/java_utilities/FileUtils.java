package com.demo.java_utilities;

import com.demo.java_utilities.URIUtils;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Utility class for file related static methods
public class FileUtils {

    private static final Logger log = LoggerFactory.getLogger(FileUtils.class);

    public static BufferedInputStream getInputStreamFromFile(File file)
        throws IOException {
        try {
            InputStream is = new FileInputStream(file);
            String lcfn = file.getName().toLowerCase();
            if (lcfn.contains(".gz") || lcfn.contains(".gzip")) is =
                new GZIPInputStream(is);
            return new BufferedInputStream(is);
        } catch (Exception | Error e) {
            throw e;
        }
    }

    public static BufferedOutputStream getOutputStreamToFile(
        File file,
        boolean append
    ) throws IOException {
        OutputStream os = new FileOutputStream(file, append);
        String lcfn = file.getName().toLowerCase();
        if (lcfn.contains(".gz") || lcfn.contains(".gzip")) os =
            new GZIPOutputStream(os);
        return new BufferedOutputStream(os);
    }

    public static BufferedOutputStream getOutputStreamToFile(File file)
        throws IOException {
        return getOutputStreamToFile(file, false);
    }

    public static BufferedReader getReaderFromFile(File file)
        throws IOException {
        return new BufferedReader(
            new InputStreamReader(
                getInputStreamFromFile(file),
                StandardCharsets.UTF_8
            )
        );
    }

    public static BufferedWriter getWriterToFile(File file, boolean append)
        throws IOException {
        return new BufferedWriter(
            new OutputStreamWriter(
                getOutputStreamToFile(file, append),
                StandardCharsets.UTF_8
            )
        );
    }

    public static BufferedWriter getWriterToFile(File file) throws IOException {
        return new BufferedWriter(
            new OutputStreamWriter(
                getOutputStreamToFile(file, false),
                StandardCharsets.UTF_8
            )
        );
    }

    public static void createJarFile(File outputFile, File... files)
        throws IOException {
        Manifest manifest = new Manifest();
        manifest
            .getMainAttributes()
            .put(Attributes.Name.MANIFEST_VERSION, "1.0");
        try (
            JarOutputStream target = new JarOutputStream(
                new FileOutputStream(outputFile),
                manifest
            )
        ) {
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                addFileToJarOutputStream(file, new StringBuilder(), target);
            }
        }
    }

    public static void addFileToJarOutputStream(
        File source,
        StringBuilder rootPath,
        JarOutputStream target
    ) throws IOException {
        BufferedInputStream in = null;
        try {
            String name = rootPath.length() > 0
                ? rootPath + source.getName()
                : source.getName();
            if (source.isDirectory()) {
                if (!name.isEmpty()) {
                    if (!name.endsWith("/")) name += "/";
                    JarEntry entry = new JarEntry(name);
                    entry.setTime(source.lastModified());
                    target.putNextEntry(entry);
                    target.closeEntry();
                }
                rootPath.append(name);
                for (File nestedFile : source.listFiles()) addFileToJarOutputStream(
                    nestedFile,
                    rootPath,
                    target
                );
                return;
            }

            JarEntry entry = new JarEntry(name);
            entry.setTime(source.lastModified());
            target.putNextEntry(entry);
            in = new BufferedInputStream(new FileInputStream(source));

            byte[] buffer = new byte[1024];
            while (true) {
                int count = in.read(buffer);
                if (count == -1) break;
                target.write(buffer, 0, count);
            }
            target.closeEntry();
        } finally {
            if (in != null) in.close();
        }
    }

    public static InputStream findResource(String name) throws IOException {
        InputStream is = null;
        if (is == null) {
            log.trace("Trying to find resource '{}' as a file", name);
            File file = new File(name);
            if (file.exists()) {
                log.trace("Found file '{}'", file);
                is = getInputStreamFromFile(file);
            }
        }
        if (is == null) {
            log.trace(
                "No file at path '{}' was found. Trying to parse as an URI.",
                new File(name).getAbsolutePath()
            );
            try {
                URI uri = new URI(name);
                // If the URI is not absolute, the conversion to an URL for input stream opening will fail
                if (uri.isAbsolute()) {
                    is = URIUtils.getInputStreamFromUri(uri);
                    if (log.isDebugEnabled() && is != null) log.debug(
                        "Found resources at URI '{}'",
                        uri.toString()
                    );
                }
            } catch (URISyntaxException e) {
                // nothing, obviously was not a valid URI
            }
        }
        if (is == null) {
            log.trace(
                "Did not find a resource at file or URI '{}', trying as resource on the classpath.",
                name
            );
            is = FileUtils.class.getResourceAsStream(
                    name.startsWith("/") ? name : "/" + name
                );
            if (log.isTraceEnabled() && is != null) log.trace(
                "Found classpath resource at '{}'",
                name
            );
            if (
                is != null &&
                (name.toLowerCase().contains(".gz") ||
                    name.toLowerCase().contains(".gzip"))
            ) {
                log.trace(
                    "Classpath resource '{}' ending indicates a GZIP resource, ungzipping is added",
                    name
                );
                is = new GZIPInputStream(is);
            }
        }
        if (log.isTraceEnabled() && is == null) log.trace(
            "The resource '{}' could not be found as a file, URI or on the classpath. Returning null.",
            name
        );
        return is;
    }
}
