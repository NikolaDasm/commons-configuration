package nikoladasm.commons.configuration.properties.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface MethodPriority {
	public static int DEFAULT_PRIORITY = 1 << 20;
	int value() default DEFAULT_PRIORITY;
}
