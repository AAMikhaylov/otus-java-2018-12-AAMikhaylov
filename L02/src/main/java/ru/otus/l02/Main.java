package ru.otus.l02;
import java.util.ArrayList;
public class Main {
    public static void main(String[] args) throws InterruptedException {
        ObjectSize objectSize = new ObjectSize();
        FactoryObjects<String> emptyStrings = new FactoryObjects(String.class);
        System.out.println("For empty String:");
        objectSize.measure(emptyStrings);
        FactoryObjects<String> anyStrings = new FactoryObjects(String.class,"abcdefghij");
        System.out.println("For string with \"abcdefghij\":");
        objectSize.measure(anyStrings);
        FactoryObjects<ArrayList> emptyArrayList = new FactoryObjects(ArrayList.class);
        System.out.println("For empty ArrayList:");
        objectSize.measure(emptyArrayList);
        FactoryObjects<ArrayList> arrayList100 = new FactoryObjects(ArrayList.class,100);
        System.out.println("For ArrayList with 100 elements:");
        objectSize.measure(arrayList100);
        FactoryObjects<ArrayList> arrayList10 = new FactoryObjects(ArrayList.class,10);
        System.out.println("For ArrayList with 10 elements:");
        objectSize.measure(arrayList10);
        FactoryObjects<ArrayList> arrayList1 = new FactoryObjects(ArrayList.class,1);
        System.out.println("For ArrayList with 1 elements:");
        objectSize.measure(arrayList1);
    }
}
