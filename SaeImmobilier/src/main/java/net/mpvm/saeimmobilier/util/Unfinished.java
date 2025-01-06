package net.mpvm.saeimmobilier.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Annotation to mark unfinished code
 */
@Target({ElementType.METHOD, ElementType.TYPE,ElementType.CONSTRUCTOR})
public @interface Unfinished {
    
    String value = "";
}
