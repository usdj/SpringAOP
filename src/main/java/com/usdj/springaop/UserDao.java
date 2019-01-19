package com.usdj.springaop;

/**
 * @program: SpringAOP
 * @description: Test jdk dynamic proxy,this class implements IUserDao interface
 * @author: JerryDeng
 * @create: 2019-01-19 17:07
 **/
public class UserDao implements IUserDao {
	@Override
	public void save() {
		System.out.println("Jdk Proxy: Saving order!");
	}

	@Override
	public void find() {
		System.out.println("Jdk Proxy: Finding order!");
	}
}
