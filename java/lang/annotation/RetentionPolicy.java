package java.lang.annotation;

/**
 * RetentionPolicy用来描述注解的生命周期：Java源文件(.java文件) ---> .class文件 ---> 内存中的字节码（JVM加载.class到内存，运行期）
 *
 * 源文件、class文件存储在磁盘中，只有加载时，class文件才会存入内存
 *
 * Policy ： 政策、方针
 *
 * @author  Joshua Bloch
 */
public enum RetentionPolicy {
    /**
     * 注解只保留在源文件，当Java文件编译成class文件的时候，注解被遗弃
     */
    SOURCE,

    /**
     * 注解被保留到class文件，Java文件编译成class文件的时存在，但jvm加载class文件时候被遗弃，这是默认的生命周期
     */
    CLASS,

    /**
     * jvm加载class文件之后，仍然存在
     */
    RUNTIME
}
