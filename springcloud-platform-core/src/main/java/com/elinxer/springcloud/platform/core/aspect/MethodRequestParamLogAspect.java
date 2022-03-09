package com.elinxer.springcloud.platform.core.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
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
