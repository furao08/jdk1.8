package java.lang.annotation;

/**
 * 注解的作用目标
 *
 * @author ASUS
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Target {
    /**
     * 注解的作用目标的值
     */
    ElementType[] value();
}
