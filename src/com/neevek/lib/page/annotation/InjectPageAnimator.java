package com.neevek.lib.page.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.neevek.lib.page.anim.PageAnimator;

/**
 * Created by neevek on 12/27/13.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectPageAnimator {
    Class<? extends PageAnimator> value();
}
