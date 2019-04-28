package ru.otus.l04;

import com.sun.management.GarbageCollectionNotificationInfo;

import javax.management.ListenerNotFoundException;
import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GcSpy {
    private final Set<GcStat> gcStats = new HashSet<>();
    private final List<Runnable> unregCmds = new ArrayList<>();
    private long startTime, endTime;

    private void addGcStatInfo(String gcName, long duration) {
        for (GcStat stat : gcStats) {
            if (stat.getName().equals(gcName)) {
                stat.addCount();
                stat.addDuration(duration);
                return;
            }
        }
        gcStats.add(new GcStat(gcName, duration));
    }

    public void restart() {
        stop();
        start();
    }

    public void stop() {
        endTime = System.currentTimeMillis();
        unregCmds.forEach(Runnable::run);
        unregCmds.clear();
    }

    public void start() {
        gcStats.clear();
        startTime = System.currentTimeMillis();
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


    public String printStat() {
        StringBuilder result = new StringBuilder("------------------------------------------\r\n");
        result.append("Statistics collection period: " + (endTime - startTime) + "ms\r\n");


        gcStats.forEach((stat) -> {
            result.append("Garbage Collection \"" + stat.getName() + "\": number of starts - ");
            result.append(stat.getCount() + ", duration of work - ");
            result.append(stat.getDuration() + "ms\r\n");
        });
        result.append("\r\n");
        return result.toString();
    }
}