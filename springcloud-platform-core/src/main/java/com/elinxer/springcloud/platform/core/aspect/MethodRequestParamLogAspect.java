package com.elinxer.springcloud.platform.core.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 由于此类使用了Component用于随项目初始化
 * 但本包名并没有在springboot扫描下，需要手动添加
 * 默认情况下，@ComponentScan注解会扫描当前包及其所有子包中的组件。
 * 而 @SpringBootApplication 注解包含了@ComponentScan，
 * 所以 Spring Boot 框架会自动扫描 Spring Boot启动类当前包及其所有子包中的组件类。
 * 而我们的组件因为不在自动扫描范围内，所以无效。
 * 在添加扫描时注意别忘记添加自己的
 * <p>
 * 如@ComponentScan(value = {"com.example", "com.elinxer.springcloud.platform.core"})
 *
 * @author elinx
 */
@Slf4j
@Aspect
@Component
public class MethodRequestParamLogAspect {

    /**
     * 定义一个切点
     * 所有被GetMapping注解修饰的方法会织入advice
     * 可以直接织入主定义注解，可以在使用时才生效
     */
    @Pointcut("@annotation(com.elinxer.springcloud.platform.core.annotation.MethodRequestParamLog)")
    private void customPointcut() {
    }

    /**
     * 切点处理前
     * JoinPoint和ProceedingJoinPoint对象
     * JoinPoint对象封装了SpringAop中切面方法的信息,在切面方法中添加JoinPoint参数,就可以获取到封装了该方法信息的JoinPoint对象
     * ProceedingJoinPoint对象是JoinPoint的子接口,该对象只用在@Around的切面方法中
     */
    @Before("customPointcut()")
    public void customAdvice(JoinPoint joinPoint) {
        log.info("MethodRequestParamLog: class.method={}",
                joinPoint.getSignature().getDeclaringType().getSimpleName()
                        + "." + joinPoint.getSignature().getName()
        );
        log.info("MethodRequestParamLog: args.size={}", joinPoint.getArgs().length);
        if (joinPoint.getArgs() != null && joinPoint.getArgs().length >= 1) {
            for (Object o : joinPoint.getArgs()) {
                log.info("MethodRequestParamLog: args.param={}", o);
            }
        }
    }

}
