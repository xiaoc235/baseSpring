package com.common.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value = 9)
public final class ToolSpring implements ApplicationContextAware {
	private static ApplicationContext applicationContext = null;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		if(ToolSpring.applicationContext == null){
			ToolSpring.applicationContext  = applicationContext;
		}
		
	}
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public static Object getBean(String name){
		return getApplicationContext().getBean(name);
	}

}
