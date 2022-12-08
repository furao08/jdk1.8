package java.io;

import java.util.ArrayList;
import java.util.List;

/**
 * fd定义了三个标准输入输出流：in、out、err，java的标准输入输出流就是这里来实现的
 * fd的值是在读写操作文件时，操作系统返回的非负整数，拿到这个值就可以操作文件系统中的文件
 *
 * FileDescriptor - 文件描述符
 * 内核（kernel）利用文件描述符（file descriptor）来访问文件。
 * 文件描述符是非负整数，打开现存文件或新建文件时，内核会返回一个文件描述符。
 * 读写文件也需要使用文件描述符来指定待读写的文件。
 *
 * 可以这么理解：文件描述符是内核和文件系统中真实的文件之间的桥梁
 *            新建、打开、读写文件时，需要创建文件描述符来连接文件系统中真实的文件
 *
 * FileDescriptor 可以被用来表示开放文件、开放套接字等。
 * 比如用 FileDescriptor 表示文件来说: 当 FileDescriptor 表示文件时，我们可以通俗的将 FileDescriptor 看成是该文件。
 * 但是，我们不能直接通过 FileDescriptor 对该文件进行操作。
 * 若需要通过 FileDescriptor 对该文件进行操作，则需要新创建 FileDescriptor 对应的 FileOutputStream或者是 FileInputStream，再对文件进行操作
 * 应用程序不应该创建他们自己的文件描述符，它是由底层返回的
 *
 * 在FileInputStream、FileOutputStream、RandomAccessFile的构造函数中创建fd，并调用attach()，
 * 表示在创建这些资源时会创建fd，并添加这些资源为需要关闭对象 parent、otherParents
 * 资源释放时会调用fd.closeAll() - 释放parent、otherParents
 */
public final class FileDescriptor {

    /**
     * 文件描述符是非负整数，打开现存文件或新建文件时，内核会返回一个文件描述符
     */
    private int fd;
    /**
     * 有一个 Closeable 对象的 parent，表示用来关闭单个资源
     */
    private Closeable parent;
    /**
     * 需要关闭对象的集合
     */
    private List<Closeable> otherParents;
    /**
     * 用来判断资源是否已经关闭
     */
    private boolean closed;

    /**
     * fd是由底层返回的非负整数，不应自己创建
     * 而且它的构造函数返回-1，不符合非负整数，所以new fd也没有意义
     */
    public FileDescriptor() {
        fd = -1;
    }

    /**
     * 私有的有参构造函数
     * 是提供给下面的静态属性，创建标准输入输出流
     */
    private FileDescriptor(int fd) {
        this.fd = fd;
    }

    /**
     * A handle to the standard input stream.
     * Usually, this file descriptor is not used directly,
     * but rather via the input stream known as <code>System.in</code>.
     *
     * 一个标准输入流的句柄。通常情况下，这个文件描述符不会直接使用，而是通过称为System.in的输入流
     */
    public static final FileDescriptor in = new FileDescriptor(0);

    /**
     * A handle to the standard output stream. Usually, this file
     * descriptor is not used directly, but rather via the output stream
     * known as <code>System.out</code>.
     *
     * 一个标准的输出流句柄。通过 System.out 来使用
     */
    public static final FileDescriptor out = new FileDescriptor(1);

    /**
     * A handle to the standard error stream. Usually, this file
     * descriptor is not used directly, but rather via the output stream
     * known as <code>System.err</code>.
     *
     * 一个标准的错误流句柄。通过 System.err 来使用
     */
    public static final FileDescriptor err = new FileDescriptor(2);

    /**
     * 判断是否有效，fd为非负整数时有效
     */
    public boolean valid() {
        return fd != -1;
    }

