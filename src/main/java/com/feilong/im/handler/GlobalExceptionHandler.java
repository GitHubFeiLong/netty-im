package com.feilong.im.handler;

import com.feilong.im.core.Result;
import com.feilong.im.exception.BasicException;
import com.feilong.im.exception.ClientException;
import com.feilong.im.exception.ServerException;
import io.jsonwebtoken.ClaimJwtException;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.UnexpectedTypeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * 全局异常
 * @author cfl 2026/04/09
 */
// @Hidden
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    /**
     * 请求对象
     */
    public final HttpServletRequest request;
    /**
     * 响应对象
     */
    public final HttpServletResponse response;


    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result<BasicException> authenticationExceptionDispose(ExpiredJwtException exception){
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        ClientException e = ClientException.of(exception, "登录失效，请重新登录");
        e.setCode(String.valueOf(httpStatus.value()));
        e.setStatus(httpStatus.value());
        printErrorMessage(e);
        return Result.ofFail(e);
    }

    /**
     * 用户访问带有权限注解的方法但权限不足时，会抛出此异常
     * AuthorizationDeniedException 只会在以下情况触发：
     * <ul>
     *     <li>URL 通过了 Spring Security 的 URL 级别检查</li>
     *     <li>但方法级别的 @PreAuthorize / @Secured 等注解验证失败</li>
     * </ul>
     * @param exception 权限不足异常
     * @return 响应对象
     */
    @ExceptionHandler(AuthorizationDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Result<BasicException> authorizationDeniedExceptionDispose(AuthorizationDeniedException exception){
        HttpStatus httpStatus = HttpStatus.FORBIDDEN;
        ClientException e = ClientException.of(exception, "权限不足");
        e.setCode(String.valueOf(httpStatus.value()));
        e.setStatus(httpStatus.value());
        printErrorMessage(e);
        return Result.ofFail(e);
    }


    /**
     * 处理认证异常（AuthenticationException）
     *
     * <p><b>触发场景：</b></p>
     * <ul>
     *   <li>Controller 方法内手动抛出 AuthenticationException 及其子类异常</li>
     *   <li>例如：在登录接口中调用 authenticationManager.authenticate() 时认证失败</li>
     * </ul>
     *
     * <p><b>与 AuthenticationEntryPointImpl 的区别：</b></p>
     * <ul>
     *   <li>AuthenticationEntryPointImpl：处理 Security 过滤器链中的认证失败（如 Token 无效）</li>
     *   <li>本方法：处理 Controller 方法执行过程中抛出的认证异常</li>
     * </ul>
     *
     * <p><b>HTTP 响应码：</b>401 UNAUTHORIZED（未授权）</p>
     *
     * @param exception 认证异常对象，包含具体的认证失败原因
     * @return 统一的错误响应结果，包含错误码、错误消息和状态码
     *
     * @see org.springframework.security.core.AuthenticationException
     * @see com.feilong.im.config.security.AuthenticationEntryPointImpl
     */
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result<BasicException> authenticationExceptionDispose(AuthenticationException exception){
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        // 根据具体异常类型返回更精确的消息
        String message = switch (exception) {
            case UsernameNotFoundException ignored -> "用户名或密码错误";
            case BadCredentialsException ignored -> "用户名或密码错误";
            case AccountExpiredException ignored -> "账户已过期";
            case DisabledException ignored -> "账户未激活";
            case LockedException ignored -> "账户已锁定";
            default -> "认证失败：" + exception.getMessage();
        };
        ClientException e = ClientException.of(exception, message);
        e.setCode(String.valueOf(httpStatus.value()));
        e.setStatus(httpStatus.value());
        printErrorMessage(e);
        return Result.ofFail(e);
    }

    /**
     * 缺少请求参数异常，直接抛出
     * @param exception
     * @return
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<BasicException> missingServletRequestParameterExceptionDispose(MissingServletRequestParameterException exception){
        ClientException e = ClientException.of(exception, "缺少请求参数：%s", exception.getParameterName());
        e.setServerMessage(exception.getMessage());
        printErrorMessage(e);
        return Result.ofFail(e);
    }


    /**
     * 全局处理自定义异常
     * @param e 自定义异常对象
     * @return 响应对象
     */
    @ExceptionHandler(BasicException.class)
    public Result<BasicException> basicExceptionDispose(BasicException e){
        // 设置响应码
        response.setStatus(e.getStatus());
        printErrorMessage(e);
        return  Result.ofFail(e);
    }


    /**
     * 运行时异常，其它框架抛出的异常未封装（openfeign调用服务时）
     * 后续可能会继续扩展....
     * @param exception 运行时异常
     * @return 响应对象
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<BasicException> runtimeExceptionDispose(RuntimeException exception){
        ServerException e = ServerException.of(exception, "服务器异常：%s", exception.getMessage());
        // 堆栈跟踪
        printErrorMessage(e);
        return Result.ofFail(e);
    }

    /**
     * 捕获意料之外的异常Exception
     * @param exception 异常
     * @return 响应对象
     */
    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Throwable> throwableDispose(Throwable exception){
        ServerException e = ServerException.of(exception, "未知的异常：%s", exception.getMessage());
        printErrorMessage(e);
        return Result.ofFail(e);
    }

    //~ web常见异常
    //==================================================================================================================
    /**
     * 400 Bad Request
     * 因发送的请求语法错误,服务器无法正常读取.
     * @param exception 异常对象
     * @return 响应对象
     */
    @ExceptionHandler(value = {
            BindException.class,
            MethodArgumentNotValidException.class,
            MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class,
            IllegalArgumentException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Throwable> ValidExceptionDispose(Exception exception){
        assert exception != null;
        List<String> messages = new ArrayList<>();
        switch (exception) {
            case BindException bindException -> {
                List<ObjectError> list = bindException.getAllErrors();
                for (ObjectError item : list) {
                    messages.add(item.getDefaultMessage());
                }
            }
            case MethodArgumentTypeMismatchException methodArgumentTypeMismatchException ->
                    messages.add("路径参数错误，或请求资源不存在");
            case HttpMessageNotReadableException httpMessageNotReadableException -> messages.add("请求参数丢失");
            case IllegalArgumentException illegalArgumentException -> messages.add(exception.getMessage());
            default -> {
            }
        }

        ClientException e = ClientException.of(exception, "请求错误：%s", String.join(",", messages));
        printErrorMessage(e);

        return Result.ofFail(e);
    }

    /**
     * 404 Not Found
     * 请求失败，请求所希望得到的资源未被在服务器上发现。没有信息能够告诉用户这个状况到底是暂时的还是永久的。假如服务器知道情况的话，应当使用410状态码来告知旧资源因为某些内部的配置机制问题，已经永久的不可用，而且没有任何可以跳转的地址。404这个状态码被广泛应用于当服务器不想揭示到底为何请求被拒绝或者没有其他适合的响应可用的情况下。出现这个错误的最有可能的原因是服务器端没有这个页面。
     * @param exception 异常对象
     * @return 响应对象
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Result<?> noHandlerFoundExceptionDispose(NoHandlerFoundException exception) {
        // 堆栈跟踪
        ClientException e = new ClientException(exception);
        e.setStatus(HttpStatus.NOT_FOUND.value());
        e.setCode(String.valueOf(HttpStatus.NOT_FOUND.value()));
        e.setClientMessage("当前请求资源不存在，请稍后再试");
        e.setServerMessage("Not Found - 目标资源资源不存在：" + request.getRequestURL().toString());
        printErrorMessage(e);
        return Result.ofFail(e);
    }

    /**
     * 405 Method Not Allowed
     * 请求行中指定的请求方法不能被用于请求相应的资源。该响应必须返回一个Allow 头信息用以表示出当前资源能够接受的请求方法的列表。
     * 鉴于 PUT，DELETE 方法会对服务器上的资源进行写操作，因而绝大部分的网页服务器都不支持或者在默认配置下不允许上述请求方法，对于此类请求均会返回405错误。
     * @param exception 异常对象
     * @return 响应对象
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public Result<?> httpRequestMethodNotSupportedExceptionDispose(HttpRequestMethodNotSupportedException exception) {
        // 堆栈跟踪
        ClientException e = new ClientException(exception);
        e.setStatus(HttpStatus.METHOD_NOT_ALLOWED.value());
        e.setCode(String.valueOf(HttpStatus.METHOD_NOT_ALLOWED.value()));
        e.setClientMessage("当前资源请求方式错误，请稍后再试");
        e.setServerMessage("Method Not Allowed - 目标资源资源不存在： + url" + request.getRequestURL().toString());
        printErrorMessage(e);
        return Result.ofFail(e);
    }

    /**
     * 406 Not Acceptable
     * 请求的资源的内容特性无法满足请求头中的条件，因而无法生成响应实体。
     * @param exception 异常对象
     * @return 响应对象
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public Result<?> httpMediaTypeNotSupportedExceptionDispose(HttpMediaTypeNotSupportedException exception) {
        // 堆栈跟踪
        ClientException e = new ClientException(exception);
        e.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
        e.setCode(String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()));
        e.setClientMessage("当前请求携带的请求头错误，请稍后再试");
        e.setServerMessage(String.format("%s 资源不支持%s:%s 方式", request.getRequestURL().toString(), "Content-Type", request.getHeader("Content-Type")));
        printErrorMessage(e);
        return Result.ofFail(e);
    }

    /**
     * 500 参数校验注解写错了
     * @param exception 异常对象
     * @return 响应对象
     */
    @ExceptionHandler(UnexpectedTypeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<BasicException> UnexpectedTypeExceptionExceptionDispose(Exception exception){
        ServerException e = ServerException.of("参数校验注解写错了:%s", exception.getMessage());
        e.setClientMessage("服务器内部错误，请稍后再试");
        printErrorMessage(e);
        return Result.ofFail(e);
    }


    /**
     * 400 参数校验注解不满足
     * @param exception 异常对象
     * @return 响应对象
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> ConstraintViolationExceptionDispose(Exception exception){
        List<String> messages = new ArrayList<>();
        // 属性校验失败，不满足注解
        for (ConstraintViolation<?> constraintViolation : ((ConstraintViolationException)exception).getConstraintViolations()) {
            String full = String.format("参数%s %s",constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage());
            messages.add(full);
        }

        ClientException e = ClientException.of("参数校验失败:%s", String.join(",", messages));
        printErrorMessage(e);
        return Result.ofFail(e);
    }

    /**
     * 400 数据库唯一索引冲突
     * @param exception 唯一索引异常对象
     * @return 响应对象
     */
    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> DuplicateKeyException(DuplicateKeyException exception){
        ClientException e = ClientException.of("保存失败，数据已存在");
        e.setServerMessage(exception.getMessage());
        printErrorMessage(e);
        return Result.ofFail(e);
    }

    /**
     * 打印异常的错误信息和堆栈信息。
     * @param e 异常信息
     */
    private void printErrorMessage(BasicException e) {
        log.error( "http响应码：{}，错误代码：{}，客户端错误信息：{}，服务端错误信息：{}，扩展信息：{}", e.getStatus(), e.getCode(), e.getClientMessage(), e.getServerMessage(), e.getDataMap(), e);
    }
}
