package org.annotations;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App
{
    public static void main( String[] args ) {

        AbstractApplicationContext context = new ClassPathXmlApplicationContext("annotations.xml");
        // gets the Student bean defined in the Student class with the @Component annotation
        Student student = (Student) context.getBean("Student");
        student.setName("Rayvon Gregory");
        student.setId(720481121);
        student.setPh(new Phone("860-XXX-XXXX"));
        student.setPh(new Phone("860-XXX-XXXX"));
        student.setAdd(new Address("Hartford", "CT", "US", "06105"));
        System.out.println(student);

    }
}
