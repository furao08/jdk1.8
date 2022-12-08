package java.lang;

import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

/**
 * 可迭代
 *
 * 可以使用"foreach循环"和"迭代器遍历"：
 * 实现此接口允许对象成为“for-each循环”语句的目标-意思就是：实现了此接口的集合可以使用foreach遍历，因为foreach的原理就是iterator
 *
 * 如果集合直接实现Iterator，那么多线程去操作同一个集合时容易出错，
 * 在集合和Iterator之间加一个Iterable，通过Iterable，每个线程就可以获取自己的Iterator，各个迭代器之间互不影响
 */
public interface Iterable<T> {
    /**
     * 集合对象调用该方法，返回自身的迭代器
     */
    Iterator<T> iterator();

    /**
     * 为所有元素执行操作
     *
     * @param action The action to be performed for each element
     * @throws NullPointerException if the specified action is null
     * @since 1.8
     */
    default void forEach(Consumer<? super T> action) {
        Objects.requireNonNull(action);
        for (T t : this) {
            action.accept(t);
        }
    }

    /**
     * Creates a {@link Spliterator} over the elements described by this
     * {@code Iterable}.
     *
     * @implSpec
     * The default implementation creates an
     * <em><a href="Spliterator.html#binding">early-binding</a></em>
     * spliterator from the iterable's {@code Iterator}.  The spliterator
     * inherits the <em>fail-fast</em> properties of the iterable's iterator.
     *
     * @implNote
     * The default implementation should usually be overridden.  The
     * spliterator returned by the default implementation has poor splitting
     * capabilities, is unsized, and does not report any spliterator
     * characteristics. Implementing classes can nearly always provide a
     * better implementation.
     *
     * @return a {@code Spliterator} over the elements described by this
     * {@code Iterable}.
     * @since 1.8
     */
    default Spliterator<T> spliterator() {
        return Spliterators.spliteratorUnknownSize(iterator(), 0);
    }
}
