package org.vaadin.aggrid;

import java.util.Objects;

/**
 * @author jcgueriaud
 */
public class Person {
    private int id;
    private String firstName, lastName;
    private int age;
    private String attribute1, attribute2, attribute3, attribute4, attribute5, attribute6, attribute7, attribute8;

    private Price price;

    public Person() {
    }

    public int getId() {
        return id;
    }

    public Person setId(int id) {
        this.id = id;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public Person setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public Person setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public int getAge() {
        return age;
    }

    public Person setAge(int age) {
        this.age = age;
        return this;
    }

    public String getAttribute1() {
        return attribute1;
    }

    public Person setAttribute1(String attribute1) {
        this.attribute1 = attribute1;
        return this;
    }

    public String getAttribute2() {
        return attribute2;
    }

    public Person setAttribute2(String attribute2) {
        this.attribute2 = attribute2;
        return this;
    }

    public String getAttribute3() {
        return attribute3;
    }

    public Person setAttribute3(String attribute3) {
        this.attribute3 = attribute3;
        return this;
    }

    public String getAttribute4() {
        return attribute4;
    }

    public Person setAttribute4(String attribute4) {
        this.attribute4 = attribute4;
        return this;
    }

    public String getAttribute5() {
        return attribute5;
    }

    public Person setAttribute5(String attribute5) {
        this.attribute5 = attribute5;
        return this;
    }

    public String getAttribute6() {
        return attribute6;
    }

    public Person setAttribute6(String attribute6) {
        this.attribute6 = attribute6;
        return this;
    }

    public String getAttribute7() {
        return attribute7;
    }

    public Person setAttribute7(String attribute7) {
        this.attribute7 = attribute7;
        return this;
    }

    public String getAttribute8() {
        return attribute8;
    }

    public Person setAttribute8(String attribute8) {
        this.attribute8 = attribute8;
        return this;
    }

    public Price getPrice() {
        return price;
    }

    public Person setPrice(Price price) {
        this.price = price;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return id == person.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Person{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
