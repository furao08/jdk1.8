package java.lang.annotation;

/**
 * 注解的作用目标的值
 *
 * @author  Joshua Bloch
 */
public enum ElementType {
    /* 接口、类、枚举、注解 */
    TYPE,

    /* 字段、枚举的常量 */
    FIELD,

    /* 方法 */
    METHOD,

    /* 方法参数 */
    PARAMETER,

    /* 构造函数 */
    CONSTRUCTOR,

    /* 局部变量 */
    LOCAL_VARIABLE,

    /* 注解 */
    ANNOTATION_TYPE,

    /* 包 */
    PACKAGE,

    /* 类型注解 */
    TYPE_PARAMETER,

    /* 类型使用 */
    TYPE_USE
}
