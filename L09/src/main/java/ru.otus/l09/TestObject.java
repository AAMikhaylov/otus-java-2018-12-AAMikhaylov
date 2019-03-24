package ru.otus.l09;

import java.io.Serializable;
import java.util.*;

public class TestObject {
    byte test1;
    boolean test2;
    char test3;
    private short test4;
    private int test5;
    private float test6;
    private long test7;
    private double test8;
    private String test9;
    private int testArr1[];
    private double testArr2[];
    private Integer testArr3[];
    private User u;
    private List<Integer> collect1 = new ArrayList<>();
    private ArrayList<User> collect2 = new ArrayList<>();
    Set<String> s = new HashSet<>();


    public TestObject() {
        test1 = 100;
        test2 = true;
        test3 = (char) 100;
        test4 = 1000;
        test5 = 100000;
        test6 = (float) 0.1;
        test7 = 6000000000000L;
        test8 = 5.0 / 6000000000000L;
        test9 = "test string";
        testArr1 = new int[]{1, 2, 3, 4, 56};
        testArr2 = new double[]{1.1, 2.3, 3.5, 4.66, 56.88};
        testArr3 = new Integer[]{1, 2, 3, 4, 56};
        collect1.add(10);
        collect1.add(11);
        collect2.add(new User());
        collect2.add(new User());
        u = new User();
        s.add("String 1");
        s.add("String 2");

    }
}
