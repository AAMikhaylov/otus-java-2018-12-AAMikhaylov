package ru.otus.l04;
/*
Copy & MarkSweepCompact
------------------------------------------
        Statistics collection period: 351170ms
        Garbage Collection "Copy": number of starts - 30, duration of work - 2581ms
        Garbage Collection "MarkSweepCompact": number of starts - 11, duration of work - 28810ms

G1
------------------------------------------
Statistics collection period: 359042ms
Garbage Collection "G1 Young Generation": number of starts - 197, duration of work - 10466ms
Garbage Collection "G1 Old Generation": number of starts - 2, duration of work - 11491ms
 */

public class Main {
    public static void main(String[] args) {
        GcSpy gcSpy = new GcSpy();
        gcSpy.start();
        OutOfMemThread th = new OutOfMemThread();
        th.start();
        try {
            th.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        gcSpy.stop();
        System.out.println(gcSpy.printStat());
    }

}
