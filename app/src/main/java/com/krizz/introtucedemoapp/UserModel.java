package com.krizz.introtucedemoapp;

public class UserModel {
    private String firstName;
    private String lastName;
    private String age;
    private String gender;
    private String country;
    private String state;
    private String homeTown;
    private String phoneNumber;
    private String telephoneNumber;
    private String imageUrl;
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public UserModel() {
    }

    public UserModel(String firstName, String lastName, String age, String gender, String country, String state, String homeTown, String phoneNumber, String telephoneNumber, String imageUrl, String key) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;
        this.country = country;
        this.state = state;
        this.homeTown = homeTown;
        this.phoneNumber = phoneNumber;
        this.telephoneNumber = telephoneNumber;
        this.imageUrl = imageUrl;
        this.key= key;
    }



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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getHomeTown() {
        return homeTown;
    }

    public void setHomeTown(String homeTown) {
        this.homeTown = homeTown;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age='" + age + '\'' +
                ", gender='" + gender + '\'' +
                ", country='" + country + '\'' +
                ", state='" + state + '\'' +
                ", homeTown='" + homeTown + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", telephoneNumber='" + telephoneNumber + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}
