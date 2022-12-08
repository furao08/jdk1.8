package java.lang.annotation;

/**
 * 如果一个类用上了@Inherited修饰的注解，那么其子类也会继承这个注解
 *
 * 子类继承父类，不会继承父类的注解，@Inherited就是解决这个问题的
 *
 * @author  Joshua Bloch
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Inherited {
}
