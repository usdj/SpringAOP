package com.usdj.staticproxy;

/**
 * @program: SpringAOP
 * @description: StaticProxy, target object must implements at leat one class and without final tag.And proxy object should implements same interface as target object
 * @author: JerryDeng
 * @create: 2019-01-19 16:27
 **/
public class UserDaoProxy implements IUserDao {
	private IUserDao target = new UserDao();
	@Override
	public void save() {
		System.out.println("StaticProxy: open operation...");
		target.save();
		System.out.println("StaticProxy: commit operation...");
	}

	@Override
	public void find() {
		System.out.println("StaticProxy: open operation...");
		target.find();
		System.out.println("StaticProxy: commit operation...");
	}
}
