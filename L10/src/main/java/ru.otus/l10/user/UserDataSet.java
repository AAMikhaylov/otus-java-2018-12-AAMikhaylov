package ru.otus.l10.user;

public class UserDataSet extends DataSet {
    private String name;
    private int age;

    public UserDataSet() {
    }

    public UserDataSet(String name, int age) {
        this.name = name;
        this.age = age;

    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "UserDataSet{" +
                "id=" + id +
                ", name=\"" + name +
                "\", age=" + age +
                '}';
    }
}
