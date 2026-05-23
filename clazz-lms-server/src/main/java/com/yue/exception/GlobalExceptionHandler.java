package com.yue.exception;

import com.yue.pojo.dto.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * Global exception handler class
 * @RestControllerAdvice 告诉 Spring ，这个类是一个全局异常处理器（Global Exception Handler），所有 Controller 抛出的异常都交给我
 * 内部组成：@RestControllerAdvice = @ControllerAdvice + @ResponseBody
 * - @ControllerAdvice -> 注册为“全局 Controller 增强器”
 * - @ResponseBody -> 方法返回值直接作为 HTTP 响应体 （ JSON 化）
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * A resource not found exception handler
     * @param ex 就是抛出来的那个异常对象。你可以拿到它的 message、errorCode、stackTrace 等。
     * @param request 当前的 HTTP 请求对象。你可以拿到 URL、请求头、请求方法等。
     * @return
     * @ExceptionHandler(ResourceNotFoundException.class) “异常类型路由”：当捕获到 ResourceNotFoundException 时，调用这个方法处理
     * **工作方式**
     * 1. Spring 收到一个异常
     * 2. Spring 在所有 @RestControllerAdvice 类的方法里找
     * 3. 找一个 @ExceptionHandler 匹配的方法
     * 4. 调用那个方法
     * **匹配规则**：支持继承。如果你写 @ExceptionHadnler(BaseException.class)，那它会匹配所有 BaseException 的子类
     *
     * **方法参数**
     * ResourceNotFoundException ex: 就是抛出来的那个异常对象。你可以拿到它的 message、errorCode、stackTrace 等。
     * HttpServletRequest request: 当前的 HTTP 请求对象。你可以拿到 URL、请求头、请求方法等。
     *
     * 这两个参数都是"可选的"——你可以全要、要一个、都不要。Spring 会根据你的方法签名智能地提供。
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFoundException(
            ResourceNotFoundException ex,
            HttpServletRequest request) {

        // 构造 ErrorResponseDTO，将异常信息和请求信息组合成错误响应
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                ex.getErrorCode(), // "RESOURCE_NOT_FOUND"
                ex.getMessage(), // "Clazz(Class) with id 999 not found"
                LocalDateTime.now(), // 当前时间戳
                request.getRequestURI() // "/clazzs/999"
        );

        /**
         * ResponseEntity
         * ResponseEntity<T> 是什么？
         * 是 Spring 对“一个完整 HTTP 响应的抽象。包括：
         * - 状态码(200、404、500...)
         * - 响应头(Content-Type 等)
         * - 响应体
         *
         * 为什么必须使用 ResponseEntity ?
         * 因为需要设置 HTTP 状态码
         * ResponseEntity 既能返回 body，又控制状态码
         */
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDTO);
    }

    /**
     * handle the unauthorised exception
     * @param ex an unauthorised exception object
     * @param request current HTTP request object
     * @return a  HTTP response entity
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponseDTO> handleUnauthorizedException(
            UnauthorizedException ex,
            HttpServletRequest request) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                ex.getErrorCode(),
                ex.getMessage(),
                LocalDateTime.now(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponseDTO);
    }

    /**
     * handle the forbidden exception
     * @param ex a forbidden exception object
     * @param request current HTTP request object
     * @return a HTTP response entity
     */
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponseDTO> handleForbiddenException(
            ForbiddenException ex,
            HttpServletRequest request) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                ex.getErrorCode(),
                ex.getMessage(),
                LocalDateTime.now(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponseDTO);
    }

    /**
     * handle validation exception
     * @param ex a validation exception object
     * @param request current HTTP request object
     * @return a HTTP response entity
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationException(
            ValidationException ex,
            HttpServletRequest request) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                ex.getErrorCode(),
                ex.getMessage(),
                LocalDateTime.now(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDTO);
    }

    /**
     * handle business rule violation exception
     * @param ex a business rule violation exception object
     * @param request current HTTP request object
     * @return a HTTP response entity
     */
    @ExceptionHandler(BusinessRuleViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleBusinessRuleViolationException(
            BusinessRuleViolationException ex,
            HttpServletRequest request) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                ex.getErrorCode(),
                ex.getMessage(),
                LocalDateTime.now(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponseDTO);
    }

    /**
     * Handles requests to URLs that don't match any controller mapping.
     *
     * <p>Spring 6 / Spring Boot 3 changed default behaviour so that requests to
     * non-existent URLS throw {@Link NoResourceFoundException} instead of
     * returning HTTP 404 directly. Without this dedicated handler, the
     * {@code Exception.class} catch-all below would map these to HTTP 500 -
     * an obvious bug, since "URL doesn't exist" is a client problem, not a
     * server error.
     *
     * <p> The external message is intentionaly generic ("does not exist")
     * rather than echoing the requested path or framework details - this
     * avoids leaking implementation hints to malicious scanners.
     *
     * @param ex the not-found exception thrown by Spring
     * @param request current HTTP request (used to populate the path field)
     * @return a 404 Not Found response with a proper error body
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNoResourceFoundException(
            NoResourceFoundException ex,
            HttpServletRequest request) {
        // log at WARN, not ERROR - a request to a nonexistent URL is a client
        // mistake (or scanner probe), not a server-side failure.
        log.warn("No resource found at {}", request.getRequestURI());

        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                "RESOURCE_NOT_FOUND",
                "The requested resource does not exist",
                LocalDateTime.now(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDTO);
    }

    /**
     * handle uncaught exception
     * @param ex an exception object
     * @param request current HTTP request object
     * @return a response entity
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleUncaughtException(
            Exception ex,
            HttpServletRequest request) {

        // record detailed exception info into server log (including stack tree)
        log.error("Uncaught Exception at {}: {}", request.getRequestURI(), ex.getMessage(), ex);

        // the information returned to client should be conservative - DO NOT expose server-side details
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                "INTERNAL_ERROR",
                "An unexpected error occurred. Please contact support.",
                LocalDateTime.now(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponseDTO);
    }

    /**
     * Handles validation failures triggered by {@code @Valid} on controller method parameters.
     * <p>
     * Spring throws {@link MethodArgumentNotValidException} when one or more fields
     * fail Bean Validation constraints. This handler aggregates all field errors into
     * a single human-readable message and returns HTTP 400 Bad Request - the
     * appropriate status for client-side input errors.
     *
     * @param ex the exception thrown by the framwork
     * @param request the current HTTP request (used to populate the path field)
     * @return a 400 Bad Request response with field-level error details
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request){

        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + ":" + fieldError.getDefaultMessage())
                .collect(Collectors.joining("; "));

        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                "VALIDATION_FAILED",
                errorMessage,
                LocalDateTime.now(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDTO);
    }

    @ExceptionHandler(RefreshTokenExpiredException.class)
    public ResponseEntity<ErrorResponseDTO> handleRefreshTokenExpired(
            RefreshTokenExpiredException e,
            HttpServletRequest request) {
        log.info("RefreshToken expired at {}: {}", request.getRequestURI(), e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponseDTO(
                        "REFRESH_TOKEN_EXPIRED",
                        e.getMessage(),
                        LocalDateTime.now(),
                        request.getRequestURI()
                ));
    }

    /**
     *
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidRefreshToken(
            InvalidRefreshTokenException e,
            HttpServletRequest request) {
        log.info("InvalidRefreshToken at {}: {}", request.getRequestURI(), e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponseDTO(
                        "INVALID_REFRESH_TOKEN",
                        e.getMessage(),
                        LocalDateTime.now(),
                        request.getRequestURI()
                ));
    }
}
