package or.smert4j.framework.annotation;

import java.lang.annotation.*;

/**
 * Created by yz on 2017/7/22.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {
    Class<? extends Annotation> value();
}
