package max.iv.tz_convenient_software.aop;

import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {


    @Pointcut("execution(public * max.iv.tz_convenient_software.services.XlsxServiceInterface+.*(..))")
    public void xlsxServiceMethodsPointcut() {
        // Тело метода остается пустым - он служит только для определения Pointcut
    }

    public void xlsxServiceMethods() {}
    @Around("xlsxServiceMethodsPointcut()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch(joinPoint.getSignature().toShortString()); // Имя StopWatch = сигнатура метода
        String methodName = joinPoint.getSignature().getName();
        Object[] methodArgs = joinPoint.getArgs();

        log.info("Начало выполнения: {} с аргументами: {}", methodName, Arrays.toString(methodArgs));
        stopWatch.start();

        Object result;
        try {
            result = joinPoint.proceed(); // Выполняем оригинальный метод
        } catch (Throwable throwable) {
            log.error("Исключение в методе {}: {}", methodName, throwable.getMessage());
            throw throwable; // Перебрасываем исключение дальше
        } finally {
            stopWatch.stop();
            log.info("Завершение выполнения: {} | Время выполнения: {} мс", methodName, stopWatch.getTotalTimeMillis());
        }
        return result;
    }
}
