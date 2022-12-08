package java.lang.annotation;

/**
 * 默认情况下,javadoc是不包括注解的. 但如果声明注解时指定了 @Documented,则该注解会被 javadoc 之类的工具记录
 * 没什么实际的作用，可不关注
 *
 * @author  Joshua Bloch
 * @since 1.5
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Documented {
}
