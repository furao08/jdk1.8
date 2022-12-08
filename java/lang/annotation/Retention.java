package java.lang.annotation;

/**
 * java中元注解(用来修饰注解的注解)有四个： @Retention @Target @Documented @Inherited
 * @Retention 指定注解的保留位置
 *
 * retention：保持、保留
 *
 * @author ASUS
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Retention {
    /**
     * 注解的保留位置（生命周期）
     */
    RetentionPolicy value();
}
