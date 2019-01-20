package com.usdj.proxy;

import com.usdj.staticproxy.IUserDao;
import com.usdj.staticproxy.UserDaoProxy;

/**
 * @program: SpringAOP
 * @description: TestStaticProxy
 * @author: JerryDeng
 * @create: 2019-01-19 16:32
 **/
public class TestStaticProxy {
	public static void main(String[] args) {
		IUserDao proxy = new UserDaoProxy();
		proxy.save();
		System.out.println("==============");
		proxy.find();
	}
}
