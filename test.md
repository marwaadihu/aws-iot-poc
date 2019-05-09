## Java

*  OOPS Concepts
   * Abstraction
   * Polymorphism
     * Overloading Overriding
     * static Binding dynamic binding
     * static method overloading
   * Encapsulation
   * Inheritance
     * Multiple Inheritance
     * Dimond problem
*  Access specifiers
*  JDK,JRE and JVM
*  Initial value of Object reference
*  Initial value of int, boolean
*  Use of this keyword
*  Can I chain the constructors?
*  Use of final, finally and finalize?
*  Can I declare an Interface as final?
*  Exception
   * Checked Exception
     - classes that extends Throwable class, except
         RuntimeException and Error are CheckedException,
         IOException, SQLException

   * Unchecked Exception
     - classes that extends RuntimeException as unchecked exception, ArithmeticException, NullPointerException
    * Can I create my own customer Exception?

    * ClassNotFoundException vs NoClassDefFoundError

*  Is it necessary that each try block must be followed by catch block?

* String:-
  * What is String pool?
      - space reserved in heap memory that can be used to store strings
  * What is immutable object?
  * Apart from String what are the other immutable ojects?
  * Difference between String and StringBuilder
* What is a nested class? 
  - Class that is defined inside another class
  - It can access all the members of outer class including private members

*  What are the wrapper classes in java?

* What is garbage collection?
* How can I make an object applicable for garbage collection?
* How can I make an object unreferenced?
  - By nulling the reference
  - By assigning the reference to another

*  How many ways you can create a thread?
*  How to control number of thread creation
   > Thread pool
*  Execute thread in sequence one after another
   > thread.join()

*  What is the difference between concurrency and parallelism?
   * Concurrency means multiple tasks which start, run and complete in overlapping time periods
   * Parallelism is when multiple tasks literally run at same time


* Collections
  * Array
  * ArrayList
  * LinkedList
  * HashSet
  * HashMap
    * How hashmap works?
    * What is Hash collision in HashMap
    * Importance of hashCode() and  equals() method 
    * return type of .put method in java

*  Comparable and Comparator interfaces

*  Design Patterns 
   * Singleton
   * Factory
   * Prototype
   * Observer

## Servlet

* Servlet life cycle
* Difference between web server and application server
* What is ServletConfig?
* What is ServletContext?
* What is RequestDispatcher
  * forward: 
  * include
* What is a Filter?
* Difference between a Cookie and HttpSession


## Spring
* What is dependency Injection
* How to implement DI in Spring
* Spring Modules:
   * Spring Context
   * Spring AOP
   * Spring DAO
   * SPring JDBC
   * SPring ORM
   * Spring MVC
* AOP
  * Aspect
  * Advice
  * Pointcut
  * Join point
  * Advice Arguments
  
* What is Spring IOC Container?
* What is Spring Bean
* What is Interceptor?
* What are the different ways to configure a bean?
	* XML configuration
			<bean name="myBean" class="com.ei.SomeClass"></bean>
	* Java based Configuration

	`
	@Bean 
	`

		
	```
	@Configuration
		@ComponentScan(value="com.ei.main")
		public class MyConfiguration {
			@Bean
			public MyService getService() {
				return new MyService();
			}
		}
	```
	```
	AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(MyConfigurtion.class);
	MyService service = ctx.getBean(MyService.class);
	```

	* Annotation based
		* @Component
		* @Service
		* @Repository
		* @Controller
			
* What is a stereotype in Spring

* What are the different scopes in Spring?
  1. singleton
  2. prototype
  3. request
  4. session
  5. global-session


## Hibernate

* Session
* SessionFactory
* Lazy Loading ?

  
 

