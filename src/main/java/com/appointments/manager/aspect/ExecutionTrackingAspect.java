package com.appointments.manager.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExecutionTrackingAspect {

    private static final Logger logger = LoggerFactory.getLogger(ExecutionTrackingAspect.class);

    @Pointcut("@annotation(TrackExecution)")
    public void trackExecutionPointcut() {}

    @Around("trackExecutionPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        long start = System.currentTimeMillis();

        String methodName = joinPoint.getSignature().toShortString();
        String username = getCurrentUsername();

        logger.info("User [{}] started method {}", username, methodName);

        try {
            Object result = joinPoint.proceed();

            long duration = System.currentTimeMillis() - start;
            logger.info("Method {} completed in {} ms", methodName, duration);

            return result;

        } catch (Exception ex) {
            logger.error("Method {} failed: {}", methodName, ex.getMessage());
            throw ex;
        }
    }

    private String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (auth != null) ? auth.getName() : "ANONYMOUS";
    }
}

