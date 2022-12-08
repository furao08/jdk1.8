package java.io;

import java.nio.channels.FileChannel;
import sun.nio.ch.FileChannelImpl;

/**
 * 文件字节输入流 - 以字节的形式读取图片视频文件等
 * FileInputStream is meant for（为...而准备） reading streams of raw（原始的、未加工的） bytes such as image data.
 * For reading streams of characters, consider using FileReader.
 * （FileInputStream - 字节流 ；FileReader - 字符流）
 *
 *  三个构造函数：FileInputStream(String name) 实际上就是调用 FileInputStream(File file)
 *             FileInputStream(FileDescriptor fdObj) 相较于 FileInputStream(File file) 就是没初始化path，不推荐使用
 *
 *  FileInputStream(String name)创建文件字节输入流的过程 ：
 *            1.根据文件路径name创建File对象，并校验是否创建成功
 *            2.创建文件描述符
 *            3.用name初始化path
 *            4.name作为入参调用本地方法open()打开文件
 *
 * @author  Arthur van Hoff
 * @see     java.io.File
 * @see     java.io.FileDescriptor
 * @see     java.io.FileOutputStream
 * @see     java.nio.file.Files
 * @since   JDK1.0
 */
public class FileInputStream extends InputStream
{
    /* File Descriptor - handle to the open file */
    /* 文件描述符，内核利用文件描述符来访问文件系统中真实的文件，也就是说，要想读写操作文件，必须要有fd */
    private final FileDescriptor fd;

    /**
     * 文件路径
     * The path of the referenced file
     * (null if the stream is created with a file descriptor)
     * 如果采用fd实例化的构造函数，那么path=null
     */
    private final String path;
    /**
     * 读、写、操作文件的通道
     */
    private FileChannel channel = null;
    /**
     * 锁，给close()加锁
     */
    private final Object closeLock = new Object();
    /**
     * 锁的条件值
     */
    private volatile boolean closed = false;

    /**
     * name : 文件路径
     * 通过文件路径创建File对象，传给FileInputStream(File file)
     */
    public FileInputStream(String name) throws FileNotFoundException {
        // this(File file) 指向构造方法，表达式返回File对象
        this(name != null ? new File(name) : null);
    }

    /**
     * 初始化fd、path，并调用open()打开文件
     * 入参File的作用：1.file.isInvalid()检查文件是否有效
     *              2.文件路径作为入参调用open()，打开文件
     */
    public FileInputStream(File file) throws FileNotFoundException {
        String name = (file != null ? file.getPath() : null);
        // 当运行未知的Java程序的时候，该程序可能有恶意代码（删除系统文件、重启系统等），
        // 为了防止运行恶意代码对系统产生影响，需要对运行的代码的权限进行控制，这时候就要启用Java安全管理器（SecurityManager）
        SecurityManager security = System.getSecurityManager();
        if (security != null) {
            // 检查程序对路径是否有读权限
            security.checkRead(name);
        }
        if (name == null) {
            throw new NullPointerException();
        }
        if (file.isInvalid()) {
            throw new FileNotFoundException("Invalid file path");
        }
        // 创建文件字节输入流时，创建文件描述符，并追踪为需要释放资源，为close()方法作铺垫
        fd = new FileDescriptor();
        fd.attach(this);
        path = name;
        open(name);
    }

    /**
     * 不建议使用
     * 初始化fd，path=null
     */
    public FileInputStream(FileDescriptor fdObj) {
        SecurityManager security = System.getSecurityManager();
        if (fdObj == null) {
            throw new NullPointerException();
        }
        if (security != null) {
            security.checkRead(fdObj);
        }
        fd = fdObj;
        path = null;

        /*
         * FileDescriptor is being shared by streams.
         * Register this stream with FileDescriptor tracker.
         */
        fd.attach(this);
    }

    /**
     * 本地方法，打开文件
     * 私有方法，在初始化FileInputStream时，被构造函数调用
     */
    private void open(String name) throws FileNotFoundException {
        open0(name);
    }

    private native void open0(String name) throws FileNotFoundException;

    /**
     * 实现了InputStream中的抽象方法read()，本地方法（native）实现
     *
     * Reads a byte of data from this input stream.
     * 从输入流中读取一个字节
     * This method blocks if no input is yet available.
     *
     * @return     the next byte of data, or <code>-1</code> if the end of the file is reached.
     *             返回该字节对应的ascii码，读到文件末尾时返回-1
     * @exception  IOException  if an I/O error occurs.
     */
    public int read() throws IOException {
        return read0();
    }
    private native int read0() throws IOException;

    /**
     * 读入缓存
     *
     * Reads up to <code>b.length</code> bytes of data from this input stream into an array of bytes.
     * This method blocks until some input is available.
     *
     * @param      b   the buffer into which the data is read.
     *             byte[] b : 缓存对象
     * @return     the total number of bytes read into the buffer
     *             返回缓存实际字节大小
     *             or <code>-1</code> if there is no more data because the end of the file has been reached.
     * @exception  IOException  if an I/O error occurs.
     */
    public int read(byte b[]) throws IOException {
        return readBytes(b, 0, b.length);
    }
    private native int readBytes(byte b[], int off, int len) throws IOException;

    /**
     * 读入缓存
     */
    public int read(byte b[], int off, int len) throws IOException {
        return readBytes(b, off, len);
    }

    /**
     * 重写InputStream的skip()方法，改用本地方法skip0()
     */
    public long skip(long n) throws IOException {
        return skip0(n);
    }

    private native long skip0(long n) throws IOException;

    /**
     * 输入流中还有多少可以读取（或跳过）的字节数
     */
    public int available() throws IOException {
        return available0();
    }

    private native int available0() throws IOException;

    /**
     *
     */
    public void close() throws IOException {
        synchronized (closeLock) {
            if (closed) {
                return;
            }
            closed = true;
        }
        if (channel != null) {
           channel.close();
        }
        // 调用fd.closeAll() 释放之前追踪的资源
        fd.closeAll(new Closeable() {
            public void close() throws IOException {
               close0();
           }
        });
    }

    private native void close0() throws IOException;

    /**
     * Returns the <code>FileDescriptor</code>
     * object  that represents the connection to
     * the actual file in the file system being
     * used by this <code>FileInputStream</code>.
     *
     * @return     the file descriptor object associated with this stream.
     * @exception  IOException  if an I/O error occurs.
     * @see        java.io.FileDescriptor
     */
    public final FileDescriptor getFD() throws IOException {
        if (fd != null) {
            return fd;
        }
        throw new IOException();
    }

    /**
     * Returns the unique {@link java.nio.channels.FileChannel FileChannel}
     * object associated with this file input stream.
     *
     * <p> The initial {@link java.nio.channels.FileChannel#position()
     * position} of the returned channel will be equal to the
     * number of bytes read from the file so far.  Reading bytes from this
     * stream will increment the channel's position.  Changing the channel's
     * position, either explicitly or by reading, will change this stream's
     * file position.
     *
     * @return  the file channel associated with this file input stream
     */
    public FileChannel getChannel() {
        synchronized (this) {
            if (channel == null) {
                channel = FileChannelImpl.open(fd, path, true, false, this);
            }
            return channel;
        }
    }

    private static native void initIDs();

    static {
        initIDs();
    }

    /**
     * 释放资源
     */
    protected void finalize() throws IOException {
        if ((fd != null) &&  (fd != FileDescriptor.in)) {
            close();
        }
    }
}
