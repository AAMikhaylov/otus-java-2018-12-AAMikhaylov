package ru.otus.l04;

/*
Copy & MarkSweepCompact
------------------------------------------
-XX:+UseSerialGC
------------------------------------------
Total Mem. - 349968Kb, Free Mem. - 128915Kb
Statistics collection period: from 2019-05-01 21:47:41.446 to 2019-05-01 21:48:41.447
Garbage Collection "Copy": number of starts - 18, duration of work - 615ms
Garbage Collection "MarkSweepCompact": number of starts - 4, duration of work - 786ms
------------------------------------------
Total Mem. - 589804Kb, Free Mem. - 217739Kb
Statistics collection period: from 2019-05-01 21:48:41.497 to 2019-05-01 21:49:41.617
Garbage Collection "MarkSweepCompact": number of starts - 1, duration of work - 550ms
Garbage Collection "Copy": number of starts - 5, duration of work - 485ms
------------------------------------------
Total Mem. - 993856Kb, Free Mem. - 471192Kb
Statistics collection period: from 2019-05-01 21:49:41.618 to 2019-05-01 21:50:41.618
Garbage Collection "MarkSweepCompact": number of starts - 1, duration of work - 906ms
Garbage Collection "Copy": number of starts - 4, duration of work - 641ms
------------------------------------------
Total Mem. - 993856Kb, Free Mem. - 121595Kb
Statistics collection period: from 2019-05-01 21:50:41.618 to 2019-05-01 21:51:41.618
Garbage Collection "Copy": number of starts - 2, duration of work - 547ms
------------------------------------------
Total Mem. - 993856Kb, Free Mem. - 115254Kb
Statistics collection period: from 2019-05-01 21:51:41.618 to 2019-05-01 21:52:41.618
Garbage Collection "Copy": number of starts - 1, duration of work - 0ms
Garbage Collection "MarkSweepCompact": number of starts - 2, duration of work - 3759ms
Total Mem. - 993856Kb, Free Mem. - 140492Kb
------------------------------------------
Total Mem. - 993856Kb, Free Mem. - 133293Kb
Statistics collection period: from 2019-05-01 21:52:41.619 to 2019-05-01 21:53:10.748
Garbage Collection "MarkSweepCompact": number of starts - 3, duration of work - 5639ms


G1
------------------------------------------

Total Mem. - 175104Kb, Free Mem. - 3256Kb
Statistics collection period: from 2019-05-01 22:02:27.741 to 2019-05-01 22:03:27.742
Garbage Collection "G1 Young Generation": number of starts - 76, duration of work - 792ms


------------------------------------------
Total Mem. - 362496Kb, Free Mem. - 13915Kb
Statistics collection period: from 2019-05-01 22:03:27.786 to 2019-05-01 22:04:27.786
Garbage Collection "G1 Young Generation": number of starts - 49, duration of work - 673ms


------------------------------------------
Total Mem. - 536576Kb, Free Mem. - 27120Kb
Statistics collection period: from 2019-05-01 22:04:27.787 to 2019-05-01 22:05:27.788
Garbage Collection "G1 Young Generation": number of starts - 30, duration of work - 579ms


------------------------------------------
Total Mem. - 791552Kb, Free Mem. - 84915Kb
Statistics collection period: from 2019-05-01 22:05:27.788 to 2019-05-01 22:06:27.788
Garbage Collection "G1 Young Generation": number of starts - 20, duration of work - 610ms


------------------------------------------
Total Mem. - 871424Kb, Free Mem. - 13256Kb
Statistics collection period: from 2019-05-01 22:06:27.789 to 2019-05-01 22:07:27.789
Garbage Collection "G1 Young Generation": number of starts - 15, duration of work - 742ms

------------------------------------------
Total Mem. - 1028096Kb, Free Mem. - 173942Kb
Statistics collection period: from 2019-05-01 22:07:27.796 to 2019-05-01 22:08:20.38
Garbage Collection "G1 Old Generation": number of starts - 2, duration of work - 16095ms
Garbage Collection "G1 Young Generation": number of starts - 9, duration of work - 392ms
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        Thread gcSpyThread = new Thread(new GcSpy());
        GarbageProducer garbageProducer = new GarbageProducer();
        gcSpyThread.start();
        try {
            garbageProducer.start();
        } catch (OutOfMemoryError e) {
            System.out.println("Total Mem. - " + Runtime.getRuntime().totalMemory() / 1024 + "Kb, Free Mem. - " + Runtime.getRuntime().freeMemory() / 1024 + "Kb\r\n");
//            System.gc();
//            Thread.sleep(100);
            e.printStackTrace();
            System.err.flush();
        } finally {
            gcSpyThread.interrupt();
        }
    }
}
