
package ru.yandex.practicum.filmorate.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ ElementType.FIELD }) // Аннотация может быть применена к полям
@Retention(RetentionPolicy.RUNTIME) // Аннотация доступна во время выполнения
@Constraint(validatedBy = DateEqualOrAfterValidator.class) // Указываем класс-валидатор
public @interface DateEqualOrAfter {
    String message() default "Date must be equal or after the specified date"; // Сообщение об ошибке

    Class<?>[] groups() default {}; // Группы для валидации
    Class<? extends Payload>[] payload() default {}; // Дополнительные данные
    String value(); // Параметр аннотации: дата, с которой сравниваем
}

