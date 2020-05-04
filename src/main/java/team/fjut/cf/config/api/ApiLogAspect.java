package team.fjut.cf.config.api;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import team.fjut.cf.utils.DateUtils;
import team.fjut.cf.utils.IpUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Date;

/**
 * AOP切片类
 * 目前仅打印Controller和异常处理的方法
 *
 * @author axiang [2019/10/22]
 */
@Aspect
@Component
@Slf4j
public class ApiLogAspect {
    @Value("${cf.config.controllerLog.enable}")
    private boolean controllerLogEnable;

    /**
     * 切点为controller层的所有方法，  ..表示包和子包
     */
    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.GetMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PostMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PutMapping)")
    public void controllerMethod() {
    }


    @Around("controllerMethod()")
    public Object logAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        //业务发生时间
        Date serviceHappenDate = new Date();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        //获取调用类信息
        Object target = proceedingJoinPoint.getTarget().getClass().getName();
        //获取调用方法信息
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = signature.getMethod();
        //获取参数类型
        StringBuilder params = new StringBuilder();
        Parameter[] parameters = method.getParameters();
        if (parameters != null && parameters.length > 0) {
            for (Parameter parameter : parameters) {
                params.append("[").append(parameter.getType().getName()).append("]").append(parameter.getName()).append(" ");
            }
        }
        //返回对象
        Object o = null;
        Object[] args = proceedingJoinPoint.getArgs();
        if (controllerLogEnable) {
            String s = "\n===================== 进入请求 =====================\n" +
                    String.format("== 【业务发生时间】:%s\n", DateUtils.formatDate(serviceHappenDate, "yyyy-MM-dd hh:mm:ss")) +
                    String.format("== 【请求URL】:%s\n", request.getRequestURL().toString()) +
                    String.format("== 【请求方法】:%s\n", request.getMethod()) +
                    String.format("== 【请求IP】:%s\n", IpUtils.getClientIpAddress(request)) +
                    String.format("== 【请求类】:%s\n", target) +
                    String.format("== 【调用方法】:%s\n", method.getName()) +
                    String.format("== 【方法参数】:%s\n", params.toString()) +
                    String.format("== 【传入数据】:%s\n", Arrays.toString(args)) +
                    "===================================================\n";
            log.info(s);
        }
        //计算执行时间
        long startTime = System.currentTimeMillis();
        long endTime = 0;

        if (args.length > 0) {
            o = proceedingJoinPoint.proceed(args);
        } else {
            o = proceedingJoinPoint.proceed();
        }
        endTime = System.currentTimeMillis();

        if (controllerLogEnable) {
            String s = "\n===================== 结束请求 =====================\n" +
                    String.format("== 【业务发生时间】:%s\n", DateUtils.formatDate(serviceHappenDate, "yyyy-MM-dd hh:mm:ss")) +
                    String.format("== 【响应耗时】:%s\n", endTime - startTime < 0 ? "" : (endTime - startTime) + "MS") +
                    String.format("== 【响应内容】:%s\n", o == null ? "" : o.toString()) +
                    "===================================================\n";
            log.info(s);
        }
        return o;

    }


}
