package java.lang;

import java.lang.annotation.*;
import static java.lang.annotation.ElementType.*;

/**
 * 指示编译器去忽略注解中声明的警告
 *
 * @author Josh Bloch
 * @since 1.5
 */
@Target({TYPE, FIELD, METHOD, PARAMETER, CONSTRUCTOR, LOCAL_VARIABLE})
@Retention(RetentionPolicy.SOURCE)
public @interface SuppressWarnings {
    String[] value();
}
