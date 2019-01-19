package com.usdj.aop;


import com.usdj.springaop.IUserDao;
import com.usdj.springaop.OrderDao;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @program: SpringAOP
 * @description: TestSpringAOP
 * @author: JerryDeng
 * @create: 2019-01-19 17:50
 **/
public class TestSpringAOP {

	private ApplicationContext ac = new ClassPathXmlApplicationContext(
			"applicationContext.xml");

	@Test
	public void testProxy() {
		IUserDao userDao = (IUserDao) ac.getBean("userDao");
		System.out.println(userDao.getClass());
		userDao.save();
		System.out.println("=================");
		userDao.find();

	}

	@Test
	public void testCglib() {
		OrderDao orderDao = (OrderDao) ac.getBean("orderDao");
		System.out.println(orderDao.getClass());
		orderDao.save();
		System.out.println("================");
		orderDao.find();
	}

}
