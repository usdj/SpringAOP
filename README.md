# 写一个自己的SpringAOP
纸上得来终觉浅，看了不少Spring AOP的实现和源码，还不如自己动手写一个SpringAOP来得更深刻理解。
首先AOP是什么，AOP为面向切面编程，作为一种编程思想。Java作为一种面向对象(OOP)的编程语言，自然也存在不足之处。当需要给相互不具体继承关系的对象引入共用方法的时候，就得在每个对象引用公共的方法，这样产生大量的冗余代码，为此AOP可以解决OOP这一问题。
Spring AOP 通过反向动态代理实现的，为此需要理解Java中的代理原理和实现。
## 代理模式
代理模式：为其他对象提供一种代理以控制对这个对象的访问。就好比你要卖房，你会通过中介替你完成，也就是一种代理。代理模式可分为静态代理和动态代理两种。
## 静态代理
静态代理：就是在程序运行前，代理类和原始类已经编译确定好了。
 1. 定义需要实现的接口
 ```java
 public interface IUserDao {
	/**
	 * save method
	 */
	void save();

	/**
	 * find method
	 */
	void find();
}
 ```

2.实现定义的接口，也就原始类
```java
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
```
3. 实现静态代理类，要求1目标对象必须已经实现接口，2代理对象必须实现和目标对象相同的接口
```java
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
```
4. 编写测试类，测试静态代理
```java
public class TestStaticProxy {
	public static void main(String[] args) {
		IUserDao proxy = new UserDaoProxy();
		proxy.save();
		System.out.println("==============");
		proxy.find();
	}
}
```

测试结果：
![static](https://github.com/usdj/BlogImage/blob/master/SpringAOP/StaticProxyResult.png
静态代理优势在于编写业务类只需要关注业务逻辑，代理对象实现的接口只服务于一种类型的对象。
不足：当增加一个方法时，不但实现类并且代理类也需要实现此方法，导致代码重复高。为此可以通过动态代理改善

## 动态代理
动态代理：程序在jvm运行的时候动态反射机制动态生成，代理类和原始类按需产生。
1. 定义接口
```java
public interface IUserDao {
	/**
	 * save method
	 */
	void save();
	/**
	 * find method
	 */
	void find();
}
```
2. 原始类，实现接口方法
```java
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
```
3. 编写动态代理类，通过代理工厂实现，能够为多个原始类在运行时生成打击类对象
```java
public class ProxyFactory {
	private Object target;

	public ProxyFactory(Object target) {
		this.target = target;
	}

	public Object getProxyInstance() {
		Object proxy = Proxy.newProxyInstance(
				target.getClass().getClassLoader(),
				target.getClass().getInterfaces(),
				new InvocationHandler() {   //在执行代理类对象方法时触发
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
```
4. 编写测试类
```java
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
```
测试结果：
![image](https://github.com/usdj/BlogImage/blob/master/SpringAOP/DynamicProxyResult.png)
在运行测试类中创建测试类对象代码中

```java
IUserDao proxy = (IUserDao)new ProxyFactory(target).getProxyInstance();
```
其实是JDK动态生成了一个类去实现接口,隐藏了这个过程:
```java
class $jdkProxy implements IUserDao{}
```
使用jdk生成的动态代理的前提是目标类必须有实现的接口。但这里又引入一个问题,如果某个类没有实现接口,就不能使用JDK动态代理,所以Cglib代理就是解决这个问题的。

Cglib是以动态生成的子类继承目标的方式实现，在运行期动态的在内存中构建一个子类，如下:
```java
public class UserDao{}
//Cglib是以动态生成的子类继承目标的方式实现,程序执行时,隐藏了下面的过程
public class $Cglib_Proxy_class  extends UserDao{}
```
Cglib使用的前提是目标类不能为final修饰。因为final修饰的类不能被继承。
现在，我们可以看看AOP的定义：面向切面编程，核心原理是使用动态代理模式在方法执行前后或出现异常时加入相关逻辑。

通过定义和前面代码我们可以发现3点：
1.AOP是基于动态代理模式。
2.AOP是方法级别的。    
3.AOP可以分离业务代码和关注点代码（重复代码），在执行业务代码时，动态的注入关注点代码。切面就是关注点代码形成的类。

## Spring AOP
动态代理的实现有JDK代理和Cglib代理两种来实现，Spring对于AOP的实现结合了两种动态代理模式的。
首先创建对象的时候根据切入点和表达式拦截类生成代理对象，如果目标对象也就是原始类有实现接口，则使用JDK代理。没有实现接口的话，则使用Cglib代理。从容器中获得代理对象后，在运行时加载切面类的方法，可查看DefaultAopProxyFactory类源码。
**如果目标类没有实现接口，且class为final修饰的，则不能进行Spring AOP编程！**

1. 定义接口
```java
public interface IUserDao {
	/**
	 * save method
	 */
	void save();
	/**
	 * find method
	 */
	void find();

}
```
2. 定义没有实现接口的类,用于测试Cglib的动态代理
```java
public class OrderDao {
	public void save() {
		System.out.println("Cglib: Saving order!");
	}
	public void find() {
		System.out.println("Cglib: finding order!");
	}
}
```
3. 定义实现接口的类，用于测试JDK动态代理
```java
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
```
4. 定义切面类，用于选择切入点和对类方法运行时加载控制
```java
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
```
5. 编写Spring的applicationContext.xml文件
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/aop
        http://www.springframework.org/schema/aop/spring-aop.xsd">
    <!-- dao实例加入容器 -->
    <bean id="userDao" class="com.usdj.springaop.UserDao"/>

    <!-- dao实例加入容器 -->
    <bean id="orderDao" class="com.usdj.springaop.OrderDao"/>

    <!-- 实例化切面类 -->
    <bean id="transactionAop" class="com.usdj.springaop.Transaction"/>

    <!-- Aop相关配置 -->
    <aop:config>
        <!-- 切入点表达式定义 -->
        <aop:pointcut expression="execution(* com.usdj.springaop..*.*(..))" id="transactionPointcut"/>
        <!-- 切面配置 -->
        <aop:aspect ref="transactionAop">
            <!-- 【环绕通知】 -->
            <aop:around method="surround" pointcut-ref="transactionPointcut"/>
            <!-- 【前置通知】 在目标方法之前执行 -->
            <aop:before method="beginTransaction" pointcut-ref="transactionPointcut"/>
            <!-- 【后置通知】 -->
            <aop:after method="afterTransaction" pointcut-ref="transactionPointcut"/>
            <!-- 【返回后通知】 -->
            <aop:after-returning method="afterReturing" pointcut-ref="transactionPointcut"/>
            <!-- 异常通知 -->
            <aop:after-throwing method="afterThrowing" pointcut-ref="transactionPointcut"/>
        </aop:aspect>
    </aop:config>
</beans>
```
6. 编写测试类
```java
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
```
测试结果1 jdk:

![1](https://github.com/usdj/BlogImage/blob/master/SpringAOP/jdk.png)
测试结果2 cglib:
![2](https://github.com/usdj/BlogImage/blob/master/SpringAOP/cglib.png)

## Ref

[Spring AOP切入点表达式](https://blog.csdn.net/keda8997110/article/details/50747923)