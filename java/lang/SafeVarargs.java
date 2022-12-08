package java.lang;

import java.lang.annotation.*;

/**
 * 忽略任何使用参数为泛型变量的方法或构造函数产生的警告
 *
 * 用于标记构造函数和方法，由于保留策略声明为RUNTIME，所以此注解在运行时生效
 * 对于非static或非final声明的方法，不适用，编译不通过
 * 对于标记的方法，其参数个数不能固定
 * 作用：抑制编译器在编译时的堆污染警告（并不安全）
 *     数组元素的数据类型在编译和运行时都是确定的，而泛型的数据类型只有在运行时才能确定下来
 *     当一个方法的参数类型为 可变长泛型 时，编译器在编译阶段无法检查数据类型是否和真实入参匹配，因此会给出警告信息“堆污染”(heap pollution)
 *     如果确实发生了堆污染，会导致“ClassCastException“（类型转换异常）
 *     所以在参数类型为 可变长泛型 的static或final方法上使用注解@SafeVarargs抑制编译器给出的警告，但是这并不安全
 * 用法：一般在源码中常见
 *      对于参数个数不固定且类型为泛型的方法，加注解@SafeVarargs抑制编译时的堆污染警告
 *
 * -------------------------------
 * SafeVarargs
 *           safe:安全
 *           var:可变
 *           args:参数
 * -------------------------------
 *
 * 堆污染是什么？
 * 泛型擦除是什么？
 *
 * @author ASUS
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD})
public @interface SafeVarargs {}
