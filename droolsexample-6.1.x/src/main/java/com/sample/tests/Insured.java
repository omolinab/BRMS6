package com.sample.tests;

public class Insured {
	 
    private String firstName = null;
 
    private String lastName = null;
 
    private int age = 0;
 
    private String gender = null;
 
    public static final String GENDER_MALE = "M";
    public static final String GENDER_FEMALE = "F";
 
    public String getFirstName() {
        return firstName;
    }
 
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
 
    public String getLastName() {
        return lastName;
    }
 
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
 
    public int getAge() {
        return age;
    }
 
    public void setAge(int age) {
        this.age = age;
    }
 
    public String getGender() {
        return gender;
    }
 
    public void setGender(String gender) {
        this.gender = gender;
    }
 
}