package ru.otus.l06;

public class CacheMain {

    private void lifeCache() throws InterruptedException {
        int size = 50;
        CacheEngine<Integer, String> cache = new CacheEngineImpl<>(size, 1000, 0, false);
        for (int i = 0; i < size; i++) {
            cache.put(new CacheElement<>(i, "String number " + i));
            Thread.sleep(10);
        }
        Thread.sleep(500);
        for (int i = 0; i < size; i++) {
            CacheElement<Integer, String> element = cache.get(i);
            System.out.println(element != null ? element.getValue() : "null");

        }
        System.out.println("Cache hits: " + cache.getHitCount());
        System.out.println("Cache misses: " + cache.getMissCount());
        cache.dispose();
    }
    private void idleCache() throws InterruptedException {
        int size = 50;
        CacheEngine<Integer, String> cache = new CacheEngineImpl<>(size,0 , 1000, false);
        for (int i = 0; i < size; i++)
            cache.put(new CacheElement<>(i, "String number " + i));

        Thread.sleep(500);
        for (int i = size/2; i < size; i++) {
            CacheElement<Integer, String> element = cache.get(i);
        }

        Thread.sleep(500);
        for (int i = 0; i < size; i++) {
            CacheElement<Integer, String> element = cache.get(i);
            System.out.println(element != null ? element.getValue() : "null");

        }
        System.out.println("Cache hits: " + cache.getHitCount());
        System.out.println("Cache misses: " + cache.getMissCount());
        cache.dispose();
    }



    private void softCache() throws InterruptedException {
        int size = 5_000_000;
        CacheEngine<Integer, String> cache = new CacheEngineImpl<>(size,0 , 0, true);
        try {
            for (int i = 0; i < size; i++)
                cache.put(new CacheElement<>(i, "String number " + i));
        } finally {
            for (int i = 0; i < size; i++) {
                CacheElement<Integer, String> element = cache.get(i);
            }
            System.out.println("Cache hits: " + cache.getHitCount());
            System.out.println("Cache misses: " + cache.getMissCount());
            cache.dispose();

        }
    }

    public static void main(String[] args) throws InterruptedException {
        //new CacheMain().lifeCache();
//        new CacheMain().idleCache();
        new CacheMain().softCache();
    }


}
