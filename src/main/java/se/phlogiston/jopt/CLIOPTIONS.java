package se.phlogiston.jopt;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Classname: CLIOPTIONS
 * Author(s): Erik Svensson
 * Date: Sep 23, 2009
 * Version: $Id$
 * Copyright Notice: 2009 Erik Svensson
 * Description:
 */
@Target(java.lang.annotation.ElementType.FIELD)
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface CLIOPTIONS {

    String shortOpt();
    String longOpt() default "";
    OptArgs argType() default OptArgs.NONE;
    String help() default "";
    String notWith() default "";
}
