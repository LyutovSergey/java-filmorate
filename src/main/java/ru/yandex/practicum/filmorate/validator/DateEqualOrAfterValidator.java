package ru.yandex.practicum.filmorate.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateEqualOrAfterValidator implements ConstraintValidator<DateEqualOrAfter, LocalDate> {

    private LocalDate comparisonDate;

    @Override
    public void initialize(DateEqualOrAfter constraintAnnotation) {
        // Парсим значение параметра аннотации в LocalDate
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        comparisonDate = LocalDate.parse(constraintAnnotation.value(), formatter);
    }

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        if (date == null) {
            return false; // Поле не должно быть null
        }
        return date.isAfter(comparisonDate) || date.isEqual(comparisonDate);
    }
}
