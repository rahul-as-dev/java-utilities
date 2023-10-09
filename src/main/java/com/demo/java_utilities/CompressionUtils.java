package com.demo.java_utilities;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.rauschig.jarchivelib.ArchiveEntry;
import org.rauschig.jarchivelib.ArchiveStream;
import org.rauschig.jarchivelib.Archiver;
import org.rauschig.jarchivelib.ArchiverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Utility class to handle and work with file compression
public class CompressionUtils {

    private static final Logger log = LoggerFactory.getLogger(
        CompressionUtils.class
    );

    public static File extract(File from, File to, boolean deleteArchive)
        throws IOException {
        Archiver archiver = ArchiverFactory.createArchiver(from);
        log.debug("Extracting archive {} to {}", from, to);
        archiver.extract(from, to);
        ArchiveStream archiveStream = archiver.stream(from);
        ArchiveEntry entry;
        String firstEntryName = null;
        while (
            (entry = archiveStream.getNextEntry()) != null &&
            firstEntryName == null
        ) {
            if (entry.isDirectory()) {
                firstEntryName = entry.getName();
            }
        }
        File firstEntryFile = new File(
            to.getAbsolutePath() + File.separator + firstEntryName
        );
        if (deleteArchive) {
            log.debug("Deleting archive file {}", from);
            if (!from.delete()) throw new IOException(
                "Could not delete the archive at " + from.getAbsolutePath()
            );
        }
        return firstEntryFile;
    }

    public static Iterator<
        Pair<ArchiveEntry, InputStream>
    > getArchiveEntryInputStreams(File archive) throws IOException {
        final Archiver archiver = ArchiverFactory.createArchiver(archive);
        final ArchiveStream stream = archiver.stream(archive);
        return new Iterator<Pair<ArchiveEntry, InputStream>>() {
            boolean exhausted = false;
            private ArchiveEntry currentEntry;

            @Override
            public boolean hasNext() {
                if (currentEntry == null && !exhausted) {
                    try {
                        currentEntry = stream.getNextEntry();
                        while (
                            currentEntry != null && currentEntry.isDirectory()
                        ) currentEntry = stream.getNextEntry();
                        if (currentEntry == null) exhausted = true;
                    } catch (IOException e) {
                        log.error("Could not get next archive entry", e);
                    }
                }
                return !exhausted;
            }

            @Override
            public Pair<ArchiveEntry, InputStream> next() {
                if (hasNext()) {
                    Pair<ArchiveEntry, InputStream> ret = new ImmutablePair<>(
                        currentEntry,
                        new LimitedInputStream(currentEntry.getSize(), stream)
                    );
                    currentEntry = null;
                    return ret;
                }

                return null;
            }
        };
    }

    private static class LimitedInputStream extends InputStream {

        private final InputStream is;
        private long limit;

        public LimitedInputStream(long limit, InputStream is) {
            this.limit = limit;
            this.is = is;
        }

        @Override
        public int read() throws IOException {
            int data = -1;
            if (limit > 0) data = is.read();
            if (data >= 0) --limit;
            return data;
        }
    }
}
