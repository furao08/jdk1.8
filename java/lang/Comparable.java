package java.lang;

/**
 * @author  Josh Bloch
 * comparable：可比较的
 *
 * Comparable - 类内部
 * Comparator - 类外部（不改变类，但影响对象的行为）
 *
 * == 地址比较
 * equals() 形比较
 * compareTo() 值比较
 */
public interface Comparable<T> {
    /**
     * 需要进行排序或比较大小的类会实现该接口，并重写compareTo()
     * a.compareTo(b)比较规则：
     *    1.a比b大，返回正数 1
     *    2.a比b小，返回负数 -1
     *    3.a等于b，返回 0
     */
    int compareTo(T o);
}
