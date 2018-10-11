package com.xc;

import com.common.base.CommConstants;
import com.common.base.exception.BusinessException;
import com.common.spring.BaseController;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExceptionTest {

    private static final Logger _logger = LoggerFactory.getLogger(BaseController.class);

    @Test
    public void test1(){
        try {
            throwBusiness();
        }catch (BusinessException ex) {
            final String message = ex.getErrorDesc();
            _logger.error(CommConstants.BUSINESS_ERROR + " " + message, ex);
        }catch (Exception e) {
            final String message = CommConstants.SYSTEM_ERROR;
            _logger.error(message, e);
        }
    }

    private void throwBusiness() throws BusinessException{
       throw new BusinessException("fdfdffd");
    }
}
