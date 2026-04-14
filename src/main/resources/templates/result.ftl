package ${package.Result};

import com.feilong.acapi.common.base.BasicExceptionInterface;
import com.feilong.acapi.common.base.ExceptionEnumInterface;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.io.Serial;
import java.io.Serializable;
import java.util.*;

/**
* 统一API响应结果封装
*
* @author ${globalConfig.author}
* @date 2025-04-01
* @version 1.0.0
* @since 1.0.0
*/
@Data
@Schema(description = "统一API响应结果封装")
public class Result<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = -1L;

    /**
    * 默认客户端消息
    */
    private static final String DEFAULT_SUCCESS_CLIENT_MESSAGE = "执行成功";

    /**
    * 成功
    */
    public static final String SUCCESS = "0";

    /**
    * 失败
    */
    public static final String FAIL = "1";

    /**
    * http状态码，默认200成功
    */
    @Schema(description = "http状态码，默认200成功")
    private int status = 200;

    /**
    * 状态码,“0”代表成功，非“0”代表失败
    * <pre>
     *     {@code code = 0}     成功
     *     {@code code != 0}    失败
     * </pre>
    */
    @Schema(description = "状态码,“0”代表成功，非“0”代表失败")
    private String code = "0";

    /**
    * 客户端状态码对应信息
    */
    @Schema(description = "客户端状态码对应信息")
    private String clientMessage;

    /**
    * 服务器状态码对应信息
    */
    @Schema(description = "服务器状态码对应信息")
    private String serverMessage;

    /**
    * 数据
    */
    @Schema(description = "数据")
    private T data;

    /**
    * 额外数据
    */
    @Schema(description = "额外数据")
    private Map<Object, Object> dataMap;

    /**
    * 属性，用于额外操作
    */
    @Schema(description = "属性，用于额外操作")
    private List<String> properties;

        /**
        * 时间戳
        */
        @Schema(description = "时间戳")
        private Date timestamp = new Date();

        //~static methods
        //==================================================================================================================
        /**
        * 返回成功
        * @return result
        */
        public static <T> Result<T> ofSuccess() {
                return new Result<>(200, Result.SUCCESS, DEFAULT_SUCCESS_CLIENT_MESSAGE);
                }

                /**
                * 返回成功,带数据
                * @return result
                */
                public static <T> Result<T> ofSuccess(T t) {
                        return new Result<>(200, Result.SUCCESS, DEFAULT_SUCCESS_CLIENT_MESSAGE, t);
                        }

                        /**
                        * 返回成功,提示用户信息,并携带一些数据
                        * @param clientMessage 客户端小时
                        * @param t             data
                        * @return  result
                        */
                        public static <T> Result<T> ofSuccess(String clientMessage, T t) {
                                return new Result<>(200, Result.SUCCESS, clientMessage, t);
                                }

                                /**
                                * 返回失败
                                * @return result
                                */
                                public static <T> Result<T> ofFail() {
                                        return new Result<>(500, Result.FAIL);
                                        }

                                        /**
                                        * 返回失败，带数据
                                        * @param t data数据
                                        * @return result
                                        */
                                        public static <T> Result<T> ofFail(T t) {
                                                return new Result<>(500, Result.FAIL, t);
                                                }

                                                /**
                                                * 使用 BasicExceptionInterface 转换成响应对象
                                                * @param basicException    异常
                                                * @return  result
                                                */
                                                public static <T> Result<T> ofFail(BasicExceptionInterface basicException) {
                                                        return new Result<T>(basicException.getStatus(), basicException.getCode(), basicException.getClientMessage(), basicException.getServerMessage());
                                                            }

                                                            /**
                                                            * 只返回失败信息，不抛额异常
                                                            * @param enumInterface 异常枚举
                                                            * @return result
                                                            */
                                                            public static <T> Result<T> ofFail(ExceptionEnumInterface enumInterface) {
                                                                    return new Result<T>(enumInterface.getStatus(), enumInterface.getCode(), enumInterface.getClientMessage(), enumInterface.getServerMessage());
                                                                        }

                                                                        /**
                                                                        * 400 Bad Request
                                                                        * @param clientMessage 客户端显示错误
                                                                        * @param serverMessage 服务端错误
                                                                        * @return result
                                                                        */
                                                                        public static <T> Result<T> ofFailByBadRequest(String clientMessage, String serverMessage) {
                                                                                return new Result<T>(400, "400", clientMessage, "Bad Request - " + serverMessage);
                                                                                    }

                                                                                    /**
                                                                                    * 400 Bad Request
                                                                                    * @param clientMessage 客户端显示错误
                                                                                    * @param serverMessage 服务端错误
                                                                                    * @return result
                                                                                    */
                                                                                    public static <T> Result<T> ofFailByForBidden(String clientMessage, String serverMessage) {
                                                                                            return new Result<>(403, "403", clientMessage, "Forbidden - " + serverMessage);
                                                                                            }

                                                                                            /**
                                                                                            * 404 Not Found
                                                                                            * @param url 访问的资源地址
                                                                                            * @return result
                                                                                            */
                                                                                            public static <T> Result<T> ofFailByNotFound(String url) {
                                                                                                    return new Result<T>(404, "404", "当前请求资源不存在，请稍后再试", "Not Found - 目标资源资源不存在：" + url);
                                                                                                        }

                                                                                                        /**
                                                                                                        * 405 Method Not Allowed
                                                                                                        * @param url 访问的资源地址
                                                                                                        * @return result
                                                                                                        */
                                                                                                        public static <T> Result<T> ofFailByMethodNotAllowed(String url) {
                                                                                                                return new Result<T>(405, "405", "当前资源请求方式错误，请稍后再试", "Method Not Allowed - 目标资源资源不存在：" + url);
                                                                                                                    }

                                                                                                                    /**
                                                                                                                    * 406 Method Not Allowed
                                                                                                                    * @param serverMessage 异常描述
                                                                                                                    * @return result
                                                                                                                    */
                                                                                                                    public static <T> Result<T> ofFailByNotAcceptable(String serverMessage) {
                                                                                                                            return new Result<T>(406, "406", "当前请求携带的请求头错误", "Not Acceptable - " + serverMessage);
                                                                                                                                }

                                                                                                                                /**
                                                                                                                                * 判断是否成功
                                                                                                                                * @param status 状态，true-成功，false-失败
                                                                                                                                * @return result 根据状态返回结果
                                                                                                                                */
                                                                                                                                public static Result<Boolean> ofJudge(boolean status) {
                                                                                                                                    if (status) {
                                                                                                                                    return ofSuccess();
                                                                                                                                    }
                                                                                                                                    return ofFail();
                                                                                                                                    }

                                                                                                                                    //~constructors
                                                                                                                                    //==================================================================================================================
                                                                                                                                    public Result() {
                                                                                                                                    this.clientMessage = DEFAULT_SUCCESS_CLIENT_MESSAGE;
                                                                                                                                    }

                                                                                                                                    public Result(int status) {
                                                                                                                                    this.status = status;
                                                                                                                                    this.code = String.valueOf(status);
                                                                                                                                    }

                                                                                                                                    public Result(int status, String code) {
                                                                                                                                    this.status = status;
                                                                                                                                    this.code = code;
                                                                                                                                    }

                                                                                                                                    public Result(int status, String code, T data) {
                                                                                                                                    this.status = status;
                                                                                                                                    this.code = code;
                                                                                                                                    this.data = data;
                                                                                                                                    }

                                                                                                                                    public Result(int status, String code, String clientMessage) {
                                                                                                                                    this.status = status;
                                                                                                                                    this.code = code;
                                                                                                                                    this.clientMessage = clientMessage;
                                                                                                                                    }

                                                                                                                                    public Result(int status, String code, String clientMessage, T data) {
                                                                                                                                    this.status = status;
                                                                                                                                    this.code = code;
                                                                                                                                    this.clientMessage = clientMessage;
                                                                                                                                    this.data = data;
                                                                                                                                    }

                                                                                                                                    public Result(int status, String code, String clientMessage, String serverMessage) {
                                                                                                                                    this.status = status;
                                                                                                                                    this.code = code;
                                                                                                                                    this.clientMessage = clientMessage;
                                                                                                                                    this.serverMessage = serverMessage;
                                                                                                                                    }

                                                                                                                                    public Result(int status, String code, String clientMessage, String serverMessage, T data, Map<Object, Object> dataMap) {
                                                                                                                                    this.status = status;
                                                                                                                                    this.code = code;
                                                                                                                                    this.clientMessage = clientMessage;
                                                                                                                                    this.serverMessage = serverMessage;
                                                                                                                                    this.data = data;
                                                                                                                                    this.dataMap = dataMap;
                                                                                                                                    }

                                                                                                                                    public Result(String code, String clientMessage, String serverMessage) {
                                                                                                                                    this.code = code;
                                                                                                                                    this.clientMessage = clientMessage;
                                                                                                                                    this.serverMessage = serverMessage;
                                                                                                                                    }
                                                                                                                                    public Result(String code, String clientMessage, String serverMessage, T t) {
                                                                                                                                    this.code = code;
                                                                                                                                    this.clientMessage = clientMessage;
                                                                                                                                    this.serverMessage = serverMessage;
                                                                                                                                    this.data = t;
                                                                                                                                    }

                                                                                                                                    //~ 属性设置
                                                                                                                                    //==================================================================================================================
                                                                                                                                    public Result<T> status(int status) {
                                                                                                                                        this.status = status;
                                                                                                                                        return this;
                                                                                                                                        }
                                                                                                                                        public Result<T> code(String code) {
                                                                                                                                            this.code = code;
                                                                                                                                            return this;
                                                                                                                                            }

                                                                                                                                            public Result<T> clientMessage(String clientMessage) {
                                                                                                                                                this.clientMessage = clientMessage;
                                                                                                                                                return this;
                                                                                                                                                }

                                                                                                                                                public Result<T> serverMessage(String serverMessage) {
                                                                                                                                                    this.serverMessage = serverMessage;
                                                                                                                                                    return this;
                                                                                                                                                    }

                                                                                                                                                    public Result<T> data(T data) {
                                                                                                                                                        this.data = data;
                                                                                                                                                        return this;
                                                                                                                                                        }

                                                                                                                                                        public Result<T> dataMap(Map<Object, Object> dataMap) {
                                                                                                                                                            this.dataMap = dataMap;
                                                                                                                                                            return this;
                                                                                                                                                            }

                                                                                                                                                            public Result<T> dataMapPut(Map<Object, Object> dataMap) {
                                                                                                                                                                Map<Object, Object> map = Optional.ofNullable(this.dataMap).orElseGet(() -> {
                                                                                                                                                                this.dataMap = new HashMap<>();
                                                                                                                                                                return this.dataMap;
                                                                                                                                                                });
                                                                                                                                                                map.putAll(dataMap);
                                                                                                                                                                return this;
                                                                                                                                                                }

                                                                                                                                                                public Result<T> dataMapPut(String key, Object value) {
                                                                                                                                                                    Map<Object, Object> map = Optional.ofNullable(this.dataMap).orElseGet(() -> {
                                                                                                                                                                    this.dataMap = new HashMap<>();
                                                                                                                                                                    return this.dataMap;
                                                                                                                                                                    });
                                                                                                                                                                    map.put(key, value);
                                                                                                                                                                    return this;
                                                                                                                                                                    }

                                                                                                                                                                    public Result<T> dataMapPut(String... kv) {
                                                                                                                                                                        // 不是偶数位
                                                                                                                                                                        if (kv.length < 2 && kv.length % 2 != 0) {
                                                                                                                                                                        throw new IllegalArgumentException("参数kv数组不正确，要是2的倍数，其中奇数是key偶数是value");
                                                                                                                                                                        }

                                                                                                                                                                        Map<Object, Object> map = Optional.ofNullable(this.dataMap).orElseGet(() -> {
                                                                                                                                                                        this.dataMap = new HashMap<>(kv.length / 2);
                                                                                                                                                                        return this.dataMap;
                                                                                                                                                                        });

                                                                                                                                                                        // 步长为2
                                                                                                                                                                        for (int i = 0, length = kv.length; i < length; i+=2) {
                                                                                                                                                                        String key = kv[i];
                                                                                                                                                                        String value = kv[i+1];
                                                                                                                                                                        map.put(key, value);
                                                                                                                                                                        }

                                                                                                                                                                        return this;
                                                                                                                                                                        }

                                                                                                                                                                        public Result<T> dataMapPutKeys(String... keys) {
                                                                                                                                                                            Map<Object, Object> map = Optional.ofNullable(this.dataMap).orElseGet(() -> {
                                                                                                                                                                            this.dataMap = new HashMap<>(keys.length);
                                                                                                                                                                            return this.dataMap;
                                                                                                                                                                            });

                                                                                                                                                                            for (String key : keys) {
                                                                                                                                                                            map.put(key, null);
                                                                                                                                                                            }

                                                                                                                                                                            return this;
                                                                                                                                                                            }

                                                                                                                                                                            /**
                                                                                                                                                                            * 设置对象 {@code properties} 属性，并返回本对象
                                                                                                                                                                            * <p>
                                                                                                                                                                                * 该方法接受一个可变参数数组，将传入的字符串元素添加到 {@code properties} 列表中。
                                                                                                                                                                                * 如果 {@code properties} 列表为空，则会初始化一个新的列表。
                                                                                                                                                                                * </p>
                                                                                                                                                                            *
                                                                                                                                                                            * @param properties 需要添加到属性列表中的字符串元素
                                                                                                                                                                            * @return 调用该方法的对象本身，用于链式调用
                                                                                                                                                                            */
                                                                                                                                                                            public Result<T> properties(String... properties) {
                                                                                                                                                                                if (properties.length != 0) {
                                                                                                                                                                                addProperties(Arrays.asList(properties));
                                                                                                                                                                                }
                                                                                                                                                                                return this;
                                                                                                                                                                                }

                                                                                                                                                                                /**
                                                                                                                                                                                * 设置对象 {@code properties} 属性，并返回本对象
                                                                                                                                                                                * <p>
                                                                                                                                                                                    * 如果传入的列表不为空，则将其添加到当前属性列表中。
                                                                                                                                                                                    * </p>
                                                                                                                                                                                *
                                                                                                                                                                                * @param properties 需要添加的属性列表
                                                                                                                                                                                * @return 调用该方法的对象本身，用于链式调用
                                                                                                                                                                                */
                                                                                                                                                                                public Result<T> properties(List<String> properties) {
                                                                                                                                                                                        if (properties != null && !properties.isEmpty()) {
                                                                                                                                                                                        addProperties(properties);
                                                                                                                                                                                        }
                                                                                                                                                                                        return this;
                                                                                                                                                                                        }

                                                                                                                                                                                        /**
                                                                                                                                                                                        * 设置对象 {@code properties} 属性，并返回本对象
                                                                                                                                                                                        * <p>
                                                                                                                                                                                            * 将单个字符串属性添加到当前属性列表中。
                                                                                                                                                                                            * </p>
                                                                                                                                                                                        *
                                                                                                                                                                                        * @param property 需要添加的单个属性
                                                                                                                                                                                        * @return 调用该方法的对象本身，用于链式调用
                                                                                                                                                                                        */
                                                                                                                                                                                        public Result<T> properties(String property) {
                                                                                                                                                                                            if (property != null && !property.isEmpty()) {
                                                                                                                                                                                            addProperties(Collections.singletonList(property));
                                                                                                                                                                                            }
                                                                                                                                                                                            return this;
                                                                                                                                                                                            }

                                                                                                                                                                                            /**
                                                                                                                                                                                            * 内部方法，用于向属性列表中添加元素
                                                                                                                                                                                            * <p>
                                                                                                                                                                                                * 如果当前属性列表为空，则初始化一个新的列表。
                                                                                                                                                                                                * </p>
                                                                                                                                                                                            *
                                                                                                                                                                                            * @param properties 需要添加的属性列表
                                                                                                                                                                                            */
                                                                                                                                                                                            private void addProperties(List<String> properties) {
                                                                                                                                                                                                if (this.properties == null) {
                                                                                                                                                                                                this.properties = new ArrayList<>();
                                                                                                                                                                                                }
                                                                                                                                                                                                this.properties.addAll(properties);
                                                                                                                                                                                                }

                                                                                                                                                                                                /**
                                                                                                                                                                                                * 设置对象 {@code timestamp} 属性，并返回本对象
                                                                                                                                                                                                * @param timestamp 时间戳
                                                                                                                                                                                                * @return 调用该方法的对象
                                                                                                                                                                                                */
                                                                                                                                                                                                public Result<T> timestamp(Date timestamp) {
                                                                                                                                                                                                    this.timestamp = timestamp;
                                                                                                                                                                                                    return this;
                                                                                                                                                                                                    }

                                                                                                                                                                                                    }
