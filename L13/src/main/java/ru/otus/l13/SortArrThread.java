package ru.otus.l13;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class SortArrThread extends Thread {
    private final Map<Integer, Integer> ranges;
    private final AtomicInteger threadWorking;
    private final int[] array;
    private int rangeCount = 0;

    public SortArrThread(int[] array, Map<Integer, Integer> ranges, AtomicInteger threadWorking) {
        this.ranges = ranges;
        this.threadWorking = threadWorking;
        this.array = array;
    }

    private void swap(int i, int j) {
        int c = array[i];
        array[i] = array[j];
        array[j] = c;
    }

    private void splitArray(int startIdx, int endIdx) {
        int i = -1;
        int j = -1;
        if (startIdx < endIdx - 1) {
            i = startIdx;
            j = endIdx;
            int m = array[(i + j) / 2];
            while (i < j - 1) {
                while (array[i] < m && i < j - 1)
                    i++;
                while (array[j] > m && j > i + 1)
                    j--;
                if (array[i] == array[j] && i < j - 1) {
                    i++;
                }
                if (array[i] > array[j])
                    swap(i, j);
            }
        }
        if (startIdx == endIdx - 1 && array[startIdx] > array[endIdx])
            swap(startIdx, endIdx);
        synchronized (ranges) {
            if (i != -1 && j != -1) {
                ranges.put(startIdx, i);
                ranges.put(j, endIdx);
            }
            threadWorking.decrementAndGet();
            ranges.notifyAll();
        }
        rangeCount++;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " started");
        try {
            while (true) {
                Map.Entry<Integer, Integer> entry = null;
                synchronized (ranges) {
                    Iterator<Map.Entry<Integer, Integer>> iter = ranges.entrySet().iterator();
                    if (iter.hasNext()) {
                        entry = iter.next();
                        iter.remove();
                        threadWorking.addAndGet(1);
                    } else if (threadWorking.intValue() > 0)
                        ranges.wait();
                    else
                        break;
                }
                if (entry != null)
                    splitArray(entry.getKey(), entry.getValue());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("The thread " + Thread.currentThread().getName() + " is finished. Number of processed ranges - " + rangeCount);
    }
}