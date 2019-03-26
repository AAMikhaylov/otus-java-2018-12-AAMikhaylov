package ru.otus.l09;

import ru.otus.l09.User;

import java.io.Serializable;
import java.util.*;

public class TestObject {
    byte test1;

    boolean test2;
    boolean test2_1;
    char test3;

    private short test4;
    private int test5;
    private float test6;
    private float test6_1;
    private long test7;
    private double test8;
    private double test8_1;
    private String test9;
    private int testArr1[];
    private double testArr2[];
    private Integer testArr3[];
    int[] test10;
    private User u;
    private List<Integer> collect1 = new ArrayList<>();
    private ArrayList<User> collect2 = new ArrayList<>();
    private List<String> collect3;
    private List<String> collect4;
    Set<String> s = new HashSet<>();
    int test11;
    String test12;

    public TestObject() {
        String nullString=null;
        String emptyString="";

        test1 = 100;
        test2 = true;
        test2_1 = false;
        test3 = (char) 100;

        test4 = 1000;
        test5 = 100000;
        test6 = (float) 0.1;
        test6_1 = (float) 0.00000000001;
        test7 = 6000000000000000000L;
        test8 = 0.1;
        test8 = 0.000000000009;
        test9 = "test string";

        testArr1 = new int[]{1, 2, 3, 4, 56};
        testArr2 = new double[]{1.1, 2.3, 3.5, 4.66, 56.88};
        testArr3 = new Integer[]{1, 2, 3, 4, 56};
        collect1.add(10);
        collect1.add(11);
        collect2.add(new User());
        collect2.add(new User());
        test10 = new int[]{1,2};
        collect3=Collections.singletonList("1");
        u = new User();
        s.add("String 1");
        s.add("String 2");
        test11 = 1;
        test12 ="AnyString";
    }
}
