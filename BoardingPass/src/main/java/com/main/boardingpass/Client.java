package com.main.boardingpass;

public class Client {

    private final String name;
    private final int age;
    private final String email;
    private final String phoneNumber;
    private final char gender;

    public Client (String name, int age, String email, String phoneNumber, char gender) {
        this.name = name;
        this.age = age;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public char getGender() {
        return gender;
    }

}
