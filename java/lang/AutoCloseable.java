package java.lang;

/**
 * 1.7之前，try_catch_finally，在finally中释放资源，但是会经常忘记，且代码冗余
 * 1.7之后，可以通过实现接口AutoCloseable中的close()方法，在资源使用之后会自动释放（只有在try中声明时）或手动释放
 * 它的出现是为了更好的管理资源，准确说是资源的释放
 *
 * try_catch_finally，无论是否报错，都会执行finally，也就是保证了资源一定会释放
 * AutoCloseable.close()方法也有这个属性，无论是否报错，一定会在资源使用结束后执行
 *
 * 使用：
 *    1.类（资源）实现close()
 *    2.在try()中声明变量，如：try(A a = new A()){}
 *
 * 带资源的try语法：
 *    try(Resource rs = new Resource())
 *       * rs是AutoCloseable类型
 *       * rs不能重新赋值，final
 *    在try代码块执行结束后，会自动调用rs.close()方法
 *    用;分隔，可在try中声明多个资源
 *    finally在任何情况下都会执行
 *
 * @since 1.7
 */
public interface AutoCloseable {
    /**
     * 释放资源
     * @throws Exception
     */
    void close() throws Exception;
}
