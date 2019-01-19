package com.usdj.staticproxy;

/**
 * @program: SpringAOP
 * @description: Create a user object to implements IUserDao interface
 * @author: JerryDeng
 * @create: 2019-01-19 16:25
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
