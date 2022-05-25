package org.annotations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component("Student")
public class Student {

    private int id;
    private String name;
    @Autowired
    private List<Phone> ph;
    @Autowired
    private Address add;

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

    public void setPh(Phone ph) {
        if(this.ph.get(0).getMob() == null) this.ph.remove(0);
        this.ph.add(ph);
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
