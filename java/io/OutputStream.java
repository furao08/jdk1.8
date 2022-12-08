package java.io;

/**
 * This abstract class is the superclass of all classes representing
 * an output stream of bytes. An output stream accepts output bytes
 * and sends them to some sink.
 * <p>
 * Applications that need to define a subclass of
 * <code>OutputStream</code> must always provide at least a method
 * that writes one byte of output.
 *
 * @author  Arthur van Hoff
 * @see     java.io.BufferedOutputStream
 * @see     java.io.ByteArrayOutputStream
 * @see     java.io.DataOutputStream
 * @see     java.io.FilterOutputStream
 * @see     java.io.InputStream
 * @see     java.io.OutputStream#write(int)
 * @since   JDK1.0
 */
public abstract class OutputStream implements Closeable, Flushable {
    /**
     * Writes the specified byte to this output stream. The general
     * contract for <code>write</code> is that one byte is written
     * to the output stream. The byte to be written is the eight
     * low-order bits of the argument <code>b</code>. The 24
     * high-order bits of <code>b</code> are ignored.
     * <p>
     * Subclasses of <code>OutputStream</code> must provide an
     * implementation for this method.
     *
     * @param      b   the <code>byte</code>.
     * @exception  IOException  if an I/O error occurs. In particular,
     *             an <code>IOException</code> may be thrown if the
     *             output stream has been closed.
     */
    public abstract void write(int b) throws IOException;

    /**
     * 
     */
    public void write(byte b[]) throws IOException {
        write(b, 0, b.length);
    }

    /**
     *
     */
    public void write(byte b[], int off, int len) throws IOException {
        if (b == null) {
            throw new NullPointerException();
        } else if ((off < 0) || (off > b.length) || (len < 0) ||
                   ((off + len) > b.length) || ((off + len) < 0)) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return;
        }
        for (int i = 0 ; i < len ; i++) {
            write(b[off + i]);
        }
    }

    /**
     * 刷新缓存，立即写入目的地
     * 有缓存区的输出流会重写这个方法，例如：BufferedOutputStream
     * 没有缓冲区的输出流不重写这个方法，例如：FileOutputStream
     *
     * write()之后flush()，将缓存区的数据刷新到目的地，
     * 由于flush()的调用者是OutputStream，它只能保证将输出字节流从缓存区传给操作系统，
     * 不能保证操作系统一定会立即将数据写入磁盘
     *
     * 这个时候，可以调用FileDecriptor.sync()，
     * sync() ：强制所有系统缓冲区与基础设备同步，
     * 因为fd可以等同于文件系统中真实的文件，可以保证缓存区和基础设备（例如：磁盘）的一致
     *
     * 区别：
     * OutputStream.flush() 刷新缓存，缓存区数据立即被送出，不保证数据一定会写入文件
     * FileDecriptor.sync() 强制缓存区和设备的同步，保证缓存数据一定写入文件
     */
    public void flush() throws IOException {}

    @Override
    public void close() throws IOException {}

}
