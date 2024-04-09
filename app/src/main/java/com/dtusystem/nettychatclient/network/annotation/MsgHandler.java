package com.dtusystem.nettychatclient.network.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * 表明当前能够处理哪条消息
 */
@Target(ElementType.METHOD)
public @interface MsgHandler {
	String value() default "";
}