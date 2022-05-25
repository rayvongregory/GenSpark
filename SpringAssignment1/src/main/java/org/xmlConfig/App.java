package org.xmlConfig;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App
{
    public static void main( String[] args ) {

        ApplicationContext context = new ClassPathXmlApplicationContext("xml-config.xml");
        Student student = (Student) context.getBean("Student"); // created using setter injection
        Student student1 = (Student) context.getBean("Student1"); // created using constructor injection

        System.out.println(student);
        System.out.println(student1);

    }
}
