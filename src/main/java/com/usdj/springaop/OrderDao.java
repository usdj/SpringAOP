package com.usdj.springaop;

/**
 * @program: SpringAOP
 * @description: Test a class without implementing interface,and it uses Cglib to achive dynamic proxy
 * @author: JerryDeng
 * @create: 2019-01-19 17:05
 **/
public class OrderDao {
	public void save() {
		System.out.println("Cglib: Saving order!");
	}
	public void find() {
		System.out.println("Cglib: finding order!");
	}
}
