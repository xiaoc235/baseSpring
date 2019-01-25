package com.common.spring.utils;

import com.common.base.exception.BusinessException;
import com.common.utils.FileTxtUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * Created by jianghaoming on 17/2/28.
 */
public class CommonUtils extends com.common.utils.CommonUtils {

    private CommonUtils(){
        super();
    }


    public static boolean isBlank(Object param){
        return ObjectUtils.isEmpty(param);
    }


    /**
     * 保存文件
     * @param file
     * @param filePath
     * @param fileName
     */
    public static File saveFile(MultipartFile file , String filePath, String fileName) {
        BufferedOutputStream stream = null;
        File filet = null;
        try {
            FileTxtUtils.mkdir(filePath);
            byte[] bytes = file.getBytes();
            filet = new File(filePath + fileName);
            stream = new BufferedOutputStream(new FileOutputStream(filet));
            stream.write(bytes);
            return filet;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return filet;
    }


    /**
     * 获取map的参数值
     * @param key
     * @return
     */
    public static String getMapParam(final String key, Map<String,Object> paramMap){
        if (paramMap == null || paramMap.isEmpty() || isBlank(key)
                || !paramMap.containsKey(key)) {
            return null;
        }
        Object obj = paramMap.get(key);
        if (obj == null) {
            return null;
        }
        String value = obj.toString();
        return value.trim();
    }


    /**
     * 获取map的参数值，并验证是否为空
     * @param key
     * @return
     */
    public static String getMapParamAndCheckNull(final String key, Map<String,Object> paramMap) throws BusinessException {
        final String param = getMapParam(key,paramMap);
        CheckUtils.checkParamNull(param,key+"不能为空");
        return param;
    }

    /**
     * 获取map的参数值，并验证是否为空
     * @param key
     * @return int
     */
    public static Integer getMapParamIntAndCheckNull(final String key, Map<String,Object> paramMap) throws BusinessException {
        final String param = getMapParamAndCheckNull(key,paramMap);
        return Integer.valueOf(param);
    }




    public static String getIpAddr(HttpServletRequest request) {
        String[] clientHeader = {"Proxy-Client-IP","WL-Proxy-Client-IP","HTTP_CLIENT_IP",
        "HTTP_X_FORWARDED_FOR","X-Real-IP"};

        String ip = request.getHeader("x-forwarded-for");
        if (!CommonUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if(ip.contains(",")){
                ip = ip.split(",")[0];
            }
        }
        for(String header : clientHeader){
            ip = request.getHeader(header);
            if(!CommonUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)){
                break;
            }
        }
        return ip;
    }

}
