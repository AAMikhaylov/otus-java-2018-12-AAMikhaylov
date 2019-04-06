package ru.otus.l11.base.dataSets;

public class HumanDataSet extends DataSet {
    byte length;
    boolean isHumen;
    short speed;
    char name;
    private int age;
    private float width;
    private long weight;
    double height;
    private String firstName;

    public HumanDataSet() {
    }

    public HumanDataSet(byte length, boolean isHumen, short speed, char name, int age, float width, long weight, double height, String firstName) {
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

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public byte getLength() {
        return length;
    }

    public boolean isHumen() {
        return isHumen;
    }

    public short getSpeed() {
        return speed;
    }

    public char getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public float getWidth() {
        return width;
    }

    public long getWeight() {
        return weight;
    }

    public double getHeight() {
        return height;
    }

    public String getFirstName() {
        return firstName;
    }

    @Override
    public String toString() {
        return "HumanDataSet{" +
                "length=" + length +
                ", isHumen=" + isHumen +
                ", speed=" + speed +
                ", name=" + name +
                ", age=" + age +
                ", width=" + width +
                ", weight=" + weight +
                ", height=" + height +
                ", firstName='" + firstName + '\'' +
                '}';
    }
}
