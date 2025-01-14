package com.movie.tdmb.exception;
import com.movie.tdmb.dto.ErrorDto;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    /**
     * Handles validation exceptions and returns an ErrorDTO.
     *
     * @param request the HTTP request
     * @param ex      the exception
     * @return an ErrorDTO containing error details
     */
    @ExceptionHandler({ MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorDto handleValidationExceptions(HttpServletRequest request, MethodArgumentNotValidException ex) {
        ErrorDto error = new ErrorDto();
        error.setTimestamp(new Date());
        error.setPath(request.getServletPath());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
            error.addError(fieldError.getDefaultMessage());
        });
        logger.error("Validation error: {}", ex.getBindingResult().getAllErrors(), ex);
        return error;
    }
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorDto handleRuntimeException(HttpServletRequest request, RuntimeException ex) {
        ErrorDto error = new ErrorDto();
        error.setTimestamp(new Date());
        error.setPath(request.getServletPath());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.addError(ex.getMessage());
        logger.error("Runtime Exception: {}", ex.getMessage(), ex);
        return error;
    }

    @ExceptionHandler({PersonNotFoundException.class, MovieNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorDto handleNotFoundException(HttpServletRequest request, Exception ex) {
        ErrorDto error = new ErrorDto();
        error.setTimestamp(new Date());
        error.setPath(request.getServletPath());
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.addError(ex.getMessage());
        logger.error("Runtime Exception: {}", ex.getMessage(), ex);
        return error;
    }
   /**
     * Handles general exceptions and returns an ErrorDTO.
     *
     * @param request the HTTP request
     * @param ex      the exception
     * @return an ErrorDTO containing error details
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorDto handleGeneralException(HttpServletRequest request, Exception ex) {
        if (ex instanceof MethodArgumentNotValidException) {
            return handleValidationExceptions(request, (MethodArgumentNotValidException) ex);
        }
        if(ex instanceof  RuntimeException)
        {
            return handleRuntimeException(request, (RuntimeException) ex);
        }
        ErrorDto error = new ErrorDto();
        error.setTimestamp(new Date());
        error.setPath(request.getServletPath());
        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.addError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        logger.error("Internal Server Error: {}", ex.getMessage(), ex);
        return error;
    }
}
