package be.tr.democracy.inmem;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public class LocalFileCache<T> implements Supplier<List<T>> {
    private final static Logger LOGGER = LoggerFactory.getLogger(LocalFileCache.class);
    private final File cacheFile;
    private final Supplier<List<T>> source;
    private final ObjectMapper objectMapper;
    private final CollectionType javaType;

    public LocalFileCache(Supplier<List<T>> source, File cacheDir, String cacheFileName, Class<T> type) {
        requireNonNull(source);
        requireNonNull(cacheDir);
        requireNonNull(cacheFileName);
        requireNonNull(type);
        if (cacheFileName.isBlank()) {
            throw new IllegalArgumentException("The cache file name must not be empty");
        }
        this.cacheFile = createCacheFile(cacheDir, cacheFileName, source);
        this.source = source;
        this.objectMapper = new ObjectMapper();
        this.javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, type);
    }

    @Override
    public List<T> get() {
        if (cacheFile == null || !cacheFile.exists()) {
            return source.get();
        } else {
            return loadFromCacheFile();
        }
    }

    public void clear() {
        if (cacheFile != null) {
            cacheFile.delete();
        }
    }

    private static synchronized <T> File createCacheFile(File possibleCacheDir, String cacheFileName, Supplier<T> source) {
        final File actualCacheDir = ensureCacheDir(possibleCacheDir);
        if (actualCacheDir == null || !actualCacheDir.exists()) {
            return null;
        }
        final var cacheFile = new File(actualCacheDir, cacheFileName);
        if (cacheFile.exists()) {
            LOGGER.info("reusing existing cacheFile {}", cacheFile.getAbsolutePath());
            return cacheFile;
        }
        return createCacheFile(source, cacheFile);


    }

    private static <T> void writeData(File cacheFile, T data) throws IOException {
        final var objectMapper = new ObjectMapper();
        objectMapper.writeValue(cacheFile, data);
    }

    private static <T> File createCacheFile(Supplier<T> source, File cacheFile) {
        LOGGER.info("No CacheFile exists");
        try {
            final var newFile = cacheFile.createNewFile();
            writeData(cacheFile, source.get());
            LOGGER.info("Created cacheFile {}", cacheFile.getAbsolutePath());
            return cacheFile;

        } catch (IOException e) {
            LOGGER.error("Unable to create cache file", e);
            return null;
        }
    }

    private static File ensureDirectory(File cacheDir) {
        if (cacheDir.isFile()) {
            LOGGER.warn("Cache directory is not a directory: {}", cacheDir.getAbsolutePath());
            return null;
        } else {
            return cacheDir;
        }
    }

    private static File createCacheDir(File cacheDir) {
        final var dirCreated = cacheDir.mkdirs();
        if (dirCreated) {
            LOGGER.info("Create cache directory : {}", cacheDir.getAbsolutePath());
            return cacheDir;
        } else {
            LOGGER.warn("Cache directory did not exist and could not be created: {}", cacheDir.getAbsolutePath());
            return null;
        }
    }

    private static synchronized File ensureCacheDir(File cacheDir) {
        if (cacheDir.exists()) {
            return ensureDirectory(cacheDir);
        } else {
            return createCacheDir(cacheDir);
        }
    }

    private List<T> loadFromCacheFile() {
        try {
            return objectMapper.readValue(cacheFile, javaType);
        } catch (Throwable e) {
            LOGGER.error("Unable to load {} from [{}]", javaType.getTypeName(), cacheFile.getAbsolutePath(), e);
            return List.of();
        }
    }

}
