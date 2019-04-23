package ru.otus.l13;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


public class SortArray {
    final static int THREADS_COUNT = 4;

    public void sort(int[] arr) {
        Map<Integer, Integer> ranges;
        ranges = new HashMap<>();
        ranges.put(0, arr.length - 1);
        AtomicInteger threadWorking = new AtomicInteger(0);
        SortArrThread[] threads = new SortArrThread[THREADS_COUNT];
        for (int i = 0; i < THREADS_COUNT; i++) {
            threads[i] = new SortArrThread(arr, ranges, threadWorking);
            threads[i].start();
        }
        try {
            for (int i = 0; i < THREADS_COUNT; i++)
                threads[i].join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Number of unprocessed ranges - " + ranges.size());
    }
}
