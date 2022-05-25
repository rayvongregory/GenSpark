package org.javaBeans;

import org.springframework.stereotype.Component;

public class Phone {
    private String mob;

    public Phone() {
        // no-args constructor
    }

    public Phone(String mob) {
        this.mob = mob;
    }

    public String getMob() {
        return mob;
    }

    public void setMob(String mob) {
        this.mob = mob;
    }
}
