package ru.spbau.svidchenko.hw06.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Means that method is test
 * Tests can be executed by TestRunner
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Test {

    /**
     * Ignore this test with cause
     */
    String ignore() default "";

    /**
     * Expect an exception
     */
    Class<? extends Throwable> expected() default NotAnException.class;
}
