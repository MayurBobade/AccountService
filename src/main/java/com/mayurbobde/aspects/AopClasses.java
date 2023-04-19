package com.mayurbobde.aspects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AopClasses {
   
	private static final Logger logger = LogManager.getLogger(AopClasses.class.getName());

   
  @Before("execution(* com.mayurbobde.controller.AccountsController.*(..))")
  public void logBeforeV2(JoinPoint joinPoint) 
  {
    System.out.println("AopClasses.logBeforeV2() : " + joinPoint.getSignature().getName());
    logger.debug("debug Before log: "+ joinPoint.getSignature().getName());
    logger.info("info Before log: "+ joinPoint.getSignature().getName());
    logger.warn("warn Before log: "+ joinPoint.getSignature().getName());
    logger.error("error Before log: "+ joinPoint.getSignature().getName());
  }
   
   
  @After("execution(* com.mayurbobde.controller.AccountsController.*(..))")
  public void logAfterV2(JoinPoint joinPoint) 
  {
    System.out.println("AopClasses.logAfterV2() : " + joinPoint.getSignature().getName());
    logger.debug("debug After log: "+ joinPoint.getSignature().getName());
    logger.info("info After log: "+ joinPoint.getSignature().getName());
    logger.warn("warn After log: "+ joinPoint.getSignature().getName());
    logger.error("error After log: "+ joinPoint.getSignature().getName());
  }
}