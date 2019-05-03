package ru.otus.l06;

import java.lang.ref.SoftReference;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Function;

public class CacheEngineImpl<K, V> implements CacheEngine<K, V> {
    private final static int TIME_THRESHOLD_MS = 5;
    private final int maxElements;
    private final long lifeTimeMs;
    private final long idleTimeMs;
    private final boolean isEternal;
    private int hit = 0;
    private int miss = 0;
    private final Timer timer = new Timer();
    private final Map<K, SoftReference<CacheElement<K, V>>> elements = new LinkedHashMap<>();

    public CacheEngineImpl(int maxElements, long lifeTimeMs, long idleTimeMs, boolean isEternal) {
        this.maxElements = maxElements;
        this.lifeTimeMs = lifeTimeMs > 0 ? lifeTimeMs : 0;
        this.idleTimeMs = idleTimeMs > 0 ? idleTimeMs : 0;
        this.isEternal = lifeTimeMs == 0 && idleTimeMs == 0 || isEternal;
    }

    @Override
    public void put(CacheElement<K, V> cacheElement) {
        if (elements.size() == maxElements) {
            K key = elements.keySet().iterator().next();
            elements.remove(key);
        }
        K key = cacheElement.getKey();
        SoftReference<CacheElement<K, V>> element = new SoftReference<>(cacheElement);
        elements.put(key, element);
        if (!isEternal) {
            if (lifeTimeMs != 0) {
                TimerTask lifeTimerTask = getTimerTask(key, lifeElement -> lifeElement.getCreationTime() + lifeTimeMs);
                timer.schedule(lifeTimerTask, lifeTimeMs);
            }
            if (idleTimeMs != 0) {
                TimerTask idleTimerTask = getTimerTask(key, idleElement -> idleElement.getLastAccessTime() + idleTimeMs);
                timer.schedule(idleTimerTask, idleTimeMs, idleTimeMs);
            }
        }
    }

    TimerTask getTimerTask(K key, Function<CacheElement<K, V>, Long> timeFunction) {
        return new TimerTask() {
            @Override
            public void run() {
                SoftReference<CacheElement<K, V>> element = elements.get(key);
                if (element == null) {
                    elements.remove(key);
                    this.cancel();
                } else if (element.get() == null || isT1BeforeT2(timeFunction.apply(element.get()), System.currentTimeMillis())) {
                    elements.remove(key);
                    this.cancel();
                }
            }
        };
    }

    @Override
    public CacheElement<K, V> get(K key) {
        SoftReference<CacheElement<K, V>> element = elements.get(key);
        if (element == null) {
            miss++;
            return null;
        }
        CacheElement<K, V> cacheElement = element.get();
        if (cacheElement != null) {
            hit++;
            cacheElement.setAccessed();
        } else
            miss++;
        return cacheElement;
    }

    @Override
    public int getHitCount() {
        return hit;
    }

    @Override
    public int getMissCount() {
        return miss;
    }

    @Override
    public void dispose() {
        timer.cancel();
    }

    private boolean isT1BeforeT2(long t1, long t2) {
        return t1 < t2 + TIME_THRESHOLD_MS;

    }
}

