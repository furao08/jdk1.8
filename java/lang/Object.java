package java.lang;

/**
 * @author  unascribed
 */
public class Object {

    /**
     * native表示这是一个本地方法（即：由c或c++实现的方法）
     * java会调用两种方法，一是java方法，另一种就是本地方法
     *
     * java要调用本地方法需要的步骤：
     *                         1.JVM将本地方法文件加载到内存
     *                         2.本地方法链接到调用处
     *
     * registerNatives方法的作用就是步骤二，将本地方法链接到调用处，这样下面的native修饰的方法就可以调用到本地方法
     */
    private static native void registerNatives();

    static {
        registerNatives();
    }

    /**
     * 本地方法，返回类或对象的 Class 对象
     */
    public final native Class<?> getClass();

    /**
     * 本地方法，返回每个对象的 hash 值（object.hashCode()默认返回随机数）
     */
    public native int hashCode();

    /**
     * 比较方法，通过 == 进行对象地址的比较
     * 一般情况下，类会重写这个方法，将 地址比较 ==> 值比较
     */
    public boolean equals(Object obj) {
        return (this == obj);
    }

    /**
     * clone方法是用来复制一个对象，不同于“=”
     * 对于值类型的数据是可以通过“=”来实现复制的。但是对于引用类型的对象，“=”只能复制其内存地址，使对象的引用指向同一个对象，而不会创建新的对象
     * 例如：
     *   1.Car c1 = new Car();
     *     Car c2 = c1;
     *     栈中变量c1、c2指向堆中同一个对象
     *   2.int num = 1;
     *     int sum = num;
     *     栈中会创建一个值类型变量sum，将num的值复制给sum，此时栈中有两个变量，num和sum，都存储了值：1
     *   3.Car c1 = new Car();
     *     Car c2 = c1.clone();
     *     c1调用clone()方法复制对象，栈中变量c2指向这个新创建的对象，c1、c2指向不同的对象
     */
    protected native Object clone() throws CloneNotSupportedException;

    /**
     * GC在回收垃圾时会调用这个方法，看起来就像是一个对象死亡之前的回调
     * 只有在内存不足时，JVM才会进行GC，所以对象不一定总会被回收（如，正常情况下程序的结束，可能就不会执行这个方法）
     */
    protected void finalize() throws Throwable { }

    /**
     * 本地方法，唤醒等待该锁的其中一个线程
     */
    public final native void notify();

    /**
     * 本地方法，唤醒等待该锁的所有线程
     */
    public final native void notifyAll();

    /**
     * 超时等待一段时间，这里的参数是毫秒，也就是等待长达n毫秒，如果没有通知就超时返回
     */
    public final native void wait(long timeout) throws InterruptedException;

    /**
     * 对于超时时间更细粒度的控制，可以达到毫秒
     */
    public final void wait(long timeout, int nanos) throws InterruptedException {
        if (timeout < 0) {
            throw new IllegalArgumentException("timeout value is negative");
        }
        if (nanos < 0 || nanos > 999999) {
            throw new IllegalArgumentException(
                                "nanosecond timeout value out of range");
        }
        if (nanos > 0) {
            timeout++;
        }
        wait(timeout);
    }

    /**
     * 调用该方法的线程进入WAITING状态，只有等待另外线程的通知或被中断才会返回，需要注意，
     * 调用wait()方法后，会释放对象的锁
     */
    public final void wait() throws InterruptedException {
        wait(0);
    }

    /**
     * 输出对象的字符串形式，一般会被重写
     */
    public String toString() {
        return getClass().getName() + "@" + Integer.toHexString(hashCode());
    }

}