    /**
     * 它的主要用处：强制所有系统缓冲区与基础设备同步。
     * 该方法在此 FileDescriptor 的所有修改数据和属性都写入相关设备后返回。
     * 如果此 FileDescriptor 引用物理存储介质，比如文件系统中的文件，则一直要等到将与此 FileDesecriptor
     * 有关的缓冲区的所有内存中修改副本写入物理介质中，sync 方法才会返回。
     * sync 方法由要求物理存储（比例文件）处于某种已知状态下的代码使用。
     * 例如，提供简单事务处理的类可以使用 sync 来确保某个文件所有由给定事务造成的更改都记录在存储介质上。
     * sync 只影响此 FileDescriptor 的缓冲区下游。
     * 如果正通过应用程序（例如，通过一个 BufferedOutputStream 对象）实现内存缓冲，
     * 那么必须在数据受 sync 影响之前将这些缓冲区刷新，并转到 FileDescriptor 中（例如，通过调用 OutputStream.flush）。
     */
    public native void sync() throws SyncFailedException;

    /* This routine initializes JNI field offsets for the class */
    private static native void initIDs();

    static {
        initIDs();
    }

    // Set up JavaIOFileDescriptorAccess in SharedSecrets
    static {
        sun.misc.SharedSecrets.setJavaIOFileDescriptorAccess(
            new sun.misc.JavaIOFileDescriptorAccess() {
                public void set(FileDescriptor obj, int fd) {
                    obj.fd = fd;
                }

                public int get(FileDescriptor obj) {
                    return obj.fd;
                }

                public void setHandle(FileDescriptor obj, long handle) {
                    throw new UnsupportedOperationException();
                }

                public long getHandle(FileDescriptor obj) {
                    throw new UnsupportedOperationException();
                }
            }
        );
    }

    /*
     * Package private methods to track referents.
     * If multiple streams point to the same FileDescriptor, we cycle
     * through the list of all referents and call close()
     */

    /**
     * 此方法用于追踪需要关闭的对象，为parent、otherParents赋值，
     * 单个需要关闭的资源赋值给parent，多个资源放在otherParents集合中，
     *
     * 如果只有单个需要关闭的对象，那么直接调用后面的 closeAll() 方法即可，
     * 如果多个流指向同一个文件描述符，FileDescriptor 会把需要关闭的资源放在 otherParents 的集合中，
     *
     * 这个方法主要为下面的 closeAll() 方法做铺垫
     *
     * default修饰，此方法只能被本包中的FileInputStream、FileOutputStream、RandomAccessFile调用，
     * 在输入输出流等的构造函数中调用，表示在创建这些资源时，即添加为需要关闭的对象
     */
    synchronized void attach(Closeable c) {
        if (parent == null) {
            parent = c;
        } else if (otherParents == null) {
            otherParents = new ArrayList<>();
            otherParents.add(parent);
            otherParents.add(c);
        } else {
            otherParents.add(c);
        }
    }

    /**
     * default修饰，此方法只能被本包中的FileInputStream、FileOutputStream、RandomAccessFile调用，
     * 在FileInputStream、FileOutputStream、RandomAccessFile关闭资源时，会调用fd.closeAll()，
     * fd.closeAll() 会去关闭parent、otherParents
     */
    @SuppressWarnings("try")
    synchronized void closeAll(Closeable releaser) throws IOException {
        if (!closed) {
            closed = true;
            IOException ioe = null;
            try (Closeable c = releaser) {
                if (otherParents != null) {
                    for (Closeable referent : otherParents) {
                        try {
                            referent.close();
                        } catch(IOException x) {
                            if (ioe == null) {
                                ioe = x;
                            } else {
                                ioe.addSuppressed(x);
                            }
                        }
                    }
                }
            } catch(IOException ex) {
                /*
                 * If releaser close() throws IOException
                 * add other exceptions as suppressed.
                 */
                if (ioe != null)
                    ex.addSuppressed(ioe);
                ioe = ex;
            } finally {
                if (ioe != null)
                    throw ioe;
            }
        }
    }
}
