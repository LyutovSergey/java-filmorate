package ru.yandex.practicum.filmorate.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.validation.Constraint;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint()
public class DateEqualOrAfter {
}
