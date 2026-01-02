package ru.yandex.practicum.filmorate.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@Slf4j
@RestControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidation(MethodArgumentNotValidException e) {
        return Map.of("error", e.getMessage());
    }
    /*
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleConstraint(ConstraintViolationException e) {
        return Map.of("error", e.getMessage());
    }
*/
    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.CONFLICT)
    public Map<String, String> DuplicatedDataException(DuplicatedDataException e){
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public Map<String, String> ConditionsNotMetException(ConditionsNotMetException e){
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public Map<String, String> NotFoundException(NotFoundException e){
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> defaultHandler(Exception E){
        log.error("Произошла ошибка: {}", E.getMessage());
        return Map.of("error", "Не удалось обработать!");
    }
}
