package com.usdj.proxy;

import com.usdj.dynamicproxy.IUserDao;
import com.usdj.dynamicproxy.ProxyFactory;
import com.usdj.dynamicproxy.UserDao;

/**
 * @program: SpringAOP
 * @description: TestDynamicProxy
 * @author: JerryDeng
 * @create: 2019-01-19 16:53
 **/
public class TestDynamicProxy {
	public static void main(String[] args) {
		IUserDao target = new UserDao();
		System.out.println("Target Object:" + target.getClass());
		IUserDao proxy = (IUserDao) new ProxyFactory(target).getProxyInstance();
		System.out.println("Proxy Object:" + proxy.getClass());
		proxy.save();
		System.out.println("==================================");
		proxy.find();
	}
}
