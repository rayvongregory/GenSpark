package org.annotationsHybrid;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App
{
    public static void main( String[] args ) {

        AbstractApplicationContext context = new ClassPathXmlApplicationContext("annotationsHybrid.xml");
        // gets the Student bean defined in the Student class with the @Component annotation
        Student student = (Student) context.getBean("Student");
        student.setId(720481121);
        student.setName("Rayvon Gregory");
        System.out.println(student);
    }
}
