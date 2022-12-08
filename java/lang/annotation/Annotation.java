package java.lang.annotation;

/**
 *
 * @author  Josh Bloch
 */
public interface Annotation {

    boolean equals(Object obj);

    @Override
    int hashCode();

    @Override
    String toString();

    Class<? extends Annotation> annotationType();
}
