package ru.otus.l10.user;

public class UserTestDataSet extends DataSet {
    byte length;
    boolean isHumen;
    short speed;
    char name;
    private int age;
    private float width;
    private long weight;
    double height;
    private String firstName;

    public UserTestDataSet(byte length, boolean isHumen, short speed, char name, int age, float width, long weight, double height, String firstName) {
        this.length = length;
        this.isHumen = isHumen;
        this.speed = speed;
        this.name = name;
        this.age = age;
        this.width = width;
        this.weight = weight;
        this.height = height;
        this.firstName = firstName;
    }
}
