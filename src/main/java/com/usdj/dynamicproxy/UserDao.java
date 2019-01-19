package com.usdj.dynamicproxy;

/**
 * @program: SpringAOP
 * @description: It uses to implement IUserDao
 * @author: JerryDeng
 * @create: 2019-01-19 16:38
 **/
public class UserDao implements IUserDao {
	@Override
	public void save() {
		System.out.println("Save user done!");
	}

	@Override
	public void find() {
		System.out.println("Find user done!");
	}
}
