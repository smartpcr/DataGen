package org.finra.test.datagen;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by xiaodongli on 9/9/15.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ColumnMapping {
    DbType DbType() default DbType.Varchar;
    int Size() default 50;
    int Precision() default 18;
    int Scale() default 8;
}
