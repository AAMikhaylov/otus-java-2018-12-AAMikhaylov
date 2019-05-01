package ru.otus.l04;


public class GcStat {
    private String name;
    private volatile long duration = 0;
    private volatile int count = 0;

    public GcStat(String name, long duration) {
        this.name = name;
        this.duration = duration;
        this.count = 1;
    }

    public String getName() {
        return name;
    }


    public long getDuration() {
        return duration;
    }

    public int getCount() {
        return count;
    }

    public void addCount() {
        count++;
    }

    public void addDuration(long duration) {
        this.duration = this.duration + duration;
    }


}
