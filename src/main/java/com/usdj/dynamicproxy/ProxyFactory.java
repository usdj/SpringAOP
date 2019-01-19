package com.usdj.dynamicproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @program: SpringAOP
 * @description: Create a proxy factory to generate proxy object for target object
 * @author: JerryDeng
 * @create: 2019-01-19 16:40
 **/
public class ProxyFactory {
	private Object target;

	public ProxyFactory(Object target) {
		this.target = target;
	}

	public Object getProxyInstance() {
		Object proxy = Proxy.newProxyInstance(
				target.getClass().getClassLoader(),
				target.getClass().getInterfaces(),
				new InvocationHandler() {
					@Override
					public Object invoke(Object proxy, Method method, Object[] args) {
						String methodName = method.getName();
						Object result;
						try{
							System.out.println("DynamicProxy: open operation...");
							System.out.println("MethodName:" + methodName);
							result = method.invoke(target, args);
							System.out.println("DynamicProxy: commit operation...");
						} catch (Exception e) {
							throw new RuntimeException("Method not found!");
						}
						return result;
					}
				});
				return proxy;
	}
}
