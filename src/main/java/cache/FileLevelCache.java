package cache;

import cache.interfaces.Cache;
import lombok.Cleanup;
import lombok.Getter;
import lombok.extern.log4j.Log4j;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Log4j
public class FileLevelCache<K, V> implements Cache<K, V> {

    private Map<K, String> storage;
    private Integer maxSize;
    private Path dir;

    public FileLevelCache(Integer maxSize) {
        this.maxSize = maxSize;
        this.storage = new ConcurrentHashMap<>();
        initCache();

    }

    void initCache() {
        try {
            dir = Files.createDirectory(new File(System.getProperty("user.dir") + "/cache" + System.nanoTime()).toPath());
        } catch (IOException e) {
            log.error("Don't created directory for file level cache: " + e.getMessage(), e);
        }
    }

    @Override
    public void put(K key, V value) {


        try {
            @Cleanup ObjectOutputStream outputStream = null;
            File file = Files.createTempFile(dir, "", "").toFile();
            outputStream = new ObjectOutputStream(new FileOutputStream(file));
            outputStream.writeObject(value);
            outputStream.flush();
            storage.put(key, file.getName());
        } catch (IOException e) {
            log.error("Don't created file for file level cache: " + e.getMessage(), e);
        }

    }

    @Override
    public void remove(K key) {
        File file = new File(dir + File.separator + storage.get(key));
        storage.remove(key);
        log.info((file.delete() ? "Success remove file: " : "Error remove file: ") + file.getName());
    }

    @Override
    public V get(K key) {
        if (!containsKey(key)) return null;

        String fileName = storage.get(key);

        try {
            @Cleanup ObjectInputStream objectInputStream = null;
            objectInputStream = new ObjectInputStream(new FileInputStream(dir + File.separator + fileName));
            return (V) objectInputStream.readObject();
        } catch (FileNotFoundException e) {
            log.error("File not found: " + fileName, e);
        } catch (IOException | ClassNotFoundException e) {
            log.error("Error read file: "+fileName, e);
        }
        return null;
    }

    @Override
    public boolean containsKey(K key) {
        return storage.containsKey(key);
    }

    @Override
    public boolean containsValue(V value) {
        return storage.containsValue(value);
    }

    @Override
    public void clear() {
        storage.forEach((k,v)->{
            remove(k);
        });
    }

    @Override
    public boolean isEmpty() {
        return storage.isEmpty();
    }

    @Override
    public Integer size() {
        return storage.size();
    }
}
