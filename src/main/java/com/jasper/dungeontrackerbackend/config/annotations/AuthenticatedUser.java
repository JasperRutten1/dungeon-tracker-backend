package com.jasper.dungeontrackerbackend.config.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER) // This means it goes inside method arguments
@Retention(RetentionPolicy.RUNTIME) //
public @interface AuthenticatedUser {
}
