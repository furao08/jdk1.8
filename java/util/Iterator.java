package java.util;

import java.util.function.Consumer;

/**
 * 有些集合有下标，有些集合没有下标，为了方便遍历集合，
 * java引入迭代器模式，为所有的集合提供一种通用的遍历方式
 *
 * 迭代器一般使用顺序：是否有下一个元素 -> 获取下一个元素 -> 删除元素
 */
public interface Iterator<E> {
    /**
     * 是否有下一个元素
     */
    boolean hasNext();

    /**
     * 获取下一个元素
     */
    E next();

    /**
     * 删除元素
     * remove() 方法只能删除游标指向的前一个元素，而且一个 next() 函数之后，
     * 只能跟着最多一个 remove() 操作，多次调用 remove() 操作会报错 -- 由实现类去实现
     */
    default void remove() {
        throw new UnsupportedOperationException("remove");
    }

    /**
     * 为剩下的元素执行操作
     *
     * @param action 为每个元素要执行的操作
     * @throws NullPointerException action不能为null
     * @since 1.8
     */
    default void forEachRemaining(Consumer<? super E> action) {
        Objects.requireNonNull(action);
        while (hasNext()) {
            action.accept(next());
        }
    }
}
