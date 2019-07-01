package cn.onlov.evaluate.util.module.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
* @Description:  注解
* @author zhangxiaowei
* @date 2018年8月15日
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CSVField {
 
    /**
     * CSV文件列名
     */
    public String name() default "";
       
}