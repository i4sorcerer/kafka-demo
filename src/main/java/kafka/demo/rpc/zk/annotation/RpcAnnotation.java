package kafka.demo.rpc.zk.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * rpc服务注册注解类
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcAnnotation {
    /**
     * 服务名称
     * @return
     */
    Class<?> name();

    /**
     * 服务版本号
     * @return
     */
    String version() default "1.0";

}
