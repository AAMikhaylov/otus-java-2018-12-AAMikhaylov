package ru.otus.l05;

import java.util.Arrays;

public class Triangle {
    private double a, b, c, e;

    public Triangle(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
        e = 0.001;
    }

    public void setSize(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public boolean isTrinagle() {
        if (a + b > c && a + c > b && b + c > a)
            return true;
        return false;
    }

    public boolean isEquilateral() throws TriangleException {
        if (!isTrinagle())
            throw new TriangleException("Triangle sides size error!");
        if (a == b && b == c)
            return true;
        return false;
    }

    public boolean isIsosceles() throws TriangleException {
        if (!isTrinagle())
            throw new TriangleException("Triangle sides size error!");

        if (a == b || a == c || c == b)
            return true;
        return false;
    }

    public boolean isRectangular() throws TriangleException {
        if (!isTrinagle())
            throw new TriangleException("Triangle sides size error!");
        double arr[] = {a, b, c};
        Arrays.sort(arr);
        if (Math.abs(arr[2] * arr[2] - arr[0] * arr[0] - arr[1] * arr[1]) < e)
            return true;
        return false;
    }

    public double S() throws TriangleException {
        if (!isTrinagle())
            throw new TriangleException("Triangle sides size error!");
        double p2 = (a + b + c) / 2;
        //формула Герона для треугольника
        return Math.sqrt(p2 * (p2 - a) * (p2 - b) * (p2 - c));
    }

    @Override
    public String toString() {
        return "Triangle{" +
                "a=" + a +
                ", b=" + b +
                ", c=" + c +
                '}';
    }
}
