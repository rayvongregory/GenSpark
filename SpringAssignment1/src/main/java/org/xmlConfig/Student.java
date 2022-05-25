package org.xmlConfig;

import java.util.Arrays;
import java.util.List;

public class Student {

    private int id;
    private String name;
    private List<Phone> ph;
    private Address add;

    public Student() {
        // no args constructor for setter injection
        // the Student bean is set up to take in an id, name, ph, and add using the property tag
        // so we use setter injection with a no-args constructor
    }

    public Student(int id, String name, List<Phone> ph, Address add) {
        this.id = id;
        this.name = name;
        this.ph = ph;
        this.add = add;
        // args constructor for constructor injection
        // very similar as the above but the values are injected using the constructor-args tag
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Phone> getPh() {
        return ph;
    }

    public void setPh(List<Phone> ph) {
        this.ph = ph;
    }

    public Address getAdd() {
        return add;
    }

    public void setAdd(Address add) {
        this.add = add;
    }

    public String toString() {
        return String.format("Name: %s%nStudent Id: %s%nPhone Numbers: %s%n%s", name, id, Arrays.toString(Arrays.stream(ph.toArray(Phone[]::new)).map(q->q.getMob()).toArray(String[]::new)), add.toString());
    }
}
