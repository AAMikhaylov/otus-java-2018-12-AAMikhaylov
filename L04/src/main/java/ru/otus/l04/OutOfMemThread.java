package ru.otus.l04;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


public class OutOfMemThread extends Thread {
    int size = 100000;
    List<Long> list = new ArrayList<>();


    @Override
    public void run() {
        try {
            System.out.println();
            while (true) {
                for (int i = 0; i < size; i++)
                    list.add(1000L);
                int delCount = (int) (list.size() - (size * 0.75));
                for (int i = list.size() - 1; i > delCount; i--)
                    list.remove(i);
               sleep(250);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }

    }
}
