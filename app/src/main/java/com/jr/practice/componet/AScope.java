package com.jr.practice.componet;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by Administrator on 2016-08-30.
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface AScope {
}
