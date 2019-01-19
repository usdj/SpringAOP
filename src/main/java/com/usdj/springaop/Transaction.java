package com.usdj.springaop;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * @program: SpringAOP
 * @description: Config AOP methods
 * @author: JerryDeng
 * @create: 2019-01-19 17:11
 **/
public class Transaction {
	public void beginTransaction() {
		System.out.println("[Before] Open transaction...");
	}
	public void afterTransaction() {
		System.out.println("[After] Commit transaction...");
	}
	public void afterReturing() {
		System.out.println("[Return] After returning..");
	}
	public void afterThrowing() {
		System.out.println("[Error] Error...");
	}
	public void surround(ProceedingJoinPoint pjp) throws Throwable{
		System.out.println("[Before surround]...");
		pjp.proceed();
		System.out.println("[After surround]...");
	}
}
