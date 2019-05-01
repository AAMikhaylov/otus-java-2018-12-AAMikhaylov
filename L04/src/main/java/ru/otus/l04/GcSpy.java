package ru.otus.l04;

import com.sun.management.GarbageCollectionNotificationInfo;

import javax.management.ListenerNotFoundException;
import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GcSpy implements Runnable {
    private final Set<GcStat> gcStats = new HashSet<>();
    private final List<Runnable> unregCmds = new ArrayList<>();
    private Timestamp endTime, startTime;

    private synchronized void addGcStatInfo(String gcName, long duration) {
        for (GcStat stat : gcStats) {
            if (stat.getName().equals(gcName)) {
                stat.addCount();
                stat.addDuration(duration);
                System.out.flush();
                return;
            }
        }
        gcStats.add(new GcStat(gcName, duration));
        System.out.flush();
    }

    @Override
    public void run() {
        initListeners();
        try {
            startTime = new Timestamp(System.currentTimeMillis());
            while (true) {
                Thread.sleep(60000);
                printMesure();
                System.out.flush();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.err.flush();
        }
        printMesure();
        releaseListeners();
    }

    private void releaseListeners() {
        unregCmds.forEach(Runnable::run);
        unregCmds.clear();
    }

    private void initListeners() {
        List<GarbageCollectorMXBean> gcbeans = ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gcbean : gcbeans) {
            NotificationEmitter emitter = (NotificationEmitter) gcbean;
            NotificationListener listener;
            listener = ((notification, handback) -> {
                if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                    GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());
                    addGcStatInfo(info.getGcName(), info.getGcInfo().getDuration());
                }
            });
            emitter.addNotificationListener(listener, null, null);
            unregCmds.add(() -> {
                try {
                    emitter.removeNotificationListener(listener);
                } catch (ListenerNotFoundException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private synchronized void printMesure() {
        endTime = new Timestamp(System.currentTimeMillis());
        StringBuilder result = new StringBuilder("------------------------------------------\r\n");
        result.append("Total Mem. - " + Runtime.getRuntime().totalMemory() / 1024 + "Kb, Free Mem. - " + Runtime.getRuntime().freeMemory() / 1024 + "Kb\r\n");
        result.append("Statistics collection period: from " + startTime + " to " + endTime + "\r\n");


        gcStats.forEach((stat) -> {
            result.append("Garbage Collection \"" + stat.getName() + "\": number of starts - ");
            synchronized (stat) {
                result.append(stat.getCount() + ", duration of work - ");
                result.append(stat.getDuration() + "ms\r\n");
            }
        });
        result.append("\r\n");
        gcStats.clear();
        System.out.println(result);
        System.out.flush();
        startTime = new Timestamp(System.currentTimeMillis());
    }
}