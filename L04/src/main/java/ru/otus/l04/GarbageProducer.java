package ru.otus.l04;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class GarbageProducer {
    private final int size = 100000;
    private final List<Long> list = new ArrayList<>();
    private boolean isStart;

    public void start() {
        try {
            isStart = true;
            while (isStart) {
                for (int i = 0; i < size; i++)
                    list.add(1000L);
                int delCount = (int) (list.size() - (size * 0.75));
                for (int i = list.size() - 1; i > delCount; i--)
                    list.remove(i);
                sleep(250);
//                sleep(120);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        isStart = false;
    }
}
