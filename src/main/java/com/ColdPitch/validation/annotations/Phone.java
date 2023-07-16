/**
 * @project ColdPitch
 * @author ARA
 * @since 2023-07-06 PM 4:51
 */

package com.ColdPitch.validation.annotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
//@Constraint(validatedBy = PhoneValidator.class)
public @interface Phone {
    // Annotation Element
    String message() default "전화번호가 아님";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
