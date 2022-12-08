package java.io;

/**
 * 释放资源
 * 等同于接口AutoCloseable
 * @since 1.5
 */
public interface Closeable extends AutoCloseable {
    @Override
    void close() throws IOException;
}
