package com.common.utils;

import com.common.base.CommConstants;
import com.common.base.exception.BusinessException;
import com.common.base.response.BaseResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;


/**
 * @author jianghaoming
 * @date 2019-02-20 16:20:52
 */
public class NoThrowExceptionUtils {

    private static Logger LOG = LoggerFactory.getLogger(NoThrowExceptionUtils.class);

    @FunctionalInterface
    public interface Method{
        void call() throws BusinessException, SQLException, Exception;
    }

    public static void calls(Method method){
        try {
            method.call();
        }catch (BusinessException ex) {
            final String message = ex.getErrorDesc();
            LOG.info(CommConstants.BUSINESS_ERROR + message,ex);
        }catch (SQLException ex){
            final String message = ex.getMessage();
            LOG.error(CommConstants.DATA_ERROR + message,ex);
        } catch (Exception e) {
            final String message = CommConstants.SYSTEM_ERROR;
            LOG.error(message, e);
        }
    }
}
