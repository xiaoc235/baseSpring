package com.common.spring;

import com.common.spring.utils.CommonUtils;
import com.common.utils.GsonUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jianghaoming on 17/5/10.
 */
@Aspect
@Component
public class WebLogAspect {

    private static final Logger _logger = LoggerFactory.getLogger(WebLogAspect.class);

    private static final String WRAN_LINE_SIGN = "\r\n";

    private static final String LINE = "--------------------------------------------------------------------------------------------------";

    private static final String application_json = "application/json";

    /**
     *
     第一个* 表示任意返回值类型
     第二个* 表示以任意名字开头的package. 如 com.xx.
     第三个* 表示以任意名字开头的class的类名 如TestService
     第四个* 表示 通配 *service下的任意class
     最后二个.. 表示通配 方法可以有0个或多个参数
     */
    @Pointcut("execution(public * *..*Controller.*(..))")
    public void webLog(){}



    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        if(request.getRequestURI().toLowerCase().endsWith("/error")){
            return;
        }

        StringBuilder requestStr = new StringBuilder("request ==> ");
        requestStr.append(WRAN_LINE_SIGN);
        requestStr.append(LINE);
        requestStr.append(WRAN_LINE_SIGN);
        requestStr.append("IP : ").append(CommonUtils.getIpAddr(request));
        requestStr.append(WRAN_LINE_SIGN);
        requestStr.append("URL : ").append(request.getRequestURL().toString());
        requestStr.append(WRAN_LINE_SIGN);
        requestStr.append("Method : ").append(request.getMethod());
        requestStr.append(WRAN_LINE_SIGN);
        requestStr.append("ContentType : ").append(request.getContentType());
        requestStr.append(WRAN_LINE_SIGN);
        requestStr.append("Class_Method : ").append(joinPoint.getSignature().getDeclaringTypeName()).append(".").append(joinPoint.getSignature().getName());
        requestStr.append(WRAN_LINE_SIGN);
        requestStr.append("Url args : ");
        Enumeration<String> enu = request.getParameterNames();
        while(enu.hasMoreElements()){
            String paraName = enu.nextElement();
            requestStr.append(" ").append(paraName).append(" = ").append(request.getParameter(paraName));
        }
        requestStr.append(WRAN_LINE_SIGN);
        Map<String,Object> header = new HashMap<>();
        Enumeration<String> requestHeader = request.getHeaderNames();
        while(requestHeader.hasMoreElements()){
            String key = requestHeader.nextElement();
            if(key.equals("host") || key.equals("user-agent") ||
                    key.equalsIgnoreCase("userid") || key.toLowerCase().contains("token")
                || key.toLowerCase().contains("appid")) {
                String value = request.getHeader(key);
                header.put(key, value);
            }
        }
        requestStr.append("Header args : ").append(header.toString());
        if(request.getDispatcherType().name().equalsIgnoreCase("ERROR")){
            _logger.info(requestStr.toString());
            return;
        }
        requestStr.append(WRAN_LINE_SIGN);
        if(joinPoint.getArgs()!=null) {
            requestStr.append("Body args : ");
            if(request.getContentType() !=null && request.getContentType().contains(application_json)) {
                requestStr.append(GsonUtils.toJson(joinPoint.getArgs()));
            }else{
                for (int i = 0; i < joinPoint.getArgs().length; i++) {
                    if(joinPoint.getArgs()[i]!=null) {
                        requestStr.append(joinPoint.getArgs()[i]).append("  ");
                    }
                }
            }
            requestStr.append(WRAN_LINE_SIGN);
        }
        requestStr.append(LINE);
        _logger.info(requestStr.toString());


    }

    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) {
        if(ObjectUtils.isEmpty(ret)){
            return;
        }

        // 处理完请求，返回内容
        StringBuilder responseStr = new StringBuilder("response ==> ");
        responseStr.append(WRAN_LINE_SIGN);
        responseStr.append(LINE);
        responseStr.append(WRAN_LINE_SIGN);
        try {
            responseStr.append(GsonUtils.toJson(ret));
        }catch (Exception e){
            responseStr.append(ret);
        }
        responseStr.append(WRAN_LINE_SIGN);
        responseStr.append(LINE);
        if(responseStr.toString().length() > 3000){
            _logger.info("{} ......", responseStr.toString().substring(0,2990));
        }else {
            _logger.info(responseStr.toString());
        }
    }

}