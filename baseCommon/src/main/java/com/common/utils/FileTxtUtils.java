package com.common.utils;

import com.common.base.CommConstants;
import com.common.base.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Paths;

public class FileTxtUtils {

    private FileTxtUtils(){

    }

    private static final Logger _logger = LoggerFactory.getLogger(FileTxtUtils.class);

    /**
     * 保存文本文件
     *
     * @user jianghaoming
     * @date 2017/11/3  下午4:49
     */
    public static void saveTXTFileByStr(String txt, String filePath, String fileName) throws BusinessException {
        _logger.info("save txt.size = {}, filePath = {}, fileName = {}",txt.length(), filePath, fileName);
        File f = new File(filePath);
        if (!f.exists()) {
            f.mkdirs();
        }
        try (BufferedReader bufReader = new BufferedReader(new StringReader(txt));
             BufferedWriter bufWriter =  new BufferedWriter(new FileWriter(filePath + fileName))){
            String temp = null;
            while ((temp = bufReader.readLine()) != null) {
                bufWriter.write(temp);
            }
            _logger.info("保存成功, {}", fileName);
        } catch (Exception e) {
            _logger.error(e.getMessage(), e);
            throw new BusinessException(CommConstants.OPERATOR_FAIL);
        }
    }


    public static String readTXTFileByFile(String filePath) throws BusinessException {
        try (FileReader fileReader =  new FileReader(filePath);
              BufferedReader br = new BufferedReader(fileReader)){
            _logger.info("filePath = {}" , filePath);
            String str = null;
            StringBuilder result = new StringBuilder();
            _logger.info("开始解析文本");
            while ((str = br.readLine()) != null) {
                result.append(str.replaceAll("",""));
            }
            return result.toString();
        } catch (Exception e) {
            _logger.error(e.getMessage(), e);
            throw new BusinessException(CommConstants.OPERATOR_FAIL);
        }
    }


    public static void mkdir(String filePath) throws BusinessException {
        File file = Paths.get(filePath).toFile();
        if(!file.exists()){
            if(!file.mkdir()){
                throw new BusinessException("创建文件夹失败:{}" + file.getAbsolutePath());
            }
        }
    }
}
