package org.javaBeans;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App 
{
    public static void main( String[] args ) {

        ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        Student student = context.getBean(Student.class);
        student.setName("Rayvon Gregory");
        student.setId(720481121);
        student.setPh(new Phone("860-XXX-XXXX"));
        student.setPh(new Phone("860-XXX-XXXX"));
        student.setAdd(new Address("Hartford", "CT", "US", "06105"));
        System.out.println(student);
    }
}
