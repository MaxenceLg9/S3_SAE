package net.mpvm.saeimmobilier.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark unfinished code
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.METHOD, ElementType.TYPE,ElementType.CONSTRUCTOR})
public @interface Unfinished {
    
    String value = "";
}