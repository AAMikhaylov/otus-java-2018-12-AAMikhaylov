package ru.otus.l13;
import java.util.Arrays;


public class Main {


    private static int[] getArray(int size) {

        int[] result = new int[size];
        for (int i = 0; i < size; i++)
            result[i] = (int)( Math.random() * size);
        return result;
    }

    public static void main(String[] args) {
        int[] arr = getArray(100000);
        System.out.println(Arrays.toString(arr));
        SortArray sa = new SortArray();
        sa.sort(arr);
        System.out.println(Arrays.toString(arr));


    }
}
