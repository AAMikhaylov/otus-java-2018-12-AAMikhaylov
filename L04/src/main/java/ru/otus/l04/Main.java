package ru.otus.l04;

public class Main {
    public static void main(String[] args) {
        OutOfMemThread th=new OutOfMemThread();
        th.start();
        try {
            th.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
