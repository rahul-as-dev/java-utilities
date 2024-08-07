package com.demo.java_utilities;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class URIUtils {

    private static final Logger log = LoggerFactory.getLogger(URIUtils.class);

    public static BufferedInputStream getInputStreamFromUri(URI uri)
        throws IOException {
        try {
            String uriStr = uri.toString().toLowerCase();
            return new BufferedInputStream(
                uriStr.endsWith(".gz") || uriStr.endsWith(".gzip")
                    ? new GZIPInputStream(uri.toURL().openStream())
                    : uri.toURL().openStream()
            );
        } catch (ZipException e) {
            log.error(
                "URI {} target could not be uncompressed: {}",
                uri,
                e.getMessage()
            );
            throw e;
        }
    }

    public static BufferedReader getReaderFromUri(URI uri) throws IOException {
        return new BufferedReader(
            new InputStreamReader(
                getInputStreamFromUri(uri),
                StandardCharsets.UTF_8
            )
        );
    }
}
