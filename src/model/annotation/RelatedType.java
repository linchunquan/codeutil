package model.annotation;

import java.lang.annotation.Documented;   
import java.lang.annotation.Inherited;   
import java.lang.annotation.Retention;   
import java.lang.annotation.Target;   
import java.lang.annotation.ElementType;   
import java.lang.annotation.RetentionPolicy; 

@Target(ElementType.FIELD)   
@Retention(RetentionPolicy.RUNTIME)   
@Documented  
@Inherited 
public @interface RelatedType {
	String type();
	boolean isOneToOne() default false;
	String propertyRef() default "";
	boolean isOnleForForeignKey()default false;
	boolean isUnidirectional()default false;
	boolean isAssociateDelete()default false;
}
