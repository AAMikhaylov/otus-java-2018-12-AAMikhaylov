package ru.otus.l05;


public class Main {
    public static void main(String... args) throws TriangleException {
        Triangle t = new Triangle(1, 1, 2);
        System.out.println(t.isRectangular());
        System.out.println(t.S());
    }
}
