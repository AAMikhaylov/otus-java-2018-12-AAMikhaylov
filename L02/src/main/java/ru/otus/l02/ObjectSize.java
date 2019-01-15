package ru.otus.l02;
import java.lang.management.ManagementFactory;
public class ObjectSize {
    private int arrSize=1000000;
    public void setArrSize(int size) {
        arrSize=size;
    }
    public void measure(FactoryObjects factoryObjects) throws InterruptedException {
        System.gc();
        Thread.sleep(5000);
        System.out.println("pid: " + ManagementFactory.getRuntimeMXBean().getName());
        System.out.println("Appl.size:" + getMem());
        long startMem = getMem();
        Object[] arr = new Object[arrSize];
        long endMem = getMem();
        System.out.println("Ref. size: " + (endMem - startMem)/arr.length);
        startMem = endMem;
        for (int i = 0; i < arr.length; i++) {
            arr[i] = factoryObjects.get();
        }
        endMem = getMem();
        System.out.println("Obj. size: " + (endMem - startMem)/arr.length);
        System.out.println("----------------------------------------\r\n");
    }
    private long getMem() throws InterruptedException {
        System.gc();
        Thread.sleep(10);
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }
}

