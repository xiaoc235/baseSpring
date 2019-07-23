package com.common.spring;

import org.springframework.context.annotation.ComponentScan;


@ComponentScan(basePackages={"com.common.dao","com.common.redis","com.common.redis.*","com.common.spring"})
public class SpringUtilConfig {

}
