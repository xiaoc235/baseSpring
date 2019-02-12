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
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jianghaoming on 17/5/10.
 */
@Aspect
@Component
@Order(1)
public class WebLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(WebLogAspect.class);

    private static final String WRAN_LINE_SIGN = "\r\n";

    private static final String LINE = "--------------------------------------------------------------------------------------------------";

    private static final String APPLICATION_JSON = "application/json";

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

    //要打印的 request header
    private static final String[]  PRINT_REQUEST_HEADER = {"host","user-agent","userId","appId","token"};

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        if(request.getRequestURI().toLowerCase().endsWith("/error")){
            return;
        }
        StringBuilder requestStr = new StringBuilder(WRAN_LINE_SIGN);
        requestStr.append(LINE);
        requestStr.append(WRAN_LINE_SIGN);
        requestStr.append("request ==> ");
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
            for(String printHeader : PRINT_REQUEST_HEADER){
                if(printHeader.equalsIgnoreCase(key)){
                    String value = request.getHeader(key);
                    header.put(key, value);
                }
            }
        }
        requestStr.append("Header args : ").append(header.toString());
        if("ERROR".equalsIgnoreCase(request.getDispatcherType().name())){
            logger.info("{}",requestStr);
            return;
        }
        requestStr.append(WRAN_LINE_SIGN);
        if(!ObjectUtils.isEmpty(joinPoint.getArgs())) {
            requestStr.append("Body args : ");
            if(request.getContentType() != null && request.getContentType().contains(APPLICATION_JSON)) {
                requestStr.append(GsonUtils.toJson(joinPoint.getArgs()));
            }else{
                for(Object o : joinPoint.getArgs()){
                    if(!ObjectUtils.isEmpty(o)){
                        requestStr.append(o).append("  ");
                    }
                }
            }
            requestStr.append(WRAN_LINE_SIGN);
        }
        requestStr.append(LINE);
        logger.info("{}", requestStr);
    }

    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) {
        if(ObjectUtils.isEmpty(ret)){
            return;
        }

        // 处理完请求，返回内容
        StringBuilder responseStr = new StringBuilder(WRAN_LINE_SIGN);
        responseStr.append(LINE);
        responseStr.append(WRAN_LINE_SIGN);
        responseStr.append("response ==> ");
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
            logger.info("{} ......", responseStr.toString().substring(0,2990));
        }else {
            logger.info(responseStr.toString());
        }
    }

}
