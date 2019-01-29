package ru.otus.l03;


import java.util.ArrayList;
import java.util.Collections;


public class Main {
    public static void main(String[] args) {
        MyArrayList<Integer> srcArray = new MyArrayList<>();
        Collections.addAll(srcArray, new Integer[]{1000, 1, 6, 3, 5, 4, 1, 8, 2000, 5000});
        System.out.println("Source list: " + srcArray);
        ArrayList<Number> trgArray = new ArrayList<>();
        Collections.addAll(trgArray, new Number[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
        Collections.copy(trgArray, srcArray);
        System.out.println("Target list after coping from source: " + trgArray);
        Collections.sort(srcArray, (e1, e2) -> e1 - e2);
        System.out.println("Source list after sorting: " + srcArray);
    }


}
